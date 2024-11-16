package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.repositorio.RepositorioNotificaciones;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

@Repository
public class RepositorioNotificacionesImpl implements RepositorioNotificaciones {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioNotificacionesImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarNotificacion(Notificacion notificacionNueva) {
        sessionFactory.getCurrentSession().save(notificacionNueva);
    }

    /*@Override
    public List<Notificacion> obtenerLasNotificacionesDelUsuario(Long id) {
        return (List<Notificacion>) sessionFactory.getCurrentSession().createCriteria(Notificacion.class)
                .createAlias("usuario", "u")
                .add(Restrictions.eq("u.id", id))
                .list();
    }*/
    @Override
    public List<Notificacion> obtenerLasNotificacionesDelUsuario(Long id) {
        return (List<Notificacion>) sessionFactory.getCurrentSession().createCriteria(Notificacion.class)
                .createAlias("usuario", "u")
                .add(Restrictions.eq("u.id", id))
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public List<Notificacion> obtenerLasNotificacionesDelUsuarioOrdenadasDeAntiguoAReciente(Long id) {
        return (List<Notificacion>) sessionFactory.getCurrentSession().createCriteria(Notificacion.class)
                .createAlias("usuario", "u")
                .add(Restrictions.eq("u.id", id))
                .addOrder(Order.asc("id"))
                .list();    }

    @Override
    public void actualizarNotificacion(Notificacion notificacionAActualizar) {
        sessionFactory.getCurrentSession().update(notificacionAActualizar);
    }

    @Override
    public Boolean consultarSiHayNotificacionesSinVerParaEsteUsuario(Long id) {
        Long count = (Long) sessionFactory.getCurrentSession().createCriteria(Notificacion.class)
                .createAlias("usuario", "u")
                .add(Restrictions.eq("u.id", id)) // Filtra por el ID del usuario
                .add(Restrictions.eq("vista", false)) // Filtra por las notificaciones no vistas
                .setProjection(Projections.rowCount()) // Cuenta el número de resultados
                .uniqueResult();
        return count != null && count > 0;
    }
}
