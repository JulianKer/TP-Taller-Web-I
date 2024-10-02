package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.ServicioUsuarioImpl;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPerfil {


    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorPerfil(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/perfil", method = RequestMethod.GET)
    public ModelAndView perfil(HttpServletRequest request) {
        /*aca podria hacer lo q hacemos en php de q si NO encuentra la variable emailUsuario de la
        * sesion que te llega por el HttpServletRequest, que te redirija al login entonces:
        * No puede acceder por la ulr poniendo /perfil sino q te ecxpulsa al login para q inicies
        * ESTO CREO Q DEBERIAMOS PONERLOS EN TODAS LAS VISTAS PARA Q SOLO ACCEDAN USUARIOS LOGUEADOS*/
        /*
        algo asi ?? =*/
        ModelMap model = new ModelMap();

        if (request.getSession().getAttribute("emailUsuario") == null){
            model.addAttribute("error", "Primero debe iniciar sesión.");
            return new ModelAndView("redirect:/login", model);
        }

        String emailABuscar = request.getSession().getAttribute("emailUsuario").toString();
        // supongo q NO deberia corroborarlo pq si entró aca es pq ya paso el login y en el login
        // encontró al usuario por este mail asiq por eso no lo verifico si es != null
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailABuscar);

        model.addAttribute("emailUsuario", userEncontrado.getEmail());
        model.addAttribute("nombreUsuario", userEncontrado.getNombre());
        model.addAttribute("apellidoUsuario", userEncontrado.getApellido());
        model.addAttribute("telefonoUsuario", userEncontrado.getTelefono());
        model.addAttribute("fechaNacimientoUsuario", userEncontrado.getFechaNacimiento());
        model.addAttribute("premiumActivo", userEncontrado.getActivo());
        return new ModelAndView("perfil", model);
    }
}
