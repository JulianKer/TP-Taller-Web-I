package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorCerrarSesion {

    @RequestMapping(path = "/cerrarSesion",method = RequestMethod.GET)
    public String cerrarSesion(HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") != null) {
            request.getSession().invalidate();
        }
        return "redirect:/login";
    }















}
