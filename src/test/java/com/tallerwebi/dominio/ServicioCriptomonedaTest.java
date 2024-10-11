package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.impl.ServicioCriptomonedaImpl;
import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
