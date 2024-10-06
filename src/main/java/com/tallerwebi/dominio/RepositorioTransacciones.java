package com.tallerwebi.dominio;

public interface RepositorioTransacciones {

    void guardarTransaccion(Transaccion transaccion);
//    Criptomoneda buscarCriptomonedaPorNombre(String nombreCriptomoneda);


    Double buscarCantidadCompradadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);

    Double buscarCantidadVendidadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario);
}
