package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.CriptomonedasInsuficientesException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.repositorio.RepositorioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.infraestructura.servicio.impl.ServicioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioTransaccionesImpl implements ServicioTransacciones {

    RepositorioTransacciones repositorioTransacciones;
    ServicioUsuario servicioUsuario;
    private ServicioEmail servicioEmail;


    @Autowired
    public ServicioTransaccionesImpl(RepositorioTransacciones repositorioTransacciones, ServicioUsuario servicioUsuario, ServicioEmail servicioEmail) {
        this.repositorioTransacciones = repositorioTransacciones;
        this.servicioUsuario = servicioUsuario;
        this.servicioEmail = servicioEmail;

    }

    @Override
    public String crearTransaccion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario) {
        Double precioTotalDeTransaccion = precioDeCripto*cantidadDeCripto;

        if (cantidadDeCripto <= 0.0){
            return "La cantidad debe ser mayor que 0.";
        }

        if (tipoDeTransaccion.equals(TipoTransaccion.COMPRA)){

            if (verificarQueTengaSaldoSuficienteParaComprar(precioTotalDeTransaccion, usuario.getSaldo())){
                //aca creo la transaccion.
                Transaccion nuevaTransaccion = generarTransaccion(precioDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, criptomoneda,cantidadDeCripto);
                //le RESTO el saldo al usuario
                //System.out.println("saldo antes: " + usuario.getSaldo());
                //usuario.setSaldo(usuario.getSaldo() - precioTotalDeTransaccion);
                servicioUsuario.restarSaldo(usuario.getId(), precioTotalDeTransaccion);
                //System.out.println("saldo despues: " + usuario.getSaldo());

                //Ahora guardo la transaccion en la bdd (osea se mezclarian muchas transacciones de ditintos user)
                repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

                //Envio el mail con el resumen de la transaccion al user.
                String destinatario = usuario.getEmail();
                String asunto = "Resumen de transacción";
                String cuerpo = servicioEmail.formarMensaje(usuario, nuevaTransaccion);

                servicioEmail.enviarEmail(destinatario, asunto, cuerpo);

                //y retorno el msj exitoso
                return "Transaccion exitosa.";
            }else {
                throw new SaldoInsuficienteException("No tienes sufieciente saldo.");
            }
        }else{

            if (verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(criptomoneda.getNombre(),cantidadDeCripto,usuario.getId())){
                //ceo la trnasaccion
                Transaccion nuevaTransaccion = generarTransaccion(precioDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, criptomoneda, cantidadDeCripto);
                //le SUMO al saldo del usuario
                servicioUsuario.sumarSaldo(usuario.getId(), precioTotalDeTransaccion);
                //Ahora guardo la transaccion en la bdd (osea se mezclarian muchas transacciones de ditintos user)
                repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

                //Envio el mail con el resumen de la transaccion al user.
                String destinatario = usuario.getEmail();
                String asunto = "Resumen de transacción";
                String cuerpo = servicioEmail.formarMensaje(usuario, nuevaTransaccion);

                servicioEmail.enviarEmail(destinatario, asunto, cuerpo);

                //y retorno el msj exitoso
                return "Transaccion exitosa.";
            }else {
                throw new CriptomonedasInsuficientesException("No tienes la cantidad suficientes de criptomonedas que quieres vender.");
            }
        }
    }

    @Override
    public Boolean verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(String nombreDeCripto, Double cantidadDeCripto, Long idDeUsuario) {
        /*aca tengo que, de TODAS las transacciones que tengo en la bdd, filtrar las que son:
            - de TipoTransaccion.COMPRA    &&    del idDeUsuario = transaccion.getUsuario().getId()    &&    de nombreDeCripto = nombreDeCripto
          y de las transacciones filtradas, sumar la cantidadDeCripto de cada una.
           Ahora, filtrar de nuevo todas las transacciones que son:
            - de TipoTransaccion.VENTA    &&    del idDeUsuario = transaccion.getUsuario().getId()    &&    de nombreDeCripto = nombreDeCripto
          y de las transacciones filtradas, sumar la cantidadDeCriptos de cada una.
          AHORA: cantidadDeCriptosTotales = cantidaddeCiptosCompradas - cantidadDeCriptosVentidas;
           A mi me interesa el resultado de esa resta para comparar con la cantidadDeCripto que quiere vender el usuario.
          */
        Double cantidadCompradaDeUnaCripto = repositorioTransacciones.buscarCantidadCompradadeUnaCriptoDeUnUsuario(nombreDeCripto, idDeUsuario) == null ? 0.0 : repositorioTransacciones.buscarCantidadCompradadeUnaCriptoDeUnUsuario(nombreDeCripto, idDeUsuario);
        Double cantidadVendidaDeUnaCripto = repositorioTransacciones.buscarCantidadVendidadeUnaCriptoDeUnUsuario(nombreDeCripto, idDeUsuario) == null ? 0.0 : repositorioTransacciones.buscarCantidadVendidadeUnaCriptoDeUnUsuario(nombreDeCripto, idDeUsuario);

        Double cantidadTotalDeUnaCriptoDelUsuario = cantidadCompradaDeUnaCripto - cantidadVendidaDeUnaCripto;

        return cantidadTotalDeUnaCriptoDelUsuario >= cantidadDeCripto;
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
    public Double dameLaCantidadQueEsteUsuarioTieneDeEstaCripto(Usuario usuario, String idCriptomoneda) {
        Double cantComprada = repositorioTransacciones.buscarCantidadCompradadeUnaCriptoDeUnUsuario(idCriptomoneda, usuario.getId());
        Double cantVendida = repositorioTransacciones.buscarCantidadVendidadeUnaCriptoDeUnUsuario(idCriptomoneda, usuario.getId());

        if (cantComprada == null || cantComprada <= 0.0) {
            return 0.0;
        }

        if (cantVendida == null) {
            cantVendida = 0.0;
        }
        return cantComprada-cantVendida;
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