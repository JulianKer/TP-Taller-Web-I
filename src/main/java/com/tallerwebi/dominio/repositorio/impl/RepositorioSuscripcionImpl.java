package com.tallerwebi.dominio.repositorio.impl;


import com.tallerwebi.dominio.entidades.Suscripcion;
import com.tallerwebi.dominio.repositorio.RepositorioSuscripcion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class RepositorioSuscripcionImpl implements RepositorioSuscripcion {



    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioSuscripcionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Suscripcion> obtenerSuscripciones() {
        return (List<Suscripcion>) sessionFactory.getCurrentSession().createCriteria(Suscripcion.class)
                .list();
    }
}
