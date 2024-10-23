package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorUsuariosTest {

    HttpServletRequest request = new MockHttpServletRequest();
    private ServicioUsuario servicioUsuario = mock(ServicioUsuario.class);
    ControladorUsuarios controladorUsuarios= new ControladorUsuarios(servicioUsuario);


    @Test
    public void queAlIngresarUnUserClienteLoRedirijaAlHome(){
        Usuario usuario = new Usuario();
        usuario.setRol("CLIENTE");
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");
        request.getSession().setAttribute("usuario", usuario);
        when(servicioUsuario.buscarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);

        String obtenido = controladorUsuarios.cargarUsuarios(request, "").getViewName();
        String esperado = "redirect:/home";
        assertEquals(esperado, obtenido);
    }
}
