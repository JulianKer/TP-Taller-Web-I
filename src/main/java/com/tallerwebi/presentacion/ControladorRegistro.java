package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

    public ModelAndView registrar(String email) {
        if (email.isEmpty()){
            ModelMap modelo = new ModelMap();
            modelo.put("error", "El email es obligatorio");
            return new ModelAndView("registro", modelo);
        }
        return new ModelAndView("redirect:/login");
    }
}
