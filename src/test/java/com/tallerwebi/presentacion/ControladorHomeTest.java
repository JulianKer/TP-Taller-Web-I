package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Criptomoneda;
import com.tallerwebi.dominio.ServicioHome;
import com.tallerwebi.dominio.ServicioHomeImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.AssertionErrors;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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

    @Test
    public void queAlSeleccionarLaMonedaEUREnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "EUR");

        ModelAndView mav = new ModelAndView("home", modelo);

        ArrayList<String> misCriptos = new ArrayList<>();
        misCriptos.add("bitcoin");

        Map<String, Double> mapa=new HashMap<>();
        mapa.put("bitcoin", 1.0);

        when(servicioHome.obtenerCrypto(misCriptos, "eur")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("eur", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "EUR");
    }
    @Test
    public void queAlSeleccionarLaMonedaBRLEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "BRL");

        ModelAndView mav = new ModelAndView("home", modelo);

        ArrayList<String> misCriptos = new ArrayList<>();
        misCriptos.add("bitcoin");

        Map<String, Double> mapa=new HashMap<>();
        mapa.put("bitcoin", 1.0);

        when(servicioHome.obtenerCrypto(misCriptos, "brl")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("brl", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "BRL");
    }
    @Test
    public void queAlSeleccionarLaMonedaARSEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "ARS");

        ModelAndView mav = new ModelAndView("home", modelo);

        ArrayList<String> misCriptos = new ArrayList<>();
        misCriptos.add("bitcoin");

        Map<String, Double> mapa=new HashMap<>();
        mapa.put("bitcoin", 1.0);

        when(servicioHome.obtenerCrypto(misCriptos, "ars")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("ars", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "ARS");
    }
    @Test
    public void queAlSeleccionarLaMonedaCNYEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "CNY");

        ModelAndView mav = new ModelAndView("home", modelo);

        ArrayList<String> misCriptos = new ArrayList<>();
        misCriptos.add("bitcoin");

        Map<String, Double> mapa=new HashMap<>();
        mapa.put("bitcoin", 1.0);

        when(servicioHome.obtenerCrypto(misCriptos, "cny")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("cny", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "CNY");
    }
    @Test
    public void queAlSeleccionarLaMonedaUSDEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "USD");

        ModelAndView mav = new ModelAndView("home", modelo);

        ArrayList<String> misCriptos = new ArrayList<>();
        misCriptos.add("bitcoin");

        Map<String, Double> mapa=new HashMap<>();
        mapa.put("bitcoin", 1.0);

        when(servicioHome.obtenerCrypto(misCriptos, "usd")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("usd", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "USD");
    }
    @Test
    public void queAlSeleccionarLaMonedaGBPEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "GBP");

        ModelAndView mav = new ModelAndView("home", modelo);

        ArrayList<String> misCriptos = new ArrayList<>();
        misCriptos.add("bitcoin");

        Map<String, Double> mapa=new HashMap<>();
        mapa.put("bitcoin", 1.0);

        when(servicioHome.obtenerCrypto(misCriptos, "gbp")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("gbp", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "GBP");
    }

}
