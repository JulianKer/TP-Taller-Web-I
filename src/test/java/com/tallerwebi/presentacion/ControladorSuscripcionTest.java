package com.tallerwebi.presentacion;

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
    ServicioSuscripcion servicioSuscripcion = mock(ServicioSuscripcionImpl.class);

    ControladorSuscripcion controladorSuscripcion= new ControladorSuscripcion(servicioSuscripcion);

    @Test
    public void queNoPuedanAccederSiNoEstanLogueados() {

        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(
                request, "approved", "123456", "credit_card");

        // Verificación
        assertEquals("redirect:/login?error=Debe ingresar primero", modelAndView.getViewName());
    }

    @Test
    public void queNoSeProceseLaSuscripcionPorqueHayCamposVacios() {
        request.getSession().setAttribute("emailUsuario", "test@user.com");

        // a este la paso cosas vacias, es por si alguien lo accede por url ya q necesito determinados parametros q me da mp
        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(
                request, "", "123456", "");

        assertEquals("redirect:/home", modelAndView.getViewName());
    }

    @Test
    public void queNoSeProceseLaSuscripcionPorqueHuboUnFallo() {
        request.getSession().setAttribute("emailUsuario", "test@user.com");

        when(servicioSuscripcion.verificarEstadoDelPago(request, "failure", "123456", "debit_card"))
                .thenReturn("?mensaje=Estamos esperando que se realize el pago.");

        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(
                request, "failure", "123456", "debit_card");

        assertEquals("redirect:/suscripcion?mensaje=Estamos esperando que se realize el pago.", modelAndView.getViewName());
    }

    @Test
    public void queLaVerificacionDePagoQuedePendiente() {
        request.getSession().setAttribute("emailUsuario", "test@user.com");

        when(servicioSuscripcion.verificarEstadoDelPago(request, "pending", "123456", "debit_card"))
                .thenReturn("?mensaje=Hubo un error al intentar procesar el pago. Vuelava a intentarlo.");

        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(
                request, "pending", "123456", "debit_card");

        assertEquals("redirect:/suscripcion?mensaje=Hubo un error al intentar procesar el pago. Vuelava a intentarlo.", modelAndView.getViewName());
    }

    @Test
    public void queLaVerificacionDePagoQuedeConEstadoDesconocido() {
        request.getSession().setAttribute("emailUsuario", "test@user.com");

        when(servicioSuscripcion.verificarEstadoDelPago(request, "rejected", "123456", "bank_transfer"))
                .thenReturn("?mensaje=Estado de pago desconocido. Vuelava a intentarlo.");

        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(
                request, "rejected", "123456", "bank_transfer");

        assertEquals("redirect:/suscripcion?mensaje=Estado de pago desconocido. Vuelava a intentarlo.", modelAndView.getViewName());
    }

    @Test
    public void queSeProceseLaSuscripcionConExitoPagandoConCuentaDeMP() {
        request.getSession().setAttribute("emailUsuario", "test@user.com");

        when(servicioSuscripcion.verificarEstadoDelPago(request, "approved", "123456", "account_money"))
                .thenReturn("?mensaje=Suscripcion exitosa.");

        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(request, "approved", "123456", "account_money");

        assertEquals("redirect:/suscripcion?mensaje=Suscripcion exitosa.", modelAndView.getViewName());
        verify(servicioSuscripcion).verificarEstadoDelPago(request, "approved", "123456", "account_money");
    }

    @Test
    public void queSeProceseLaSuscripcionConExitoPagandoConTarjetaDeDebito() {
        request.getSession().setAttribute("emailUsuario", "test@user.com");

        when(servicioSuscripcion.verificarEstadoDelPago(request, "approved", "123456", "debit_card"))
                .thenReturn("?mensaje=Suscripcion exitosa.");

        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(request, "approved", "123456", "debit_card");

        assertEquals("redirect:/suscripcion?mensaje=Suscripcion exitosa.", modelAndView.getViewName());
        verify(servicioSuscripcion).verificarEstadoDelPago(request, "approved", "123456", "debit_card");
    }

    @Test
    public void queSeProceseLaSuscripcionConExitoPagandoConTarjetaDeCredito() {
        request.getSession().setAttribute("emailUsuario", "test@user.com");

        when(servicioSuscripcion.verificarEstadoDelPago(request, "approved", "123456", "credit_card"))
                .thenReturn("?mensaje=Suscripcion exitosa.");

        ModelAndView modelAndView = controladorSuscripcion.procesarRespuestaDeSuscripcion(request, "approved", "123456", "credit_card");

        assertEquals("redirect:/suscripcion?mensaje=Suscripcion exitosa.", modelAndView.getViewName());
        verify(servicioSuscripcion).verificarEstadoDelPago(request, "approved", "123456", "credit_card");
    }



































    /*
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

     */
}
