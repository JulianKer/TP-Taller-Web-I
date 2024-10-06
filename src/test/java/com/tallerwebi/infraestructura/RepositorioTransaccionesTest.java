package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.Criptomoneda;
import com.tallerwebi.dominio.RepositorioTransaccionesImpl;
import com.tallerwebi.dominio.Transaccion;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void buscarCriptomonedaPorNombre() {
        Criptomoneda nuevaCriptomoneda = new Criptomoneda();
        nuevaCriptomoneda.setNombre("ethereum");



        Transaccion nuevaTransaccion = new Transaccion();
        repositorioTransacciones.guardarTransaccion(nuevaTransaccion);

        assertNotNull(nuevaTransaccion.getId());
    }
}
