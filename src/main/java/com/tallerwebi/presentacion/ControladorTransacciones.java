package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.CriptomonedasInsuficientesException;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.infraestructura.servicio.impl.ServicioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    public ModelAndView transacciones(@RequestParam(value = "tipoTransaccion",required = false, defaultValue = "todos") String tipoTransaccion,
                                      HttpServletRequest request){

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());
        if (userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }

        String emailUsuario = (String) request.getSession().getAttribute("emailUsuario");

        ModelMap model = new ModelMap();
        model.addAttribute("usuario", userEncontrado);
        //model.put("criptos", servicioCriptomoneda.obtenerNombreDeTodasLasCriptos());
        List<Transaccion> historialTransacciones;
        Long idUsuario = userEncontrado.getId();

        try {
            TipoTransaccion tipoTransaccionEncontrada = TipoTransaccion.valueOf(tipoTransaccion);
            historialTransacciones = servicioTransacciones.filtrarTransacciones(tipoTransaccionEncontrada, idUsuario);
        } catch (IllegalArgumentException e) {
            historialTransacciones = servicioTransacciones.obtenerHistorialTransaccionesDeUsuario(idUsuario);
        }


        model.put("criptos", servicioCriptomoneda.obtenerCriptosHabilitadas());
        model.put("emailUsuario", userEncontrado.getEmail());


        model.put("historialTransacciones", historialTransacciones);
        model.put("filtro", tipoTransaccion);

        return new ModelAndView("transacciones", model);
    }

    @RequestMapping(path = "/realizarTransaccion",method = RequestMethod.POST)
    public ModelAndView realizarTransaccion(String nombreDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, String emailUsuario) {
        ModelMap model = new ModelMap();
        Criptomoneda criptomonedaEncontrada;

        if (cantidadDeCripto == null){
            return new ModelAndView("redirect:/transacciones?mensaje=Debe especificar la cantidad.");
        }

        try {
            criptomonedaEncontrada = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
        }catch (NoSeEncontroLaCriptomonedaException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage());
        }

        Double precioDeCripto = servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto);
        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailUsuario); //no pregundo si es != null porque se que esxiste ya que se logeo.

        String mensaje = "";
        try{
            mensaje = servicioTransacciones.crearTransaccion(criptomonedaEncontrada,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuarioEncontrado);
            return new ModelAndView("redirect:/transacciones?mensaje=" + mensaje);
        }catch (SaldoInsuficienteException | CriptomonedasInsuficientesException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage());
        }
    }
}
