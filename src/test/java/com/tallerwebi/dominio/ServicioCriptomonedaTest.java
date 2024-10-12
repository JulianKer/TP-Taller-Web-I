package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.impl.ServicioCriptomonedaImpl;
import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioCriptomonedaTest {

    private RepositorioCriptomoneda repositorioCriptomoneda = mock(RepositorioCriptomoneda.class);
    private ServicioSubirImagen servicioSubirImagen;
    private ServicioCriptomoneda servicioCriptomoneda = new ServicioCriptomonedaImpl(repositorioCriptomoneda,servicioSubirImagen);

    @Test
    public void queAlBuscarCriptomonedaPorNombreLaEncuentre() {
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre("bitcoin")).thenReturn(criptomoneda);
        assertNotNull(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto));
    }

    @Test
    public void queSeIntenteBuscarCriptomonedaInexistenteYLanceUnaExcepcion() {
        String nombreDeCripto = "bitcoin";

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(null);
        assertThrows(NoSeEncontroLaCriptomonedaException.class,()->servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto));
    }

    @Test
    public void queSeObtengaElMapaConLasCriptosQueTengaEnMiBddATravezDeLaApi() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> criptomonedas = new ArrayList<>();
        criptomonedas.add(criptomoneda);

        Map<Criptomoneda, Double> mapaRecibido = servicioCriptomoneda.obtenerCrypto(criptomonedas, "usd");
        assertEquals(criptomonedas.size(), mapaRecibido.size());
    }

    @Test
    public void queSePuedaObtenerElPrecioDeUnaCriptomonedaEnEspecifico() {
        assertTrue(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre("bitcoin") > 0);
    }

    @Test
    public void queSePuedaAgregarUnaCriptomoneda() {
        assertTrue(servicioCriptomoneda.dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla("avalanche"));
    }

    @Test
    public void queAlBuscarlaEnLaBDDNOExistaEsaCripto() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombreConMayus("Bitcoin");
        criptomoneda.setNombre("bitcoin");
        criptomoneda.setImagen("Bitcoin.png");

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre("criptoX")).thenReturn(null);

        assertTrue(servicioCriptomoneda.verificarQueNoTengaEsaCriptoEnMiBdd("criptoX"));
    }

    @Test
    public void queAlBuscarlaEnLaBDDSIExistaEsaCripto() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombreConMayus("Bitcoin");
        criptomoneda.setNombre("bitcoin");
        criptomoneda.setImagen("Bitcoin.png");

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre("criptoX")).thenReturn(new Criptomoneda());

        assertFalse(servicioCriptomoneda.verificarQueNoTengaEsaCriptoEnMiBdd("criptoX"));
    }
}
