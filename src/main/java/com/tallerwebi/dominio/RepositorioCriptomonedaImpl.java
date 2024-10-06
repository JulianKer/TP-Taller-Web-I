package com.tallerwebi.dominio;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
