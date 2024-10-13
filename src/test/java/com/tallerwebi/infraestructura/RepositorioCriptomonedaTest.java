package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.repositorio.impl.RepositorioCriptomonedaImpl;
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
public class RepositorioCriptomonedaTest {

    @Autowired
    private RepositorioCriptomonedaImpl repositorioCriptomoneda;

    @Test
    @Transactional
    @Rollback
    public void queSePuedaGuardarUnaCriptomoneda() {
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda);

        assertNotNull(criptomoneda.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void queAlBuscarCriptomonedaPorNombreLaEncuentre() {
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda);

        assertNotNull(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto));
    }

    @Test
    @Transactional
    @Rollback
    public void queMeDevuelvaElNombreDeTodasLasCriptosEnUnArray() {
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);
        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda);

        ArrayList<Criptomoneda> arrayRecibido = repositorioCriptomoneda.dameElNombreDeTodasLasCriptos();

        assertFalse(arrayRecibido.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaActualizarCriptomoneda() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");
        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda);

        criptomoneda.setNombre("ethereum");
        repositorioCriptomoneda.actualizarCriptomoneda(criptomoneda);

        Criptomoneda criptoEncontrada = repositorioCriptomoneda.buscarCriptomonedaPorNombre("ethereum");
        assertEquals("ethereum", criptoEncontrada.getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaObtenerLasCriptosHabilitadas() {
        Criptomoneda criptomoneda1 = new Criptomoneda();
        criptomoneda1.setId(1L);
        criptomoneda1.setNombre("bitcoin");
        criptomoneda1.setHabilitada(true);
        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda1);

        Criptomoneda criptomoneda2 = new Criptomoneda();
        criptomoneda2.setId(2L);
        criptomoneda2.setNombre("ethereum");
        criptomoneda2.setHabilitada(false);
        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda2);

        Criptomoneda criptomoneda3 = new Criptomoneda();
        criptomoneda3.setId(4L);
        criptomoneda3.setNombre("dogecoin");
        criptomoneda3.setHabilitada(true);
        repositorioCriptomoneda.guardarCriptomoneda(criptomoneda3);

        List<Criptomoneda> criptomonedasHabilitadas = new ArrayList<>();
        criptomonedasHabilitadas.add(criptomoneda1);
        criptomonedasHabilitadas.add(criptomoneda3);

        ArrayList<Criptomoneda> criptosRecibidas = repositorioCriptomoneda.obtenerCriptosHabilitadas();

        assertEquals(criptosRecibidas, criptomonedasHabilitadas);
    }
}
