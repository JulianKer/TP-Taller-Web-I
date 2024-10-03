package com.tallerwebi.presentacion;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorCerrarSesionTest{

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private ControladorCerrarSesion controladorCerrarSesion = new ControladorCerrarSesion();

    @Test
    public void queSiUnUsuarioLogeadoCierreSesionSeInvalideLaMisma(){
        request.getSession().setAttribute("emailUsuario", "german@gmail.com");
        //assertNotNull(request.getSession().getAttribute("emailUsuario"));

        assertEquals("redirect:/login", controladorCerrarSesion.cerrarSesion(request));
        assertNull(request.getSession().getAttribute("emailUsuario"));
    }

    @Test
    public void queSiunUsuarioNoLogeadoIntenteBarraCerrarSesionLoRedirijaAlLogin(){
        assertEquals("redirect:/login", controladorCerrarSesion.cerrarSesion(request));
    }


}
