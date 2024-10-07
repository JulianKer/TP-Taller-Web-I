package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCriptomoneda {


    Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto);

    void guardarCriptomoneda(Criptomoneda criptomoneda);

    List<String> dameElNombreDeTodasLasCriptos();
}
