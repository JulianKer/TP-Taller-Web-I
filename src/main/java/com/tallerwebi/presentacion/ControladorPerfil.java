package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            // si uso redirect, NO puedo pasarle un modelo asiq tengo q usar un flash atribute (RedirectAttributes redirectAttributes)
            // pq tengo un redirect, pero sino tengo q usar el, model.put("error", "Debe ingresar primero");
            // como ninguna de las anterioires formas me funco (no funco pq tengo q modificar toods los test
            // ya q recibe un parametro RedirectAttributes redirectAttributes y no los modifique :))
            // asique mejor lo paso por get y listo, ahí funcionó
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        String emailABuscar = request.getSession().getAttribute("emailUsuario").toString();
        // supongo q NO deberia corroborarlo pq si entró aca es pq ya paso el login y en el login
        // encontró al usuario por este mail asiq por eso no lo verifico si es != null
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailABuscar);

        if (userEncontrado == null) {
            model.put("error", "Usuario no encontrado.");
            return new ModelAndView("perfil", model);
        }

        model.put("emailUsuario", userEncontrado.getEmail());
        model.put("nombreUsuario", userEncontrado.getNombre());
        model.put("apellidoUsuario", userEncontrado.getApellido());
        model.put("telefonoUsuario", userEncontrado.getTelefono());
        model.put("fechaNacimientoUsuario", userEncontrado.getFechaNacimiento());
        model.put("premiumActivo", userEncontrado.getActivo());
        return new ModelAndView("perfil", model);
    }
}
