package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidades.Transaccion;

import java.util.List;

public interface RepositorioTransacciones {

    void guardarTransaccion(Transaccion transaccion);

    Double buscarCantidadCompradadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);

    Double buscarCantidadVendidadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);

    List<Transaccion> obtenerHistorialUsuario(Long idDeUsuario);
}
