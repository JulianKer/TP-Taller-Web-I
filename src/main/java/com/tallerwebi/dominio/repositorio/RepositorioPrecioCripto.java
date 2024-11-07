package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidades.PrecioCripto;

import java.util.List;

public interface RepositorioPrecioCripto {
    void guardarPrecioCripto(PrecioCripto precioCripto);

    List<PrecioCripto> obtenerHistorialDePrecioCriptoDeEstaCripto(String nombreCripto);
}
