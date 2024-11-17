package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.entidades.TransaccionProgramada;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.impl.RepositorioTransaccionesImpl;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.repositorio.impl.RepositorioUsuarioImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioTransaccionesTest {

    @Autowired
    private RepositorioTransaccionesImpl repositorioTransacciones;

    @Test
    @Transactional
    @Rollback
    public void queSeGuardeUnaTransaccion() {
        Transaccion nuevaTransaccion = new Transaccion();
        repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

        assertNotNull(nuevaTransaccion.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void queSeEncuentreUnaTransaccionPorId() {
        Transaccion nuevaTransaccion = new Transaccion();
        nuevaTransaccion.setId(1L);
        repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

        assertNotNull(repositorioTransacciones.buscarTransaccionPorId(nuevaTransaccion.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void queNoSeEncuentreUnaTransaccionPorId() {
        Transaccion nuevaTransaccion = new Transaccion();
        nuevaTransaccion.setId(1L);
        repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

        assertNull(repositorioTransacciones.buscarTransaccionPorId(2L));
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaeliminarunaTransaccion() {
        Transaccion nuevaTransaccion = new Transaccion();
        nuevaTransaccion.setId(1L);
        repositorioTransacciones.guardarTransaccion(nuevaTransaccion);
        repositorioTransacciones.eliminarTransaccion(nuevaTransaccion);

        assertNull(repositorioTransacciones.buscarTransaccionPorId(nuevaTransaccion.getId()));
    }
}
