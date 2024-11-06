package com.tallerwebi.dominio.repositorio;


import com.tallerwebi.dominio.entidades.Suscripcion;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioSuscripcion {


    List<Suscripcion> obtenerSuscripciones();
}
