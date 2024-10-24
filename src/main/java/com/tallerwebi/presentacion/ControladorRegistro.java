package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.MenorDeEdadException;
import com.tallerwebi.dominio.excepcion.PasswordLongitudIncorrecta;
import com.tallerwebi.dominio.excepcion.TelefonoConLongitudIncorrectaException;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

    ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorRegistro(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/register")
    public ModelAndView registrar() {
        return new ModelAndView("register");
    }

    @RequestMapping(path = "/validarRegistro", method = RequestMethod.POST)
    public ModelAndView registrar(String correo, String clave, String repitClave, String nombre, String apellido, Long telefono, String fechaNacimiento) {
        ModelMap modelo = new ModelMap();

        if (correo.isEmpty()){
            modelo.put("error", "El email es obligatorio");
            return new ModelAndView("register", modelo);
        }

        if (clave.isEmpty() || repitClave.isEmpty()){
            modelo.put("error", "La/s contraseñas estan vacias.");
            return new ModelAndView("register", modelo);
        }

        // desp de este if, ya la pass2 no me sirve, solo es para saber si son iguales. Para lo q viene uso solo la pass1
        if (!clave.equals(repitClave)){
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
            servicioUsuario.registrar(correo, clave, nombre, apellido,telefono,fechaNacimiento);
        } catch (UsuarioExistente e) {
            modelo.put("error", "El usuario ya existe");
            return new ModelAndView("register", modelo);
        }catch (MenorDeEdadException | TelefonoConLongitudIncorrectaException |
                 PasswordLongitudIncorrecta e) {
            modelo.put("error", e.getMessage());
            return new ModelAndView("register", modelo);
        }
        return new ModelAndView("redirect:/login");
    }
}