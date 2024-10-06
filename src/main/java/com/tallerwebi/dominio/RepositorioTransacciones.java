package com.tallerwebi.dominio;

public interface RepositorioTransacciones {

    void guardarTransaccion(Transaccion transaccion);

    Double buscarCantidadCompradadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);

    Double buscarCantidadVendidadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);
}
