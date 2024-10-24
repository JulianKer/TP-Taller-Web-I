package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.CriptomonedasInsuficientesException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.repositorio.RepositorioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.infraestructura.servicio.impl.ServicioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ServicioTransaccionesImpl implements ServicioTransacciones {

    RepositorioTransacciones repositorioTransacciones;
    ServicioUsuario servicioUsuario;
    private ServicioEmail servicioEmail;
    private ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda;


    @Autowired
    public ServicioTransaccionesImpl(RepositorioTransacciones repositorioTransacciones, ServicioUsuario servicioUsuario, ServicioEmail servicioEmail, ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda) {
        this.repositorioTransacciones = repositorioTransacciones;
        this.servicioUsuario = servicioUsuario;
        this.servicioEmail = servicioEmail;
        this.servicioBilleteraUsuarioCriptomoneda = servicioBilleteraUsuarioCriptomoneda;
    }

    @Override
    public String crearTransaccion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario) {
        Double precioTotalDeTransaccion = precioDeCripto * cantidadDeCripto;

        if (cantidadDeCripto <= 0.0) {
            return "La cantidad debe ser mayor que 0.";
        }

        switch (tipoDeTransaccion) {
            case COMPRA:
                if (verificarQueTengaSaldoSuficienteParaComprar(precioTotalDeTransaccion, usuario.getSaldo())) {
                    Transaccion nuevaTransaccion = generarTransaccion(precioDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, criptomoneda, cantidadDeCripto);
                    repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

                    BilleteraUsuarioCriptomoneda billetera = servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario);
                    if(billetera == null) {
                        billetera = new BilleteraUsuarioCriptomoneda();
                        billetera.setCriptomoneda(criptomoneda);
                        billetera.setUsuario(usuario);
                        billetera.setCantidadDeCripto(0.0);
                        servicioBilleteraUsuarioCriptomoneda.guardarBilletera(billetera);
                    }

                    billetera.incrementarCantidadDeCripto(cantidadDeCripto);
                    servicioBilleteraUsuarioCriptomoneda.actualizarBilletera(billetera);
                    servicioUsuario.restarSaldo(usuario.getId(), precioTotalDeTransaccion);

                    String destinatario = usuario.getEmail();
                    String asunto = "Resumen de transacción";
                    String cuerpo = servicioEmail.formarMensaje(usuario, nuevaTransaccion);
                    servicioEmail.enviarEmail(destinatario, asunto, cuerpo);
                    return "Transaccion exitosa.";
                } else {
                    throw new SaldoInsuficienteException("No tienes sufieciente saldo.");
                }
            case VENTA:
            case DEVOLUCION:

                BilleteraUsuarioCriptomoneda billetera = servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario);

                if (servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(billetera, cantidadDeCripto)){
                    Transaccion nuevaTransaccion = generarTransaccion(precioDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, criptomoneda, cantidadDeCripto);
                    repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

                    servicioUsuario.sumarSaldo(usuario.getId(), precioTotalDeTransaccion);
                    billetera.decrementarCantidadDeCripto(cantidadDeCripto);

                    String destinatario = usuario.getEmail();
                    String asunto = "Resumen de transacción";
                    String cuerpo = servicioEmail.formarMensaje(usuario, nuevaTransaccion);
                    servicioEmail.enviarEmail(destinatario, asunto, cuerpo);
                    return "Transaccion exitosa.";
                } else {
                    throw new CriptomonedasInsuficientesException("No tienes la cantidad suficientes de criptomonedas que quieres vender.");
                }

            default:
                return null;
        }
    }

    @Override
    public List<Transaccion> obtenerHistorialTransaccionesDeUsuario(Long idDeUsuario) {
        return repositorioTransacciones.obtenerHistorialUsuario(idDeUsuario);
    }

    @Override
    public Transaccion generarTransaccion(Double precioDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Criptomoneda criptoEncontrada, Double cantidadDeCripto) {
        Transaccion nuevaTransaccion = new Transaccion();
        nuevaTransaccion.setMontoTotal(precioTotalDeTransaccion);
        nuevaTransaccion.setUsuario(usuario);
        nuevaTransaccion.setTipo(tipoDeTransaccion);
        nuevaTransaccion.setCriptomoneda(criptoEncontrada);
        nuevaTransaccion.setPrecioAlQueSehizo(precioDeCripto);
        nuevaTransaccion.setFechaDeTransaccion(LocalDate.now());
        nuevaTransaccion.setCantidadDeCripto(cantidadDeCripto);
        return nuevaTransaccion;
    }

    @Override
    public Boolean verificarQueTengaSaldoSuficienteParaComprar(Double precioTotalDeTransaccion, Double saldoDelUsuario) {
//        System.out.println("Saldo del usuario: " + saldoDelUsuario);
//        System.out.println("Precio total de la transacción: " + precioTotalDeTransaccion);
        return saldoDelUsuario >= precioTotalDeTransaccion;
    }

    @Override
    public List<Transaccion> obtenerTransaccionesDeEstaCripto(String idCriptomoneda) {
        return repositorioTransacciones.obtenerTransaccionesDeEstaCripto(idCriptomoneda);
    }

    @Override
    public void eliminarTransaccion(Transaccion transaccion) {
        repositorioTransacciones.eliminarTransaccion(transaccion);
    }

    @Override
    public List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario) {
        return repositorioTransacciones.filtrarTransacciones(tipoTransaccion, idUsuario);
    }

}