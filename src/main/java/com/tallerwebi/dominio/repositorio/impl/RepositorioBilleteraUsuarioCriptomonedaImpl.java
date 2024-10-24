package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioBilleteraUsuarioCriptomoneda;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioBilleteraUsuarioCriptomonedaImpl implements RepositorioBilleteraUsuarioCriptomoneda {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioBilleteraUsuarioCriptomonedaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarBilletera(BilleteraUsuarioCriptomoneda billeteraNueva) {
        sessionFactory.getCurrentSession().save(billeteraNueva);
    }

    @Override
    public void actualizarBilletera(BilleteraUsuarioCriptomoneda billetera) {
        sessionFactory.getCurrentSession().update(billetera);
    }

    @Override
    public BilleteraUsuarioCriptomoneda buscarBilleteraCriptoUsuario(Criptomoneda criptomoneda, Usuario usuario) {
        return (BilleteraUsuarioCriptomoneda) sessionFactory.getCurrentSession().createCriteria(BilleteraUsuarioCriptomoneda.class)
                .createAlias("criptomoneda", "c")
                .createAlias("usuario", "u")
                .add(Restrictions.eq("u.id", usuario.getId()))
                .add(Restrictions.eq("c.id", criptomoneda.getId()))
                .uniqueResult();
    }
}
