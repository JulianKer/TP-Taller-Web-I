package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Suscripcion;
import com.tallerwebi.dominio.repositorio.RepositorioSuscripcion;
import com.tallerwebi.dominio.repositorio.impl.RepositorioCriptomonedaImpl;
import com.tallerwebi.dominio.repositorio.impl.RepositorioSuscripcionImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioSuscripcionTest {

    @Autowired
    private RepositorioSuscripcionImpl repositorioSuscripcion;

    @Autowired
    SessionFactory sessionFactory;

    @Test
    @Transactional
    @Rollback
    public void queSePuedanObtenerSuscripciones() {
        List<Suscripcion> suscripciones = new ArrayList<>();

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setId(1L);
        sessionFactory.getCurrentSession().save(suscripcion);
        Suscripcion suscripcion2 = new Suscripcion();
        suscripcion2.setId(2L);
        sessionFactory.getCurrentSession().save(suscripcion2);
        Suscripcion suscripcion3 = new Suscripcion();
        suscripcion3.setId(3L);
        sessionFactory.getCurrentSession().save(suscripcion3);

        suscripciones.add(suscripcion);
        suscripciones.add(suscripcion2);
        suscripciones.add(suscripcion3);

        assertEquals(suscripciones, repositorioSuscripcion.obtenerSuscripciones());
    }
}
