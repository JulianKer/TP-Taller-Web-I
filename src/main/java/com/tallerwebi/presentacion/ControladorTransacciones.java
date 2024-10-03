package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorTransacciones {

    //cree este metodo solo para tener linkeado el navbar, despues cuando tengan que
    //hacer algo, modifiquenlo como quieran.
    @GetMapping("/suscripcion")
    public String suscripcion(){
        return "suscripcion";
    }
}
