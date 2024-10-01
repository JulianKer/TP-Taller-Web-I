package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Criptomoneda;
import com.tallerwebi.dominio.ServicioHome;
import com.tallerwebi.dominio.ServicioHomeImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorHomeTest {

    private ServicioHome servicioHome = mock(ServicioHomeImpl.class);
    private ControladorHome controladorHome = new ControladorHome(servicioHome);

    @Test
    public void queAlEntrarAlHomeMeDevuelvaUnMapConMonedaPrecioDeCriptos() {
/*
        when(servicioHome.obtenerCrypto("ethereum", "usd")).thenReturn(null);

        ModelAndView mav = controladorHome.cargarPrecioDeCryptos("usd");

        Map<String, Double> mapaMonedaPrecios = (Map<String, Double>) mav.getModel().get("mapaMonedaPrecios");

        assertFalse(mapaMonedaPrecios.isEmpty());*/
    }

    @Test
    public void queAlEntrarAlHomeMeArrojeUnaExcepcionPorLaPeticionALaApi() {

    }
}
