package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorNotificaciones {

    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorNotificaciones(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/notificaciones", method = RequestMethod.GET)
    public ModelAndView notificaciones(HttpServletRequest request){

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        ModelMap model = new ModelMap();
        Usuario usuarioEncontrado=servicioUsuario.buscarUsuarioPorEmail((String) request.getSession().getAttribute("emailUsuario"));
        model.addAttribute("usuario", usuarioEncontrado);

        ModelAndView mav = new ModelAndView("notificaciones", model);
        return mav;
    }
}
