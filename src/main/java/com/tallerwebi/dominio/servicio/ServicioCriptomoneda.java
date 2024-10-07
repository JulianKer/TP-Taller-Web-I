package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Criptomoneda;

import java.util.List;

public interface ServicioCriptomoneda {

    Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto);

    List<String> obtenerNombreDeTodasLasCriptos();

    Double obtenerPrecioDeCriptoPorNombre(String nombreDeCripto);
}
