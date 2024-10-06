package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
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
    private ServicioCriptomoneda servicioCriptomoneda;

    @Autowired
    public ControladorTransacciones(ServicioUsuario servicioUsuario, ServicioTransacciones servicioTransacciones, ServicioCriptomoneda servicioCriptomoneda) {
        this.servicioUsuario = servicioUsuario;
        this.servicioTransacciones = servicioTransacciones;
        this.servicioCriptomoneda = servicioCriptomoneda;
    }

    @GetMapping("/transacciones")
    public ModelAndView transacciones(HttpServletRequest request){

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        ModelMap model = new ModelMap();
        model.put("msj", "bienvenido a TRANSACCIONES");
        ModelAndView mav = new ModelAndView("transacciones", model);
        return mav;
    }

    @RequestMapping(path = "/transacciones/realizarTransaccion",method = RequestMethod.POST)
    public ModelAndView realizarTransaccion(String nombreDeCripto, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, String emailUsuario) {
        ModelMap model = new ModelMap();
        Criptomoneda criptomonedaEncontrada;

        if (cantidadDeCripto == null){
            model.put("mensaje", "Debe especificar la cantidad.");
            return new ModelAndView("transacciones", model);
        }

        try {
            criptomonedaEncontrada = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
        }catch (NoSeEncontroLaCriptomonedaException e){
            model.put("mensaje", e.getMessage());
            return new ModelAndView("transacciones", model);
        }

        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailUsuario); //no pregundo si es != null porque se que esxiste ya que se logeo.
        String mensaje = servicioTransacciones.crearTransaccion(criptomonedaEncontrada,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuarioEncontrado);
        model.put("mensaje", mensaje);

        return new ModelAndView("transacciones", model);
    }
}
