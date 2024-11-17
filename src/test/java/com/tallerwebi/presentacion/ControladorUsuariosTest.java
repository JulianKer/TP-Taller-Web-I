package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void queSeCarguenLosUsuarios() {
        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        usuario.setEmail("julian@gmail.com");

        Usuario usuario2 = new Usuario();
        usuario2.setRol("CLIENTE");
        usuario2.setEmail("german@gmail.com");

        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");
        request.getSession().setAttribute("usuario", usuario);

        when(servicioUsuario.buscarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);

        ArrayList<Usuario> listaUsuariosNoAdmin = new ArrayList<>();
        listaUsuariosNoAdmin.add(usuario2);

        when(servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins()).thenReturn(listaUsuariosNoAdmin);

        ModelAndView mav = controladorUsuarios.cargarUsuarios(request, "");

        assertEquals(mav.getViewName(), "usuarios");
        assertEquals(mav.getModelMap().getAttribute("misUsuarios"), listaUsuariosNoAdmin);
    }

    @Test
    public void queSeCarguenLosUsuariosSegunLaElCriterioDeBusqueda() {
        String criterioDeBusqueda = "an"; // me deberia dar a julian y a german

        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        usuario.setEmail("gasti@gmail.com");

        Usuario usuario2 = new Usuario();
        usuario2.setRol("CLIENTE");
        usuario2.setEmail("german@gmail.com");

        Usuario usuario3 = new Usuario();
        usuario3.setRol("CLIENTE");
        usuario3.setEmail("julian@gmail.com");

        Usuario usuario4 = new Usuario();
        usuario4.setRol("CLIENTE");
        usuario4.setEmail("toto@gmail.com");

        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");
        request.getSession().setAttribute("usuario", usuario);

        when(servicioUsuario.buscarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);

        ArrayList<Usuario> todosLosUsersNoAdmins = new ArrayList<>();
        todosLosUsersNoAdmins.add(usuario2);
        todosLosUsersNoAdmins.add(usuario3);
        todosLosUsersNoAdmins.add(usuario4);


        ArrayList<Usuario> listaUsuariosNoAdminQueCoincidenConLaBusqueda = new ArrayList<>();
        listaUsuariosNoAdminQueCoincidenConLaBusqueda.add(usuario2);
        listaUsuariosNoAdminQueCoincidenConLaBusqueda.add(usuario3);

        when(servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins()).thenReturn(todosLosUsersNoAdmins);
        when(servicioUsuario.filtrarUsuarioPorBusqueda(todosLosUsersNoAdmins, criterioDeBusqueda)).thenReturn(listaUsuariosNoAdminQueCoincidenConLaBusqueda);

        ModelAndView mav = controladorUsuarios.cargarUsuarios(request, criterioDeBusqueda);

        assertEquals(mav.getViewName(), "usuarios");
        assertEquals(mav.getModelMap().getAttribute("misUsuarios"), listaUsuariosNoAdminQueCoincidenConLaBusqueda);
    }

    @Test
    public void queSeCarguenTodosLosUsuariosPorqueNoCoincidioNingunoConElCriterioDeBusquedaaYDioUnMsjDeError() {
        String criterioDeBusqueda = "jsjsjs"; // no encuentra a nadie entonces me debe dar todos

        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        usuario.setEmail("gasti@gmail.com");

        Usuario usuario2 = new Usuario();
        usuario2.setRol("CLIENTE");
        usuario2.setEmail("german@gmail.com");

        Usuario usuario3 = new Usuario();
        usuario3.setRol("CLIENTE");
        usuario3.setEmail("julian@gmail.com");

        Usuario usuario4 = new Usuario();
        usuario4.setRol("CLIENTE");
        usuario4.setEmail("toto@gmail.com");

        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");
        request.getSession().setAttribute("usuario", usuario);

        when(servicioUsuario.buscarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);

        ArrayList<Usuario> listaUsuariosNoAdminQueCoincidenConLaBusqueda = new ArrayList<>();
        listaUsuariosNoAdminQueCoincidenConLaBusqueda.add(usuario2);
        listaUsuariosNoAdminQueCoincidenConLaBusqueda.add(usuario3);
        listaUsuariosNoAdminQueCoincidenConLaBusqueda.add(usuario4);

        when(servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins()).thenReturn(listaUsuariosNoAdminQueCoincidenConLaBusqueda);
        when(servicioUsuario.filtrarUsuarioPorBusqueda(listaUsuariosNoAdminQueCoincidenConLaBusqueda, criterioDeBusqueda)).thenReturn(new ArrayList<>());

        ModelAndView mav = controladorUsuarios.cargarUsuarios(request, criterioDeBusqueda);
        String errorEsperado = "No hay coincidencias para: " + criterioDeBusqueda;
        assertEquals(mav.getViewName(), "usuarios");
        assertEquals(mav.getModelMap().getAttribute("misUsuarios"), listaUsuariosNoAdminQueCoincidenConLaBusqueda);
        assertEquals(mav.getModelMap().getAttribute("error"), errorEsperado);
    }

    @Test
    public void queSePuedaBloquearAUnUser() {
        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        usuario.setEmail("gasti@gmail.com");

        Usuario usuario2 = new Usuario();
        usuario2.setRol("CLIENTE");
        usuario2.setId(1L);
        usuario2.setEmail("german@gmail.com");

        when(servicioUsuario.bloquearUsuario(usuario2.getId())).thenReturn("Usuario bloqueado con exito");

        ModelAndView mav = controladorUsuarios.bloquearUsuario(usuario2.getId());
        assertEquals(mav.getViewName(), "redirect:/usuarios?mensaje=Usuario bloqueado con exito");
    }

    @Test
    public void queNoSePuedaBloquearAUnUserPorqueNoExiste() {
        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        usuario.setEmail("gasti@gmail.com");

        Usuario usuario2 = new Usuario(); // al no tener id, no existe en mi bdd
        usuario2.setRol("CLIENTE");
        usuario2.setEmail("german@gmail.com");

        when(servicioUsuario.bloquearUsuario(usuario2.getId())).thenReturn("No existe el usuario");

        ModelAndView mav = controladorUsuarios.bloquearUsuario(usuario2.getId());
        assertEquals(mav.getViewName(), "redirect:/usuarios?error=No existe el usuario");
    }

    @Test
    public void queSePuedaDesbloquearAUnUser() {
        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        usuario.setEmail("gasti@gmail.com");

        Usuario usuario2 = new Usuario();
        usuario2.setRol("CLIENTE");
        usuario2.setId(1L);
        usuario2.setEmail("german@gmail.com");

        when(servicioUsuario.desbloquearUsuario(usuario2.getId())).thenReturn("Usuario desbloqueado con exito");

        ModelAndView mav = controladorUsuarios.desbloquearUsuario(usuario2.getId());
        assertEquals(mav.getViewName(), "redirect:/usuarios?mensaje=Usuario desbloqueado con exito");
    }

    @Test
    public void queNoSePuedaDesbloquearAUnUserPorqueNoExiste() {
        Usuario usuario = new Usuario();
        usuario.setRol("ADMIN");
        usuario.setEmail("gasti@gmail.com");

        Usuario usuario2 = new Usuario(); // al no tener id, no existe en mi bdd
        usuario2.setRol("CLIENTE");
        usuario2.setEmail("german@gmail.com");

        when(servicioUsuario.desbloquearUsuario(usuario2.getId())).thenReturn("No existe el usuario");

        ModelAndView mav = controladorUsuarios.desbloquearUsuario(usuario2.getId());
        assertEquals(mav.getViewName(), "redirect:/usuarios?error=No existe el usuario");
    }
}
