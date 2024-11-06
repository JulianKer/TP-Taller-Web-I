package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.TransaccionProgramada;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import java.time.LocalDate;

import java.util.List;


public interface ServicioTransacciones {

    List<Transaccion> obtenerHistorialTransaccionesDeUsuario(Long idDeUsuario, LocalDate fechaDesde, LocalDate fechaHasta);

    //----- METODO PRINCIPAL PARA GENERAR LA TRANSACCION -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    String crearTransaccion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Criptomoneda criptoAObtener, Double precioDeCriptoAObtener, Boolean esProgramada);

    Transaccion generarTransaccion(Double precioDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Criptomoneda criptoEncontrada, Double cantidadDeCripto, Criptomoneda criptoAObtener, Double cantidadDeCriptoAObtener, Double precioDeCriptoAObtener, Boolean esProgramada);

    Boolean verificarQueTengaSaldoSuficienteParaComprar(Double precioTotalDeTransaccion, Double saldo);

    List<Transaccion> obtenerTransaccionesDeEstaCripto(String idCriptomoneda);

    void eliminarTransaccion(Transaccion transaccion);

    // List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario);
    List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario, LocalDate desde, LocalDate hasta);

    /*------------------- METODO PARA PROGRAMAR TRANSACCION COMPRA ----------------------------------*/
    String programarTransaccion(Criptomoneda criptomonedaEncontrada, Double cantidadDeCriptoProgramada, TipoTransaccion tipoTransaccionProgramada,
                                Usuario userEncontrado, String condicionProgramada, Double precioACumplir, Criptomoneda criptomonedaAObtener);

    List<TransaccionProgramada> filtrarTransaccionesProgramadas(TipoTransaccion tipoTransaccionEncontrada, Long idUsuario);

    List<TransaccionProgramada> obtenerHistorialTransaccionesDeUsuarioProgramadas(Long idUsuario);

    Transaccion buscarTransaccionPorId(Long idTransaccion);

    void verSiHayTransaccionesProgramadasAEjecutarse();

    void ejecutarTransaccionesProgramadasDelUsuario(List<TransaccionProgramada> transaccionesProgramadasDeUnUsuario);

    boolean verificarQueCumplaLaCondicion(TransaccionProgramada transaccionProgramada);
}
