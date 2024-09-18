package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

    ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorRegistro(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

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

        // desp de este if, ya la pass2 no me sirve, solo es para saber si son iguales. Para lo q viene uso solo la pass1
        if (!pass1.equals(pass2)){
            modelo.put("error", "Las contraseñas no coinciden.");
            return new ModelAndView("registro", modelo);
        }

        try{
            servicioUsuario.registrar(email, pass1);
        } catch (UsuarioExistente e) {
            modelo.put("error", "EI usuario ya existe");
            return new ModelAndView("registro", modelo);
        }

        return new ModelAndView("redirect:/login");
    }
}
