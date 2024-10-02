package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.ServicioUsuarioImpl;
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
    private ControladorPerfil controladorPerfil = new ControladorPerfil(servicioUsuario);

    @Test
    public void queAlIntentarHacerBarraPerfilSiNoTeLogueasteDesdeElLoginTeRedirijaAlLoginConUnMensajeDeErrorOseaQueFalle() {
        ModelAndView mavRecibido = controladorPerfil.perfil(request);

        assertEquals(mavRecibido.getViewName(), "redirect:/login");
        assertEquals(mavRecibido.getModel().get("error"), "Primero debe iniciar sesión.");
    }

    @Test
    public void queAlIntentarHacerBarraPerfilSiLogueasteCorrectamenteDesdeElLoginTeRedirijaVistaPerfilOseaQueSeaExitoso() {
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        ModelAndView mavRecibido = controladorPerfil.perfil(request);

        assertEquals(mavRecibido.getViewName(), "perfil");
        assertEquals(request.getSession().getAttribute("emailUsuario"), "german@gmail.com");
    }
}
