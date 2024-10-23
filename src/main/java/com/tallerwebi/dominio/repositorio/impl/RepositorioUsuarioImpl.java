package com.tallerwebi.dominio.repositorio.impl;

import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidades.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {

        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public List<Usuario> buscarPorRol(String rol) {
        return (List<Usuario>) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("rol", rol)).list();
    }

    @Override
    public void restarSaldo(Long idUsuario, Double precioTotalDeTransaccion) {
        Usuario user = (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("id", idUsuario))
                .uniqueResult();

        user.setSaldo(user.getSaldo() - precioTotalDeTransaccion);
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public void cambiarEstado(Long idUsuario, boolean estado) {
        Usuario user = (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("id", idUsuario))
                .uniqueResult();

        user.setActivo(estado);
        sessionFactory.getCurrentSession().update(user);

    }

    @Override
    public void sumarSaldo(Long idUsuario, Double precioTotalDeTransaccion) {
        Usuario user = (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("id", idUsuario))
                .uniqueResult();

        user.setSaldo(user.getSaldo() + precioTotalDeTransaccion);
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public ArrayList<Usuario> obtenerUnaListaDeTodosLosUsuariosClientes() {
        return (ArrayList<Usuario>) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("rol", "CLIENTE"))
                .list();
    }

    @Override
    public Usuario buscarUsuarioPorId(Long idUsuario) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("id", idUsuario))
                .uniqueResult();
    }


}
