package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.enums.TipoTransaccion;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioTransacciones {

    void guardarTransaccion(Transaccion transaccion);

    List<Transaccion> obtenerHistorialUsuario(Long idDeUsuario);

    List<Transaccion> obtenerTransaccionesDeEstaCripto(String idCriptomoneda);

    void eliminarTransaccion(Transaccion transaccion);

    List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario);
}
