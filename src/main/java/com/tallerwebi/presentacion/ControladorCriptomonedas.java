package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorCriptomonedas {

    @RequestMapping(path = "/criptomonedas", method = RequestMethod.GET)
    public ModelAndView cargarPrecioDeCryptos(HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        ModelMap model = new ModelMap();
        model.addAttribute("usuario", request.getSession().getAttribute("usuario"));
        return new ModelAndView("criptomonedas", model);
    }
}
