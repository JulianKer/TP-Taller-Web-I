package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioCriptomonedaImpl;
import com.tallerwebi.dominio.servicio.impl.ServicioTransaccionesImpl;
import com.tallerwebi.dominio.servicio.impl.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorTransaccionesTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    private ServicioTransacciones servicioTransacciones = mock(ServicioTransaccionesImpl.class);
    private ServicioCriptomoneda servicioCriptomoneda = mock(ServicioCriptomonedaImpl.class);
    private ControladorTransacciones controladorTransacciones = new ControladorTransacciones(servicioUsuario,servicioTransacciones,servicioCriptomoneda);


    @Test
    public void queAlIngresarUnUserClienteLoRedirijaAlHome(){
        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");
        request.getSession().setAttribute("usuario", usuario);
        when(servicioUsuario.buscarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);

        String obtenido = controladorTransacciones.transacciones("", "", "", "", null, null, request).getViewName();
        String esperado = "redirect:/home";
        assertEquals(esperado, obtenido);
    }

    @Test
    public void queCuandoIntenteBarraTransaccionesPorUrlSinLogearseTeRedirijaAlLoginConMensajeDeError() {
        String tipoTransaccion = "todos";
        ModelAndView recibido = controladorTransacciones.transacciones( tipoTransaccion, "", "", "", null, null, request);
        assertEquals(recibido.getViewName(), "redirect:/login?error=Debe ingresar primero");
    }

    @Test
    public void queCuandoSeRealiceUnaTransaccionCompraTeDevuelvaATransaccionesConUnMensjaeDeExito(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 1.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.COMPRA;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario(); // aca solo creo un user con este mail pero pq los demas atributos no me ineteresan
        usuario.setEmail(emailUsuario);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);
        criptomoneda.setPrecioActual(precioDeCripto);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false)).thenReturn("Transaccion exitosa.");
        //when(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto)).thenReturn(precioDeCripto);

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCripto,cantidadDeCripto,tipoDeTransaccion,emailUsuario, null);

        //assertEquals(mav.getViewName(), "transacciones");
        assertEquals("redirect:/transacciones?mensaje=Transaccion exitosa.&nombreDeCriptoADarSeleccionada=bitcoin&tipoTransaccionSeleccionada=COMPRA",mav.getViewName());
    }

    @Test
    public void queSielCampoCantidadEsVacioLaTransaccionFalla(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = null;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.COMPRA;
        String emailUsuario = "german@gmail.com";

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCripto,cantidadDeCripto,tipoDeTransaccion,emailUsuario, null);

        //assertEquals(mav.getViewName(), "transacciones");
        assertEquals("redirect:/transacciones?mensaje=Debe especificar la cantidad.&nombreDeCriptoADarSeleccionada=bitcoin&nombreDeCriptoAObtenerSeleccionada=null&tipoTransaccionSeleccionada=COMPRA",mav.getViewName());
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
        criptomoneda.setPrecioActual(precioDeCripto);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false)).thenReturn("La cantidad debe ser mayor que 0.");
        when(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto)).thenReturn(precioDeCripto);

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCripto,cantidadDeCripto,tipoDeTransaccion,emailUsuario, null);

        //assertEquals(mav.getViewName(), "transacciones");
        assertEquals("redirect:/transacciones?mensaje=La cantidad debe ser mayor que 0.&nombreDeCriptoADarSeleccionada=bitcoin&tipoTransaccionSeleccionada=COMPRA",mav.getViewName());
    }

    @Test
    public void queCuandoSeRealiceUnaTransaccionVentaTeDevuelvaATransaccionesConUnMensjaeDeExito(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 1.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.VENTA;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario(); // aca solo creo un user con este mail pero pq los demas atributos no me ineteresan
        usuario.setEmail(emailUsuario);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);
        criptomoneda.setPrecioActual(precioDeCripto);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false)).thenReturn("Transaccion exitosa.");
        when(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto)).thenReturn(precioDeCripto);

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCripto,cantidadDeCripto,tipoDeTransaccion,emailUsuario, null);

        //assertEquals(mav.getViewName(), "transacciones");
        assertEquals("redirect:/transacciones?mensaje=Transaccion exitosa.&nombreDeCriptoADarSeleccionada=bitcoin&tipoTransaccionSeleccionada=VENTA",mav.getViewName());
    }

    @Test
    public void queCuandoSeRealiceUnaTransaccionIntercambioTeDevuelvaATransaccionesConUnMensjaeDeExito(){
        String nombreDeCriptoADar = "bitcoin";
        String nombreDeCriptoAObtener = "ethereum";

        Double precioDeCriptoADar = 100.0;
        Double precioDeCriptoAObtener = 10.0;

        Double cantidadDeCriptoADar = 1.0;

        TipoTransaccion tipoDeTransaccion = TipoTransaccion.INTERCAMBIO;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario(); // aca solo creo un user con este mail pero pq los demas atributos no me ineteresan
        usuario.setEmail(emailUsuario);

        Criptomoneda criptomonedaADar = new Criptomoneda();
        criptomonedaADar.setNombre(nombreDeCriptoADar);
        criptomonedaADar.setId(1L);

        Criptomoneda criptomonedaAObtener = new Criptomoneda();
        criptomonedaAObtener.setNombre(nombreDeCriptoAObtener);
        criptomonedaAObtener.setId(2L);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoADar)).thenReturn(criptomonedaADar);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoAObtener)).thenReturn(criptomonedaAObtener);
        when(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCriptoADar)).thenReturn(precioDeCriptoADar);
        when(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCriptoAObtener)).thenReturn(precioDeCriptoAObtener);
        when(servicioTransacciones.crearTransaccion(criptomonedaADar,precioDeCriptoADar,cantidadDeCriptoADar,tipoDeTransaccion,usuario, criptomonedaAObtener, precioDeCriptoAObtener,false)).thenReturn("Transaccion exitosa.");

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCriptoADar,cantidadDeCriptoADar,tipoDeTransaccion,emailUsuario, nombreDeCriptoAObtener);

        assertEquals("redirect:/transacciones?mensaje=Transaccion exitosa.&nombreDeCriptoADarSeleccionada=bitcoin&nombreDeCriptoAObtenerSeleccionada=ethereum&tipoTransaccionSeleccionada=INTERCAMBIO",mav.getViewName());
    }

    @Test
    public void queCuandoSeRealiceUnaTransaccionIntercambioConLaMismaCriptomonedaFalle(){
        String nombreDeCriptoADar = "bitcoin";
        String nombreDeCriptoAObtener = "bitcoin";

        Double precioDeCriptoADar = 10.0;
        Double precioDeCriptoAObtener = 10.0;

        Double cantidadDeCriptoADar = 1.0;

        TipoTransaccion tipoDeTransaccion = TipoTransaccion.INTERCAMBIO;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario(); // aca solo creo un user con este mail pero pq los demas atributos no me ineteresan
        usuario.setEmail(emailUsuario);

        Criptomoneda criptomonedaADar = new Criptomoneda();
        criptomonedaADar.setNombre(nombreDeCriptoADar);
        criptomonedaADar.setId(1L);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoADar)).thenReturn(criptomonedaADar);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoAObtener)).thenReturn(criptomonedaADar);
        when(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCriptoADar)).thenReturn(precioDeCriptoADar);
        when(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCriptoAObtener)).thenReturn(precioDeCriptoAObtener);
        when(servicioTransacciones.crearTransaccion(criptomonedaADar,precioDeCriptoADar,cantidadDeCriptoADar,tipoDeTransaccion,usuario, criptomonedaADar, precioDeCriptoAObtener,false)).thenReturn("Transaccion exitosa.");

        ModelAndView mav = controladorTransacciones.realizarTransaccion(nombreDeCriptoADar,cantidadDeCriptoADar,tipoDeTransaccion,emailUsuario, nombreDeCriptoAObtener);

        assertEquals("redirect:/transacciones?mensaje=No puedes intercambiar criptomonedas iguales. Elije dos distintas.&nombreDeCriptoADarSeleccionada=bitcoin&nombreDeCriptoAObtenerSeleccionada=bitcoin&tipoTransaccionSeleccionada=INTERCAMBIO",mav.getViewName());
    }

    @Test
    public void queCuandoSeRealiceUnaTransaccionIntercambioConUnaCriptoInexistenteFalle(){
        String nombreDeCriptoADar = "bitcoin";
        String nombreDeCriptoAObtener = "kercoin"; //esta cripto no existe

        Double precioDeCriptoADar = 100.0;
        Double precioDeCriptoAObtener = 10.0;

        Double cantidadDeCriptoADar = 1.0;

        TipoTransaccion tipoDeTransaccion = TipoTransaccion.INTERCAMBIO;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario(); // aca solo creo un user con este mail pero pq los demas atributos no me ineteresan
        usuario.setEmail(emailUsuario);

        Criptomoneda criptomonedaADar = new Criptomoneda();
        criptomonedaADar.setNombre(nombreDeCriptoADar);

        Criptomoneda criptomonedaAObtener = new Criptomoneda();
        criptomonedaAObtener.setNombre(nombreDeCriptoAObtener);

        when(servicioUsuario.buscarUsuarioPorEmail(emailUsuario)).thenReturn(usuario);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoADar)).thenReturn(criptomonedaADar);
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoAObtener)).thenThrow(NoSeEncontroLaCriptomonedaException.class);

        controladorTransacciones.realizarTransaccion(nombreDeCriptoADar,cantidadDeCriptoADar,tipoDeTransaccion,emailUsuario, nombreDeCriptoAObtener);

        assertThrows(NoSeEncontroLaCriptomonedaException.class, ()-> servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoAObtener));
    }
}
