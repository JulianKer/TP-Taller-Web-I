package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioCriptomoneda {

    Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto);

    List<String> obtenerNombreDeTodasLasCriptos();

    Double obtenerPrecioDeCriptoPorNombre(String nombreDeCripto);
}
