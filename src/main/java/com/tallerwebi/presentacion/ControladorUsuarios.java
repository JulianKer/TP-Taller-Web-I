package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorUsuarios {

    ServicioUsuario servicioUsuario;
    @Autowired
    public ControladorUsuarios(ServicioUsuario servicioUsuario) {

        this.servicioUsuario = servicioUsuario;

    }


    @RequestMapping(path = "/usuarios", method = RequestMethod.GET)
    public ModelAndView cargarPrecioDeCryptos(HttpServletRequest request, @RequestParam(value = "busquedaUsuario",required = false, defaultValue = "") String busquedaUsuario) {

        if (request.getSession().getAttribute("emailUsuario") == null){

            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        ModelMap model = new ModelMap();
        List<Usuario> misUsuarios= servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins();
        if(!busquedaUsuario.isEmpty()){
            misUsuarios= servicioUsuario.filtrarUsuarioPorBusqueda(misUsuarios, busquedaUsuario);
           if(misUsuarios.isEmpty()){
               model.addAttribute("error", "No hay coincidencias para: " + busquedaUsuario);
               model.addAttribute("misUsuarios", servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins());
           }
            model.addAttribute("misUsuarios", misUsuarios);
        }
        model.addAttribute("misUsuarios", misUsuarios);
        model.addAttribute("usuario", request.getSession().getAttribute("usuario"));
        return new ModelAndView("usuarios", model);
    }

    @RequestMapping(path = "/desbloquearUsuario/{idUsuario}", method = RequestMethod.GET)
    public ModelAndView desbloquearUsuario(@PathVariable(value = "idUsuario", required = true) Long idUsuario) {

        if(idUsuario==null){
            return new ModelAndView("redirect:/usuarios?error=No existe el usuario");
        }

        String mensaje = servicioUsuario.desbloquearUsuario(idUsuario);


        return new ModelAndView("redirect:/usuarios?mensaje=" + mensaje);

    }

    @RequestMapping(path = "/bloquearUsuario/{idUsuario}", method = RequestMethod.GET)
    public ModelAndView bloquearUsuario(@PathVariable(value = "idUsuario", required = true) Long idUsuario) {

        if(idUsuario==null){
            return new ModelAndView("redirect:/usuarios?error=No existe el usuario");
        }

        String mensaje = servicioUsuario.bloquearUsuario(idUsuario);


        return new ModelAndView("redirect:/usuarios?mensaje=" + mensaje);
    }


}
