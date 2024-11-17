package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioUsuarioTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    RepositorioUsuario repositorioUsuario;



    @Test
    @Transactional
    @Rollback
    public void queSeGuardeUnUsuario() {
        Usuario user = new Usuario();
        user.setEmail("julian@gmial.com");
        user.setPassword("123456");

        repositorioUsuario.guardar(user);

        assertNotNull(user.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaBuscarUnUsuarioPorMailGuardado() {
        Usuario userABuscar = new Usuario();
        userABuscar.setEmail("julian@gmial.com");
        userABuscar.setPassword("123456");
        sessionFactory.getCurrentSession().save(userABuscar);

        // este NO lo uso ya que ya se que el .save() funciona
        // ademas en este test NO quiero validar el metodo .guardar()
        //repositorioUsuario.guardar(user);

        Usuario userEncontrado = repositorioUsuario.buscar("julian@gmial.com");

        assertNotNull(userEncontrado);
        assertEquals(userABuscar.getEmail(), userEncontrado.getEmail());
        assertEquals(userABuscar.getPassword(), userEncontrado.getPassword());
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaBuscarUnUsuarioPorId() {
        Usuario userABuscar = new Usuario();
        userABuscar.setEmail("julian@gmial.com");
        userABuscar.setPassword("123456");
        userABuscar.setId(1L);
        sessionFactory.getCurrentSession().save(userABuscar);

        Usuario userEncontrado = repositorioUsuario.buscarUsuarioPorId(userABuscar.getId());

        assertNotNull(userEncontrado);
    }



    @Test
    @Transactional
    @Rollback
    public void queMeDevuelvaUnaListaDeUsuariosDeRolAdministrador() {
        Usuario user1 = new Usuario();
        user1.setEmail("julian@gmial.com");
        user1.setPassword("123456");
        user1.setRol("CLIENTE");                                    //cliente
        sessionFactory.getCurrentSession().save(user1);

        Usuario user2 = new Usuario();
        user2.setEmail("German@gmial.com");
        user2.setPassword("45896");
        user2.setRol("CLIENTE");                                    //cliente
        sessionFactory.getCurrentSession().save(user2);

        Usuario user3 = new Usuario();
        user3.setEmail("agus@gmial.com");
        user3.setPassword("78855");
        user3.setRol("ADMIN");                                    //admin
        sessionFactory.getCurrentSession().save(user3);


        List<Usuario> listaEsperada = new ArrayList<>();
        listaEsperada.add(user3);


        String rolABuscar = "ADMIN";
        List<Usuario> listaObtenida = repositorioUsuario.buscarPorRol(rolABuscar);

        assertTrue(listaObtenida.containsAll(listaEsperada) && listaEsperada.containsAll(listaObtenida));
    }


    @Test
    @Transactional
    @Rollback
    public void queSePuedaRestarSaldoDelUser(){
        Usuario user = new Usuario();
        user.setEmail("julian@gmial.com");
        user.setSaldo(5000.0);
        sessionFactory.getCurrentSession().save(user);

        repositorioUsuario.restarSaldo(user.getId(), 2000.0);
        assertEquals(repositorioUsuario.buscarUsuarioPorId(user.getId()).getSaldo(), 3000.0);
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaSumarSaldoDelUser(){
        Usuario user = new Usuario();
        user.setEmail("julian@gmial.com");
        user.setSaldo(5000.0);
        sessionFactory.getCurrentSession().save(user);

        repositorioUsuario.sumarSaldo(user.getId(), 2000.0);
        assertEquals(repositorioUsuario.buscarUsuarioPorId(user.getId()).getSaldo(), 7000.0);
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaDarSuscripcionAlUser(){
        Usuario user = new Usuario();
        user.setEmail("julian@gmial.com");
        user.setActivo(false);
        sessionFactory.getCurrentSession().save(user);

        repositorioUsuario.cambiarEstado(user.getId(), true);
        assertEquals(repositorioUsuario.buscarUsuarioPorId(user.getId()).getActivo(), true);
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaSacarSuscripcionAlUser(){
        Usuario user = new Usuario();
        user.setEmail("julian@gmial.com");
        user.setActivo(true);
        sessionFactory.getCurrentSession().save(user);

        repositorioUsuario.cambiarEstado(user.getId(), false);
        assertEquals(repositorioUsuario.buscarUsuarioPorId(user.getId()).getActivo(), false);
    }

    @Test
    @Transactional
    @Rollback
    public void queMeDevuelvaUnaListaDeUsuariosClientes() {
        Usuario user1 = new Usuario();
        user1.setEmail("julian@gmial.com");
        user1.setPassword("123456");
        user1.setRol("CLIENTE");
        sessionFactory.getCurrentSession().save(user1);

        Usuario user2 = new Usuario();
        user2.setEmail("German@gmial.com");
        user2.setPassword("45896");
        user2.setRol("CLIENTE");
        sessionFactory.getCurrentSession().save(user2);

        Usuario user3 = new Usuario();
        user3.setEmail("agus@gmial.com");
        user3.setPassword("78855");
        user3.setRol("ADMIN");
        sessionFactory.getCurrentSession().save(user3);


        List<Usuario> listaEsperada = new ArrayList<>();
        listaEsperada.add(user1);
        listaEsperada.add(user2);

        List<Usuario> listaObtenida = repositorioUsuario.obtenerUnaListaDeTodosLosUsuariosClientes();
        assertTrue(listaObtenida.containsAll(listaEsperada) && listaEsperada.containsAll(listaObtenida));
    }
}
