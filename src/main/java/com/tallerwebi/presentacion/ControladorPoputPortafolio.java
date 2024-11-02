package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPoputPortafolio {

    private ServicioUsuario servicioUsuario;

    @GetMapping("/poputPortafolio")
    public String poputPortafolio(){
        return "poputPortafolio";
    }

    @Autowired
    public ControladorPoputPortafolio(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/procesarRespuestaDeIngresarSaldo")
    public ModelAndView procesarRespuestaDePago(HttpServletRequest request,
                                                @RequestParam("status") String status,
                                                @RequestParam("payment_id") String payment_id,
                                                @RequestParam("payment_type") String payment_type,
                                                @RequestParam("external_reference") Double valorSaldoIngresado,
                                                RedirectAttributes redirectAttributes) {

        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        servicioUsuario.sumarSaldo(userDeLaSesion.getId(), valorSaldoIngresado);
        redirectAttributes.addFlashAttribute("mensajeExito", "¡Saldo acreditado exitosamente!");
        return new ModelAndView("redirect:/portfolio");
    }

}



