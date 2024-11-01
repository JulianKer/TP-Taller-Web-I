package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.TransaccionProgramada;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.repositorio.RepositorioTransacciones;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
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
    public List<Transaccion> obtenerHistorialUsuario(Long idDeUsuario) {
        return (List<Transaccion>) sessionFactory.getCurrentSession().createCriteria(Transaccion.class)
                .createAlias("usuario", "u")
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("u.id", idDeUsuario))
                .add(Restrictions.eq("class", Transaccion.class))
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
                .add(Restrictions.eq("class", Transaccion.class))
                .setFetchMode("criptomoneda", FetchMode.JOIN)
                .list();
    }

    @Override
    public List<TransaccionProgramada> filtrarTransaccionesProgramadas(TipoTransaccion tipoTransaccion, Long idUsuario) {
        return (ArrayList<TransaccionProgramada>) sessionFactory.getCurrentSession().createCriteria(TransaccionProgramada.class)
                .createAlias("usuario", "u")
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("tipo", tipoTransaccion))
                .add(Restrictions.eq("u.id", idUsuario))
                .add(Restrictions.eq("class", Transaccion.class))
                .setFetchMode("criptomoneda", FetchMode.JOIN)
                .list();
    }

    @Override
    public List<TransaccionProgramada> obtenerHistorialTransaccionesDeUsuarioProgramadas(Long idUsuario) {
        return (List<TransaccionProgramada>) sessionFactory.getCurrentSession().createCriteria(TransaccionProgramada.class)
                .createAlias("usuario", "u")
                .createAlias("criptomoneda", "c")
                .add(Restrictions.eq("u.id", idUsuario))
                .add(Restrictions.eq("class", TransaccionProgramada.class))
                .setFetchMode("criptomoneda", FetchMode.JOIN)
                .list();
    }
}
