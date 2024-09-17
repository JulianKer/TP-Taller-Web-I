package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

    public ModelAndView registrar(String email, String pass1, String pass2) {
        ModelMap modelo = new ModelMap();


        if (email.isEmpty()){
            modelo.put("error", "El email es obligatorio");
            return new ModelAndView("registro", modelo);
        }

        if (pass1.isEmpty() || pass2.isEmpty()){
            modelo.put("error", "La/s contraseñas estan vacias.");
            return new ModelAndView("registro", modelo);
        }

        if (!pass1.equals(pass2)){
            modelo.put("error", "Las contraseñas no coinciden.");
            return new ModelAndView("registro", modelo);
        }

        return new ModelAndView("redirect:/login");
    }
}
