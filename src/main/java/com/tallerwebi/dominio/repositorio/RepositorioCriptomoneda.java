package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidades.Criptomoneda;

import java.util.List;

public interface RepositorioCriptomoneda {


    Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto);

    void guardarCriptomoneda(Criptomoneda criptomoneda);

    List<String> dameElNombreDeTodasLasCriptos();
}
