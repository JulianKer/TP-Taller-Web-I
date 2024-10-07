package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidades.Transaccion;

public interface RepositorioTransacciones {

    void guardarTransaccion(Transaccion transaccion);

    Double buscarCantidadCompradadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);

    Double buscarCantidadVendidadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);
}
