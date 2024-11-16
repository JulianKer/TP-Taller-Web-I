package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicio.ServicioNotificaciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ControladorPerfilTest {

    // este es el q uso para simular un obj http serverlet request, no uso mock pq le tengo q especificar el comportamiento
    // y me tira error diciendo q no puedo hacer un request.getSession(), nose pq
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    private ServicioNotificaciones servicioNotificaciones = mock(ServicioNotificaciones.class);
    private ControladorPerfil controladorPerfil = new ControladorPerfil(servicioUsuario, servicioNotificaciones);

    @Test
    public void queAlIntentarHacerBarraPerfilSiNoTeLogueasteDesdeElLoginTeRedirijaAlLoginConUnMensajeDeErrorOseaQueFalle() {
        ModelAndView mavRecibido = controladorPerfil.perfil(request);

        assertEquals(mavRecibido.getViewName(), "redirect:/login?error=Debe ingresar primero");
    }

    @Test
    public void queAlIntentarHacerBarraPerfilSiLogueasteCorrectamenteDesdeElLoginTeRedirijaVistaPerfilOseaQueSeaExitoso() {
        request.getSession().setAttribute("emailUsuario", "test@unlam.edu.ar");
        ModelAndView mavRecibido = controladorPerfil.perfil(request);

        assertEquals(mavRecibido.getViewName(), "perfil");
        assertEquals(request.getSession().getAttribute("emailUsuario"), "test@unlam.edu.ar");
    }
}
