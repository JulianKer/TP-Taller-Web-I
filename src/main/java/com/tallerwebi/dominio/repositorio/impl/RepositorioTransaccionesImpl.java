package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.repositorio.RepositorioTransacciones;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioTransacciones") //lo tengo qe poner eso??
public class RepositorioTransaccionesImpl implements RepositorioTransacciones {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioTransaccionesImpl( SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarTransaccion(Transaccion transaccion) {
        //tengo que hacer la conexion con la dbdd para guardarlo.
        sessionFactory.getCurrentSession().save(transaccion);
    }

    @Override
    public Double buscarCantidadCompradadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario) {
        return (Double) sessionFactory.getCurrentSession().createCriteria(Transaccion.class)
                .createAlias("usuario", "u")
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("u.id", idDeUsuario))
                .add(Restrictions.eq("c.nombre", nombreDeCripto))
                .add(Restrictions.eq("tipo", TipoTransaccion.COMPRA))
                .setProjection(Projections.sum("cantidadDeCripto"))
                .uniqueResult();
    }

    @Override
    public Double buscarCantidadVendidadeUnaCriptoDeUnUsuario(String nombreDeCripto, Long idDeUsuario) {
        return (Double) sessionFactory.getCurrentSession().createCriteria(Transaccion.class)
                .createAlias("usuario", "u")
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("u.id", idDeUsuario))
                .add(Restrictions.eq("c.nombre", nombreDeCripto))
                .add(Restrictions.eq("tipo", TipoTransaccion.VENTA))
                .setProjection(Projections.sum("cantidadDeCripto"))
                .uniqueResult();
    }

    @Override
    public List<Transaccion> obtenerHistorialUsuario(Long idDeUsuario) {
        return (List<Transaccion>) sessionFactory.getCurrentSession().createCriteria(Transaccion.class)
                .createAlias("usuario", "u")
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("u.id", idDeUsuario))
                .setFetchMode("criptomoneda", FetchMode.JOIN)
                .list();
    }

    @Override
    public List<Transaccion> obtenerTransaccionesDeEstaCripto(String idCriptomoneda) {
        return  (List<Transaccion>) sessionFactory.getCurrentSession().createCriteria(Transaccion.class)
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("c.nombre", idCriptomoneda))
                .list();
    }

    @Override
    public void eliminarTransaccion(Transaccion transaccion) {
        sessionFactory.getCurrentSession().delete(transaccion);
    }

    @Override
    public List<Transaccion> filtrarTransacciones(TipoTransaccion tipoTransaccion, Long idUsuario) {

        return(ArrayList<Transaccion>) sessionFactory.getCurrentSession().createCriteria(Transaccion.class)
                .createAlias("usuario", "u")
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("tipo", tipoTransaccion))
                .add(Restrictions.eq("u.id", idUsuario))
                .setFetchMode("criptomoneda", FetchMode.JOIN)
                .list();


    }
}
