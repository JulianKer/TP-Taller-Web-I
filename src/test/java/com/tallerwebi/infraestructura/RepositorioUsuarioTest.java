package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
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


}
