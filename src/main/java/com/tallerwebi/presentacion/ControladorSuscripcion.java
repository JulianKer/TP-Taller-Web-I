package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorSuscripcion {

    private ServicioUsuario servicioUsuario;
    @Autowired
    public ControladorSuscripcion(ServicioUsuario servicioUsuario){

        this.servicioUsuario = servicioUsuario;

    }


    //cree este metodo solo para tener linkeado el navbar, despues cuando tengan que
    //hacer algo, modifiquenlo como quieran.
    @GetMapping("/suscripcion")
    public ModelAndView suscripcion(HttpServletRequest request){
        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        return new ModelAndView("suscripcion");
    }

    @GetMapping("/validarSuscripcion")
    public ModelAndView validarSuscripcion(HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        String emailABuscar = request.getSession().getAttribute("emailUsuario").toString();

        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailABuscar);

        if (userEncontrado.getActivo() == true) {
            return new ModelAndView("redirect:/suscripcion?mensaje=Ya esta suscripto");
        }

        try{
            servicioUsuario.verificarQueTengaSaldoSuficienteParaComprar(20.0, userEncontrado.getSaldo());

        }catch(SaldoInsuficienteException e){
            return new ModelAndView("redirect:/suscripcion?mensaje=" + e.getMessage());

        }

        servicioUsuario.restarSaldo(userEncontrado.getId(), 20.0);
        servicioUsuario.cambiarEstado(userEncontrado.getId(), true);
        return new ModelAndView("redirect:/suscripcion?mensaje=SE HA SUSCRIPTO CON EXITO");

    }


}
