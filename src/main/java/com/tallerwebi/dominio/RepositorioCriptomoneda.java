package com.tallerwebi.dominio;

public interface RepositorioCriptomoneda {


    Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto);

    void guardarCriptomoneda(Criptomoneda criptomoneda);
}
