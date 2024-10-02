package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

    ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorRegistro(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }



    public ModelAndView registrar(String email, String pass1, String pass2, String nombre, String apellido, Long telefono, String fechaNacimiento) {
        ModelMap modelo = new ModelMap();

        if (email.isEmpty()){
            modelo.put("error", "El email es obligatorio");
            return new ModelAndView("register", modelo);
        }

        if (pass1.isEmpty() || pass2.isEmpty()){
            modelo.put("error", "La/s contraseñas estan vacias.");
            return new ModelAndView("register", modelo);
        }

        // desp de este if, ya la pass2 no me sirve, solo es para saber si son iguales. Para lo q viene uso solo la pass1
        if (!pass1.equals(pass2)){
            modelo.put("error", "Las contraseñas no coinciden.");
            return new ModelAndView("register", modelo);
        }

        if (nombre.isEmpty()){
            modelo.put("error", "El nombre es obligatorio.");
            return new ModelAndView("register", modelo);
        }

        if (apellido.isEmpty()){
            modelo.put("error", "El apellido es obligatorio.");
            return new ModelAndView("register", modelo);
        }

        if (telefono == null){ // aca si podemos desp lo cambiamos a string pq podemos tener problemas si ponen 0 adelante o atras
            modelo.put("error", "El telefono es obligatorio.");
            return new ModelAndView("register", modelo);
        }

        if (fechaNacimiento.isEmpty()){
            modelo.put("error", "La fecha de nacimiento es obligatoria.");
            return new ModelAndView("register", modelo);
        }

        try{
            servicioUsuario.registrar(email, pass1, nombre, apellido,telefono,fechaNacimiento);
        } catch (UsuarioExistente e) {
            modelo.put("error", "EI usuario ya existe");
            return new ModelAndView("register", modelo);
        }
        return new ModelAndView("redirect:/login");
    }
}