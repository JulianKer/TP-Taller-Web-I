package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioNotificaciones;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
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

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private ServicioCriptomoneda servicioCriptomoneda = mock(ServicioCriptomoneda.class);
    private ServicioUsuario servicioUsuario = mock(ServicioUsuario.class);
    private ServicioTransacciones servicioTransacciones = mock(ServicioTransacciones.class);
    private ServicioNotificaciones servicioNotificaciones = mock(ServicioNotificaciones.class);
    private ControladorHome controladorHome = new ControladorHome(servicioCriptomoneda, servicioUsuario, servicioTransacciones, servicioNotificaciones);

    // este lo deberia usar para "pasar una session" al home y qcy, mostrar el nombre del user en el nav segun el email logueado
    // private MockHttpServletRequest request = new MockHttpServletRequest();
    //request.getSession().setAttribute("ROL", "ADMIN");

    @Test
    public void queAlEntrarAlHomeElMapaQueMeDevuelvaEsteVacio() {
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("julian@gmail.com")).thenReturn(user);

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();

        // en este le digo q no me devuelva un mapa sino q me devuelva null entonces es como si no hubiese recibido nada
        when(servicioCriptomoneda.obtenerCrypto(misCriptos, "usd")).thenReturn(null);
        ModelAndView mav = controladorHome.cargarPrecioDeCryptos("usd", "", request);

        Map<Criptomoneda, Double> mapaMonedaPreciosRecibido = (Map<Criptomoneda, Double>) mav.getModel().get("mapaMonedaPrecios");

        assertTrue(mapaMonedaPreciosRecibido.isEmpty());
    }

    @Test
    public void queAlEntrarAlHomeMeDevuelvaUnMapConMonedaPrecioDeCriptos() {
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("julian@gmail.com")).thenReturn(user);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> misCriptos = new ArrayList<>();
        misCriptos.add(criptomoneda);

        Map<Criptomoneda, Double> mapaQueDeberiaRecibir = new HashMap<>();
        mapaQueDeberiaRecibir.put(criptomoneda, 61000.0);

        // en este le digo q no me devuelva un mapa sino q me devuelva null entonces es como si no hubiese recibido nada
        //when(servicioCriptomoneda.obtenerNombreDeTodasLasCriptos()).thenReturn(misCriptos);
        when(servicioCriptomoneda.obtenerCriptosHabilitadas()).thenReturn(misCriptos);
        when(servicioCriptomoneda.obtenerCrypto(misCriptos, "")).thenReturn(mapaQueDeberiaRecibir);

        ModelAndView mav = controladorHome.cargarPrecioDeCryptos("", "", request);

        Map<Criptomoneda, Double> mapaMonedaPreciosRecibido = (Map<Criptomoneda, Double>) mav.getModel().get("mapaMonedaPrecios");

        assertFalse(mapaMonedaPreciosRecibido.isEmpty());
        assertEquals(mapaMonedaPreciosRecibido.get(criptomoneda), 61000.0);
    }

    @Test
    public void queAlSeleccionarLaMonedaEUREnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("german@gmail.com")).thenReturn(user);

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
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("eur", "bitcoin", request);

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "EUR");
    }

    @Test
    public void queAlSeleccionarLaMonedaBRLEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("german@gmail.com")).thenReturn(user);

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
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("brl", "bitcoin", request);

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "BRL");
    }
    @Test
    public void queAlSeleccionarLaMonedaARSEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("german@gmail.com")).thenReturn(user);

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
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("ars", "bitcoin", request);

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "ARS");
    }
    @Test
    public void queAlSeleccionarLaMonedaCNYEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("german@gmail.com")).thenReturn(user);

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
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("cny", "bitcoin", request);

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "CNY");
    }
    @Test
    public void queAlSeleccionarLaMonedaUSDEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("german@gmail.com")).thenReturn(user);

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
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("usd", "bitcoin", request);

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "USD");
    }
    @Test
    public void queAlSeleccionarLaMonedaGBPEnElSelectHagaLaConversionDeLaCriptoYLaMuestre() {
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        Usuario user = new Usuario();
        user.setId(1L);
        when(servicioUsuario.buscarUsuarioPorEmail("german@gmail.com")).thenReturn(user);

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
        ModelAndView mavRecibido = controladorHome.cargarPrecioDeCryptos("gbp", "bitcoin", request);

        assertEquals(mavRecibido.getModel().get("divisaAMostrar"), "GBP");
    }
}
