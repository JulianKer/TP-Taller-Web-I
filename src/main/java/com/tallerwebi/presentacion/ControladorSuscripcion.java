package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.servicio.ServicioNotificaciones;
import com.tallerwebi.dominio.servicio.ServicioSuscripcion;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.infraestructura.servicio.impl.ServicioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorSuscripcion {

    private ServicioSuscripcion servicioSuscripcion;
    private ServicioUsuario servicioUsuario;
    private ServicioNotificaciones servicioNotificaciones;

    @Autowired
    public ControladorSuscripcion(ServicioSuscripcion servicioSuscripcion,ServicioUsuario servicioUsuario, ServicioNotificaciones servicioNotificaciones) {
        this.servicioSuscripcion = servicioSuscripcion;
        this.servicioUsuario = servicioUsuario;
        this.servicioNotificaciones = servicioNotificaciones;
    }


    //cree este metodo solo para tener linkeado el navbar, despues cuando tengan que
    //hacer algo, modifiquenlo como quieran.
    @GetMapping("/suscripcion")
    public ModelAndView suscripcion(HttpServletRequest request){
        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());
        if (userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }
        ModelMap model = new ModelMap();
        model.addAttribute("usuario", userEncontrado);
        model.addAttribute("suscripciones", servicioSuscripcion.obtenerSuscripciones());

        Boolean hayAlgunaNotifSinVer = servicioNotificaciones.consultarSiHayNotificacionesSinVerParaEsteUsuario(userEncontrado.getId());
        model.addAttribute("hayNotifSinVer", hayAlgunaNotifSinVer);

        return new ModelAndView("suscripcion", model);
    }

    @GetMapping("/procesarRespuestaDeSuscripcion")
    public ModelAndView procesarRespuestaDeSuscripcion(HttpServletRequest request,
                                                       @RequestParam("status") String status,
                                                       @RequestParam("payment_id") String payment_id,
                                                       @RequestParam("payment_type") String payment_type,
                                                       @RequestParam("external_reference") Double valorSuscripcion) {

        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());
        if (userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }

        // este lo hago para que no puedan acceder desde url a este endpoint, osea, si pone justo estos
        // param puede q si, pero bueno, no CREO q eso pase(? jaj
        if (status == null || status.isEmpty() ||
                payment_id == null || payment_id.isEmpty() ||
                payment_type == null || payment_type.isEmpty()) {
            return new ModelAndView("redirect:/home");
        }

       // Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        servicioUsuario.restarSaldo(userDeLaSesion.getId(), valorSuscripcion);

        String viewNameCompleto = "redirect:/suscripcion" + servicioSuscripcion.verificarEstadoDelPago(request, status, payment_id, payment_type);
        return new ModelAndView(viewNameCompleto);
    }
}