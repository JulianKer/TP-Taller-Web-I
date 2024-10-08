package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Criptomoneda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ServicioCriptomoneda {

    Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto);

    ArrayList<Criptomoneda> obtenerNombreDeTodasLasCriptos();

    Double obtenerPrecioDeCriptoPorNombre(String nombreDeCripto);

    Map<Criptomoneda, Double> obtenerCrypto(ArrayList<Criptomoneda> misCriptos, String moneda);
}
