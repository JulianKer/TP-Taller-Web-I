package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;

import java.util.ArrayList;
import java.util.List;


public interface ServicioTransacciones {

    String crearTransaccion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Criptomoneda criptoAObtener, Double precioDeCriptoAObtener);

    List<Transaccion> obtenerHistorialTransaccionesDeUsuario(Long idDeUsuario);

    Transaccion generarTransaccion(Double precioDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Criptomoneda criptoEncontrada, Double cantidadDeCripto, Criptomoneda criptoAObtener, Double cantidadDeCriptoAObtener, Double precioDeCriptoAObtener);

    Boolean verificarQueTengaSaldoSuficienteParaComprar(Double precioTotalDeTransaccion, Double saldo);

    List<Transaccion> obtenerTransaccionesDeEstaCripto(String idCriptomoneda);

    void eliminarTransaccion(Transaccion transaccion);

    List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario);
}
