package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.entidades.PrecioCripto;
import com.tallerwebi.dominio.repositorio.RepositorioPrecioCripto;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioPrecioCriptoImpl implements RepositorioPrecioCripto {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPrecioCriptoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarPrecioCripto(PrecioCripto precioCripto) {
        sessionFactory.getCurrentSession().save(precioCripto);
    }

    @Override
    public List<PrecioCripto> obtenerHistorialDePrecioCriptoDeEstaCripto(String nombreCripto) {
        return (List<PrecioCripto>) sessionFactory.getCurrentSession().createCriteria(PrecioCripto.class)
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("c.nombre", nombreCripto))
                .list();
    }
}
