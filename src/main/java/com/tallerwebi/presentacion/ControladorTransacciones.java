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
                                      @RequestParam(value = "nombreDeCriptoADarSeleccionada",required = false, defaultValue = "todos") String nombreDeCriptoADarSeleccionada,
                                      @RequestParam(value = "nombreDeCriptoAObtenerSeleccionada",required = false, defaultValue = "todos") String nombreDeCriptoAObtenerSeleccionada,
                                      @RequestParam(value = "tipoTransaccionSeleccionada",required = false, defaultValue = "todos") String tipoTransaccionSeleccionada,
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

        model.put("nombreDeCriptoADarSeleccionada", nombreDeCriptoADarSeleccionada);
        model.put("nombreDeCriptoAObtenerSeleccionada", nombreDeCriptoAObtenerSeleccionada);
        model.put("tipoTransaccionSeleccionada", tipoTransaccionSeleccionada);

        return new ModelAndView("transacciones", model);
    }

    @RequestMapping(path = "/realizarTransaccion",method = RequestMethod.POST)
    public ModelAndView realizarTransaccion(String nombreDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, String emailUsuario,
                                            @RequestParam(value = "nombreDeCripto2", required = false, defaultValue = "") String nombreDeCripto2) {
        if (cantidadDeCripto == null){
            return new ModelAndView("redirect:/transacciones?mensaje=Debe especificar la cantidad.&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }
        if (tipoDeTransaccion == TipoTransaccion.INTERCAMBIO && nombreDeCripto2 == null){
            return new ModelAndView("redirect:/transacciones?mensaje=Debe especificar la criptomoneda a recibir.&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }

        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailUsuario);
        if (tipoDeTransaccion == TipoTransaccion.INTERCAMBIO){
            return intentarHacerUnIntercambio(nombreDeCripto, cantidadDeCripto, tipoDeTransaccion, nombreDeCripto2, usuarioEncontrado);

        }else{
            return intentarHacerUnaVentaCompraODevolucion(nombreDeCripto, cantidadDeCripto, tipoDeTransaccion, usuarioEncontrado);
        }
    }

    private ModelAndView intentarHacerUnaVentaCompraODevolucion(String nombreDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuarioEncontrado) {
        Criptomoneda criptomonedaEncontrada;
        Double precioDeCripto;
        try {
            criptomonedaEncontrada = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
        }catch (NoSeEncontroLaCriptomonedaException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage() + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }

        precioDeCripto = servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto);
        String mensaje = "";
        try{
            mensaje = servicioTransacciones.crearTransaccion(criptomonedaEncontrada,precioDeCripto, cantidadDeCripto, tipoDeTransaccion, usuarioEncontrado, null, null);
            return new ModelAndView("redirect:/transacciones?mensaje=" + mensaje + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }catch (SaldoInsuficienteException | CriptomonedasInsuficientesException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage()  + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }
    }

    private ModelAndView intentarHacerUnIntercambio(String nombreDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, String nombreDeCripto2, Usuario usuarioEncontrado) {
        Criptomoneda criptoAObtener;
        Double precioDeCriptoADar;
        Criptomoneda criptoADar;
        Double precioDeCriptoAObtener;

        try {
            criptoADar = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
            criptoAObtener = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto2);
        }catch (NoSeEncontroLaCriptomonedaException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage() + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }

        if (criptoADar.getId().equals(criptoAObtener.getId())){
            return new ModelAndView("redirect:/transacciones?mensaje=No puedes intercambiar criptomonedas iguales. Elije dos distintas."  + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }

        precioDeCriptoADar = servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto);
        precioDeCriptoAObtener = servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto2);

        String mensaje = "";
        try{
            mensaje = servicioTransacciones.crearTransaccion(criptoADar,precioDeCriptoADar, cantidadDeCripto, tipoDeTransaccion, usuarioEncontrado, criptoAObtener, precioDeCriptoAObtener);
            return new ModelAndView("redirect:/transacciones?mensaje=" + mensaje  + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }catch (SaldoInsuficienteException | CriptomonedasInsuficientesException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage()  + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }
    }
}
