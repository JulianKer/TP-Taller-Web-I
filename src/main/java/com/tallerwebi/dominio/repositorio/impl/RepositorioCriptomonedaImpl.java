package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

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

    @Override
    public Boolean eliminarCriptomoneda(Criptomoneda criptomonedaAEliminar) {
        sessionFactory.getCurrentSession().delete(criptomonedaAEliminar);
        return buscarCriptomonedaPorNombre(criptomonedaAEliminar.getNombre()) == null;
    }

    @Override
    public Boolean inhabilitarCriptomoneda(Criptomoneda criptoAInhabilitar) {
        sessionFactory.getCurrentSession().update(criptoAInhabilitar);
        return buscarCriptomonedaPorNombre(criptoAInhabilitar.getNombre()).getHabilitada();
    }

    @Override
    public Boolean habilitarCriptomoneda(Criptomoneda criptoAHabilitar) {
        sessionFactory.getCurrentSession().update(criptoAHabilitar);
        return buscarCriptomonedaPorNombre(criptoAHabilitar.getNombre()).getHabilitada();
    }
}
