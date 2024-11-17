package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioNotificaciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@Controller
public class ControladorNotificaciones {

    private ServicioUsuario servicioUsuario;
    private ServicioNotificaciones servicioNotificaciones;

    @Autowired
    public ControladorNotificaciones(ServicioNotificaciones servicioNotificaciones, ServicioUsuario servicioUsuario) {
        this.servicioNotificaciones = servicioNotificaciones;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/notificaciones", method = RequestMethod.GET)
    public ModelAndView notificaciones(@RequestParam(value = "q",required = false, defaultValue = "recienteAAntiguo") String criterioDeOrdenamiento,
                                       HttpServletRequest request){

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        ModelMap model = new ModelMap();
        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorEmail((String) request.getSession().getAttribute("emailUsuario"));

        if (usuarioEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }
        model.addAttribute("usuario", usuarioEncontrado);

        Boolean hayAlgunaNotifSinVer = servicioNotificaciones.consultarSiHayNotificacionesSinVerParaEsteUsuario(usuarioEncontrado.getId());
        model.addAttribute("hayNotifSinVer", hayAlgunaNotifSinVer);

        // obtengo todas las notif (ordenadas segun el criterio)
        List<Notificacion> notificaciones = servicioNotificaciones.obtenerLasNotificacionesDelUsuario(usuarioEncontrado.getId(), criterioDeOrdenamiento);
        model.addAttribute("notificaciones", notificaciones);

        // desp les seteo el visto pq ya no me interfiere en esta peticion sino que ya para la proxima se van a motrar en visto
        servicioNotificaciones.marcarComoVistasLasNotificacionesDelUsuario(usuarioEncontrado.getId());

        model.addAttribute("criterio", criterioDeOrdenamiento);
        ModelAndView mav = new ModelAndView("notificaciones", model);
        return mav;
    }
}
