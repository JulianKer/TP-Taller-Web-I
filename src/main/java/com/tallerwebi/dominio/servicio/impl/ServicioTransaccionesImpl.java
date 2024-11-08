package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.*;
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
import java.util.stream.Collectors;

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

    //----- METODO PRINCIPAL PARA GENERAR LA TRANSACCION -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public String crearTransaccion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Criptomoneda criptoAObtener, Double precioDeCriptoAObtener, Boolean esProgramada) {
        Double precioTotalDeTransaccion = precioDeCripto * cantidadDeCripto;

        if (cantidadDeCripto <= 0.0) {
            return "La cantidad debe ser mayor que 0.";
        }

        switch (tipoDeTransaccion) {
            case COMPRA:
                return intentarHacerUnaCompra(criptomoneda, precioDeCripto, cantidadDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, esProgramada);
            case VENTA:
            case DEVOLUCION:
                return intentarHacerUnaVentaODevolucion(criptomoneda, precioDeCripto, cantidadDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, esProgramada);
            case INTERCAMBIO:
                return intentarHacerUnIntercambio(criptomoneda, precioDeCripto, cantidadDeCripto, tipoDeTransaccion, usuario, criptoAObtener, precioDeCriptoAObtener, esProgramada);
            default:
                return "La transaccion no se pudo realizar. Tipo de transaccion desconocida";
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private String intentarHacerUnaVentaODevolucion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Boolean esProgramada) {
        BilleteraUsuarioCriptomoneda billetera = servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario);

        if (billetera != null && servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(billetera, cantidadDeCripto)){
            Transaccion nuevaTransaccion = generarTransaccion(precioDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, criptomoneda, cantidadDeCripto, null, null, null, esProgramada);
            repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

            servicioUsuario.sumarSaldo(usuario.getId(), precioTotalDeTransaccion);
            billetera.decrementarCantidadDeCripto(cantidadDeCripto);

            generarYEnviarMail(usuario, nuevaTransaccion);
            return "Transaccion exitosa.";
        } else {
            throw new CriptomonedasInsuficientesException("No tienes la cantidad suficientes de criptomonedas que quieres vender.");
        }
    }

    private String intentarHacerUnaCompra(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Boolean esProgramada) {
        if (verificarQueTengaSaldoSuficienteParaComprar(precioTotalDeTransaccion, usuario.getSaldo())) {
            Transaccion nuevaTransaccion = generarTransaccion(precioDeCripto, tipoDeTransaccion, usuario, precioTotalDeTransaccion, criptomoneda, cantidadDeCripto, null, null, null, esProgramada);
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

            generarYEnviarMail(usuario, nuevaTransaccion);
            return "Transaccion exitosa.";
        } else {
            throw new SaldoInsuficienteException("No tienes sufieciente saldo.");
        }
    }

    private String intentarHacerUnIntercambio(Criptomoneda criptoADar, Double precioDeCriptoADar, Double cantidadDeCriptoADar, TipoTransaccion tipoDeTransaccion, Usuario usuario, Criptomoneda criptoAObtener, Double precioDeCriptoAObtener, Boolean esProgramada) {
        Double precioTotalDeTransaccion = precioDeCriptoADar * cantidadDeCriptoADar;

        BilleteraUsuarioCriptomoneda billeteraCriptoADar = servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptoADar, usuario);
        BilleteraUsuarioCriptomoneda billeteraCriptoAObtener = servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptoAObtener, usuario);

        if (billeteraCriptoADar == null || !servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaIntercambiar(billeteraCriptoADar, cantidadDeCriptoADar)){
            throw new CriptomonedasInsuficientesException("No tienes la cantidad suficientes de criptomonedas que quieres intercambiar.");
        }

        if (billeteraCriptoAObtener == null){
            billeteraCriptoAObtener = new BilleteraUsuarioCriptomoneda();
            billeteraCriptoAObtener.setCriptomoneda(criptoAObtener);
            billeteraCriptoAObtener.setUsuario(usuario);
            billeteraCriptoAObtener.setCantidadDeCripto(0.0);
            servicioBilleteraUsuarioCriptomoneda.guardarBilletera(billeteraCriptoAObtener);
        }

        Double cantidadDeCriptoAObtener = precioTotalDeTransaccion / precioDeCriptoAObtener;
        billeteraCriptoADar.decrementarCantidadDeCripto(cantidadDeCriptoADar);
        billeteraCriptoAObtener.incrementarCantidadDeCripto(cantidadDeCriptoAObtener);

        Transaccion nuevaTransaccion = generarTransaccion(precioDeCriptoADar, tipoDeTransaccion, usuario, precioTotalDeTransaccion, criptoADar, cantidadDeCriptoADar, criptoAObtener, cantidadDeCriptoAObtener, precioDeCriptoAObtener, esProgramada);
        repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

        generarYEnviarMail(usuario, nuevaTransaccion);
        return "Transaccion exitosa.";
    }

    // -------  envio del mail   ---------------------------------------------------------------------
    private void generarYEnviarMail(Usuario usuario, Transaccion nuevaTransaccion) {
        String destinatario = usuario.getEmail();
        String asunto = "Resumen de transacción";
        String cuerpo = "";

        if (nuevaTransaccion.getTipo().equals(TipoTransaccion.INTERCAMBIO)){
            cuerpo = servicioEmail.formarMensajeParaIntercambio(usuario, nuevaTransaccion);
        }else{
            cuerpo = servicioEmail.formarMensaje(usuario, nuevaTransaccion);
        }
        servicioEmail.enviarEmail(destinatario, asunto, cuerpo);
    }
    // -----------------------------------------------------------------------------------------------

    @Override
    public Transaccion generarTransaccion(Double precioDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Criptomoneda criptoEncontrada, Double cantidadDeCripto, Criptomoneda criptoAObtener, Double cantidadDeCriptoAObtener, Double precioDeCriptoAObtener, Boolean esProgramada) {

        Transaccion nuevaTransaccion = new Transaccion();
        nuevaTransaccion.setMontoTotal(precioTotalDeTransaccion);
        nuevaTransaccion.setUsuario(usuario);
        nuevaTransaccion.setTipo(tipoDeTransaccion);
        nuevaTransaccion.setFechaDeTransaccion(LocalDate.now());

        nuevaTransaccion.setCriptomoneda(criptoEncontrada);
        nuevaTransaccion.setPrecioAlQueSehizo(precioDeCripto);
        nuevaTransaccion.setCantidadDeCripto(cantidadDeCripto);

        nuevaTransaccion.setCriptomoneda2(criptoAObtener);
        nuevaTransaccion.setCantidadDeCripto2(cantidadDeCriptoAObtener);
        nuevaTransaccion.setPrecioAlQueSehizo2(precioDeCriptoAObtener);

        nuevaTransaccion.setFueProgramada(esProgramada);

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

    /*------------------- METODO PARA PROGRAMAR TRANSACCION COMPRA ----------------------------------*/
    @Override
    public String programarTransaccion(Criptomoneda criptomonedaEncontrada, Double cantidadDeCriptoProgramada, TipoTransaccion tipoTransaccionProgramada,
                                       Usuario userEncontrado, String condicionProgramada, Double precioACumplir, Criptomoneda criptomonedaAObtener) {
        if (cantidadDeCriptoProgramada <= 0.0) {
            return "La cantidad debe ser mayor que 0.";
        }

        switch (tipoTransaccionProgramada) {
            case COMPRA:
            case VENTA:
            case INTERCAMBIO:
                return intentarProgramarUnaTransaccion(criptomonedaEncontrada, criptomonedaAObtener, cantidadDeCriptoProgramada, tipoTransaccionProgramada, userEncontrado, condicionProgramada, precioACumplir);
            default:
                return "La transaccion no se pudo realizar. Tipo de transaccion desconocida";
        }
    }
    /*------------------------------------------------------------------------------------------------*/

    /*-----------este hace el new y las guarda--------------------------------------------------------*/
    private String intentarProgramarUnaTransaccion(Criptomoneda criptomoneda1, Criptomoneda criptomoneda2, Double cantidadDeCriptoProgramada, TipoTransaccion tipoTransaccionProgramada, Usuario userEncontrado, String condicionProgramada, Double precioACumplir) {
        TransaccionProgramada nuevaTransaccion = new TransaccionProgramada(userEncontrado, criptomoneda1, criptomoneda2, null, null, null, null, tipoTransaccionProgramada, cantidadDeCriptoProgramada, null, LocalDate.now(), condicionProgramada, precioACumplir);
        repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

        //generarYEnviarMail(usuario, nuevaTransaccion);
        return "Programacion exitosa.";
    }
    /*------------------------------------------------------------------------------------------------*/

    @Override
    public List<TransaccionProgramada> filtrarTransaccionesProgramadas(TipoTransaccion tipoTransaccionEncontrada, Long idUsuario) {
        return repositorioTransacciones.filtrarTransaccionesProgramadas(tipoTransaccionEncontrada, idUsuario);
    }

    @Override
    public List<TransaccionProgramada> obtenerHistorialTransaccionesDeUsuarioProgramadas(Long idUsuario) {
        return repositorioTransacciones.obtenerHistorialTransaccionesDeUsuarioProgramadas(idUsuario);
    }

    /*@Override
    public List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario) {
        return repositorioTransacciones.filtrarTransacciones(tipoTransaccion, idUsuario);
    }*/
    @Override
    public List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario, LocalDate desde, LocalDate hasta) {
        //return repositorioTransacciones.filtrarTransacciones(tipoTransaccion, idUsuario);
        List<Transaccion>  filtradaPorTransacciones = repositorioTransacciones.filtrarTransacciones(tipoTransaccion, idUsuario);
        return filtradaPorTransacciones.stream()
                .filter(t -> (desde == null || !t.getFechaDeTransaccion().isBefore(desde)) &&
                        (hasta == null || !t.getFechaDeTransaccion().isAfter(hasta)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaccion> obtenerHistorialTransaccionesDeUsuario(Long idDeUsuario, LocalDate fechaDesde, LocalDate fechaHasta) {
        //return repositorioTransacciones.obtenerHistorialUsuario(idDeUsuario);
        List<Transaccion>  filtradaPorTransacciones = repositorioTransacciones.obtenerHistorialUsuario(idDeUsuario);
        return filtradaPorTransacciones.stream()
                .filter(t -> (fechaDesde == null || !t.getFechaDeTransaccion().isBefore(fechaDesde)) &&
                        (fechaHasta == null || !t.getFechaDeTransaccion().isAfter(fechaHasta)))
                .collect(Collectors.toList());
    }

    @Override
    public Transaccion buscarTransaccionPorId(Long idTransaccion) {
        return repositorioTransacciones.buscarTransaccionPorId(idTransaccion);
    }

    @Override
    public void verSiHayTransaccionesProgramadasAEjecutarse() {
        List<Usuario> usuarios = servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins();

        for (Usuario usuario : usuarios) {
            List<TransaccionProgramada> transaccionesProgramadasDeUnUsuario = repositorioTransacciones.obtenerHistorialTransaccionesDeUsuarioProgramadas(usuario.getId());

            if (!transaccionesProgramadasDeUnUsuario.isEmpty()) {
                this.ejecutarTransaccionesProgramadasDelUsuario(transaccionesProgramadasDeUnUsuario);
            }
        }




    }

    @Override
    public void ejecutarTransaccionesProgramadasDelUsuario(List<TransaccionProgramada> transaccionesProgramadasDeUnUsuario) {
        for (TransaccionProgramada transaccionProgramada : transaccionesProgramadasDeUnUsuario) {
            if (this.verificarQueCumplaLaCondicion(transaccionProgramada)){
                try {
                    if (transaccionProgramada.getTipo().equals(TipoTransaccion.INTERCAMBIO)){
                        this.crearTransaccion(transaccionProgramada.getCriptomoneda(),transaccionProgramada.getCriptomoneda().getPrecioActual(),transaccionProgramada.getCantidadDeCripto(),transaccionProgramada.getTipo(),transaccionProgramada.getUsuario(),transaccionProgramada.getCriptomoneda2(),transaccionProgramada.getCriptomoneda2().getPrecioActual(),true);
                    }else{
                        this.crearTransaccion(transaccionProgramada.getCriptomoneda(),transaccionProgramada.getCriptomoneda().getPrecioActual(),transaccionProgramada.getCantidadDeCripto(),transaccionProgramada.getTipo(),transaccionProgramada.getUsuario(),null,null,true);
                    }

                    // ESTE PONERLO AFUERA DEL CATCH CUANDO HAGA LA NOTIF, LO DEJO ACA PARA QUE SI AHORA NO SE CUMPLE; SIGA APARECiendo- EN LA_ L-i-sta
                    this.eliminarTransaccion(transaccionProgramada);

                }catch (CriptomonedasInsuficientesException | SaldoInsuficienteException e){
                    //o enviar mail o hacer que te envie una notiff si hacemos ese apartado.
                }
            }
        }
    }

    @Override
    public boolean verificarQueCumplaLaCondicion(TransaccionProgramada transaccionProgramada) {
        String condicion = transaccionProgramada.getCondicionParaHacerla();
        Double precioACumplir = transaccionProgramada.getPrecioACumplir();
        Double precioCripto = transaccionProgramada.getCriptomoneda().getPrecioActual();

        switch (condicion){
            case "mayor":
                return precioACumplir < precioCripto;
            case "menor":
                return precioACumplir > precioCripto;
        }
        return true;
    }
}