package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorTransaccionesTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    private ServicioTransacciones servicioTransacciones = mock(ServicioTransaccionesImpl.class);
    private ServicioCriptomoneda servicioCriptomoneda = mock(ServicioCriptomonedaImpl.class);
    private ControladorTransacciones controladorTransacciones = new ControladorTransacciones(servicioUsuario,servicioTransacciones,servicioCriptomoneda);


    @Test
    public void queCuandoIntenteBarraTransaccionesPorUrlSinLogearseTeRedirijaAlLoginConMensajeDeError() {
        String recibido = controladorTransacciones.transacciones(request);

        assertEquals(recibido, "redirect:/login?error=Debe ingresar primero");
    }

    @Test
    public void queCuandoSeRealiceUnaTransaccionExitosaTeDevuelvaATransaccionesConUnMensjaeDeExito(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 1.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.COMPRA;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario(); // aca solo creo un user con este mail pero pq los demas atributos no me ineteresan
        usuario.setEmail(emailUsuario);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario)).thenReturn("Transaccion exitosa.");

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,emailUsuario);

        assertEquals(mav.getViewName(), "transacciones");
        assertEquals("Transaccion exitosa.",mav.getModel().get("mensaje"));
    }

    @Test
    public void queSielCampoCantidadEsVacioLaTransaccionFalla(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = null;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.COMPRA;
        String emailUsuario = "german@gmail.com";

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,emailUsuario);

        assertEquals(mav.getViewName(), "transacciones");
        assertEquals("Debe especificar la cantidad.",mav.getModel().get("mensaje"));
    }

    @Test
    public void queSielCampoCantidadEsCeroOMenorLaTransaccionFalla(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 0.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.COMPRA;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario(); // aca solo creo un user con este mail pero pq los demas atributos no me ineteresan
        usuario.setEmail(emailUsuario);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario)).thenReturn("La cantidad debe ser mayor que 0.");

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,emailUsuario);

        assertEquals(mav.getViewName(), "transacciones");
        assertEquals("La cantidad debe ser mayor que 0.",mav.getModel().get("mensaje"));
    }
}
