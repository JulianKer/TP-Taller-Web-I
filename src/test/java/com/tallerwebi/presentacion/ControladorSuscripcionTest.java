package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.servicio.ServicioSuscripcion;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioSuscripcionImpl;
import com.tallerwebi.dominio.servicio.impl.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorSuscripcionTest {

    HttpServletRequest request = new MockHttpServletRequest();
    ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    ServicioSuscripcion servicioSuscripcion = mock(ServicioSuscripcionImpl.class);

    ControladorSuscripcion controladorSuscripcion= new ControladorSuscripcion(servicioUsuario, servicioSuscripcion);





































    @Test
    public void queSePuedaSuscribirUnUsuario() {

        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.edu.ar");
        usuario.setActivo(false);
        usuario.setSaldo(40.0);

        request.getSession().setAttribute("emailUsuario", "test@unlam.edu.ar");

        when(servicioUsuario.buscarUsuarioPorEmail("test@unlam.edu.ar")).thenReturn(usuario);

        ModelAndView mav= controladorSuscripcion.validarSuscripcion(request);

        assertEquals(mav.getViewName(), "redirect:/suscripcion?mensaje=SE HA SUSCRIPTO CON EXITO");
    }

    @Test
    public void queNoSePuedaSuscribirUnUsuarioPorqueYaEstaSuscripto() {

        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.edu.ar");
        usuario.setActivo(true);
        usuario.setSaldo(40.0);

        request.getSession().setAttribute("emailUsuario", "test@unlam.edu.ar");

        when(servicioUsuario.buscarUsuarioPorEmail("test@unlam.edu.ar")).thenReturn(usuario);

        ModelAndView mav= controladorSuscripcion.validarSuscripcion(request);

        assertEquals(mav.getViewName(), "redirect:/suscripcion?mensaje=Ya esta suscripto");
    }

    @Test
    public void queNoSePuedaSuscribirPorqueNoLeAlcanzaElSaldo() {

        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.edu.ar");
        usuario.setActivo(false);
        usuario.setSaldo(10.0);

        request.getSession().setAttribute("emailUsuario", "test@unlam.edu.ar");

        when(servicioUsuario.buscarUsuarioPorEmail("test@unlam.edu.ar")).thenReturn(usuario);
        when(servicioUsuario.verificarQueTengaSaldoSuficienteParaComprar(20.0, usuario.getSaldo())).thenThrow(new SaldoInsuficienteException("NO TIENE SUFICIENTE SALDO!"));

        ModelAndView mav= controladorSuscripcion.validarSuscripcion(request);

        assertEquals(mav.getViewName(), "redirect:/suscripcion?mensaje=NO TIENE SUFICIENTE SALDO!");
    }
}
