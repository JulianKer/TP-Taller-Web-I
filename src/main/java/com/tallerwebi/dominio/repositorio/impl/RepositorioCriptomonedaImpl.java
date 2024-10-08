package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioCriptomonedaImpl implements RepositorioCriptomoneda {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioCriptomonedaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto) {
        return (Criptomoneda) sessionFactory.getCurrentSession().createCriteria(Criptomoneda.class)
                .add(Restrictions.eq("nombre",nombreDeCripto))
                .uniqueResult();
    }

    @Override
    public void guardarCriptomoneda(Criptomoneda criptomoneda) {
        sessionFactory.getCurrentSession().save(criptomoneda);
    }

    @Override
    public ArrayList<Criptomoneda> dameElNombreDeTodasLasCriptos() {
        return (ArrayList<Criptomoneda>) sessionFactory.getCurrentSession().createCriteria(Criptomoneda.class)
                .list();
    }

    @Override
    public void actualizarCriptomoneda(Criptomoneda criptoDeMiBdd) {
        sessionFactory.getCurrentSession().update(criptoDeMiBdd);
    }
}
