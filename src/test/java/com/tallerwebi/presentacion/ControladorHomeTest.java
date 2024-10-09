package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioHome;
import com.tallerwebi.dominio.servicio.impl.ServicioHomeImpl;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorHomeTest {

    private ServicioCriptomoneda servicioCriptomoneda = mock(ServicioCriptomoneda.class);
    private ControladorHome controladorHome = new ControladorHome(servicioCriptomoneda);

    // este lo deberia usar para "pasar una session" al home y qcy, mostrar el nombre del user en el nav segun el email logueado
    // private MockHttpServletRequest request = new MockHttpServletRequest();
    //request.getSession().setAttribute("ROL", "ADMIN");

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

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();
        misCriptos.add(criptomoneda);

        Map<Criptomoneda, Double> mapa = new HashMap<Criptomoneda, Double>();
        mapa.put(criptomoneda, 1.0);

        when(servicioCriptomoneda.obtenerCrypto(misCriptos, "eur")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("eur", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "EUR");
    }
    @Test
    public void queAlSeleccionarLaMonedaBRLEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "BRL");

        ModelAndView mav = new ModelAndView("home", modelo);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();
        misCriptos.add(criptomoneda);

        Map<Criptomoneda, Double> mapa = new HashMap<Criptomoneda, Double>();
        mapa.put(criptomoneda, 1.0);

        when(servicioCriptomoneda.obtenerCrypto( misCriptos, "brl")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("brl", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "BRL");
    }
    @Test
    public void queAlSeleccionarLaMonedaARSEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "ARS");

        ModelAndView mav = new ModelAndView("home", modelo);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();
        misCriptos.add(criptomoneda);

        Map<Criptomoneda, Double> mapa = new HashMap<Criptomoneda, Double>();
        mapa.put(criptomoneda, 1.0);

        when(servicioCriptomoneda.obtenerCrypto(misCriptos, "ars")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("ars", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "ARS");
    }
    @Test
    public void queAlSeleccionarLaMonedaCNYEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "CNY");

        ModelAndView mav = new ModelAndView("home", modelo);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();
        misCriptos.add(criptomoneda);

        Map<Criptomoneda, Double> mapa = new HashMap<Criptomoneda, Double>();
        mapa.put(criptomoneda, 1.0);

        when(servicioCriptomoneda.obtenerCrypto(misCriptos, "cny")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("cny", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "CNY");
    }
    @Test
    public void queAlSeleccionarLaMonedaUSDEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "USD");

        ModelAndView mav = new ModelAndView("home", modelo);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();
        misCriptos.add(criptomoneda);

        Map<Criptomoneda, Double> mapa = new HashMap<Criptomoneda, Double>();
        mapa.put(criptomoneda, 1.0);

        when(servicioCriptomoneda.obtenerCrypto(misCriptos, "usd")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("usd", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "USD");
    }
    @Test
    public void queAlSeleccionarLaMonedaGBPEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("divisaAMostrar", "GBP");

        ModelAndView mav = new ModelAndView("home", modelo);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();
        misCriptos.add(criptomoneda);

        Map<Criptomoneda, Double> mapa = new HashMap<Criptomoneda, Double>();
        mapa.put(criptomoneda, 1.0);

        when(servicioCriptomoneda.obtenerCrypto(misCriptos, "gbp")).thenReturn(mapa);
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("gbp", "bitcoin");

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "GBP");
    }

}
