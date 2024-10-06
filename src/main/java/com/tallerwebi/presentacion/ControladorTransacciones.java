package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioTransacciones;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.ServicioUsuarioImpl;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorTransacciones {

    private ServicioTransacciones servicioTransacciones;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorTransacciones(ServicioUsuario servicioUsuario, ServicioTransacciones servicioTransacciones) {
        this.servicioUsuario = servicioUsuario;
        this.servicioTransacciones = servicioTransacciones;
    }


    //cree este metodo solo para tener linkeado el navbar, despues cuando tengan que
    //hacer algo, modifiquenlo como quieran.
    @GetMapping("/transacciones")
    public String transacciones(HttpServletRequest request){

        if (request.getSession().getAttribute("emailUsuario") == null){
            return"redirect:/login?error=Debe ingresar primero";
        }
        return "redirect:/transacciones";
    }

    @RequestMapping(path = "/transacciones/realizarTransaccion",method = RequestMethod.POST)
    public ModelAndView realizarTransaccion(String nombreDeCripto, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, String emailUsuario) {
        ModelMap model = new ModelMap();

        if (cantidadDeCripto == null){
            model.put("mensaje", "Debe especificar la cantidad.");
            return new ModelAndView("transacciones", model);
        }

        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailUsuario);
        String mensaje = servicioTransacciones.crearTransaccion(nombreDeCripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuarioEncontrado);
        model.put("mensaje", mensaje);

        return new ModelAndView("transacciones", model);
    }
}
