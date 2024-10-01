package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControladorPerfil {


    @RequestMapping("/perfil")
    public String perfil() {
        return "perfil";
    }
}
