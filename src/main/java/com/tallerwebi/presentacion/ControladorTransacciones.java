package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
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
    private ServicioCriptomoneda servicioCriptomoneda;

    @Autowired
    public ControladorTransacciones(ServicioUsuario servicioUsuario, ServicioTransacciones servicioTransacciones, ServicioCriptomoneda servicioCriptomoneda) {
        this.servicioUsuario = servicioUsuario;
        this.servicioTransacciones = servicioTransacciones;
        this.servicioCriptomoneda = servicioCriptomoneda;
    }

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

        Criptomoneda criptomoneda = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
        if (criptomoneda == null){ //no haria falta poruqe en el select doy si o si las criptos que tengo.
            model.put("mensaje", "La criptomoneda no existe");
            return new ModelAndView("transacciones", model);
        }

        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailUsuario); //no pregundo si es != null porque se que esxiste ya que se logeo.
        String mensaje = servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuarioEncontrado);
        model.put("mensaje", mensaje);

        return new ModelAndView("transacciones", model);
    }
}
