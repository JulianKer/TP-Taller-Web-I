package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.TipoTransaccion;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

//    @Override
//    public Criptomoneda buscarCriptomonedaPorNombre(String nombreCriptomoneda) {
//        return (Criptomoneda) sessionFactory.getCurrentSession().createCriteria(Criptomoneda.class)
//                .add(Restrictions.eq("nombre",nombreCriptomoneda))
//                .uniqueResult();
//    }

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
}
