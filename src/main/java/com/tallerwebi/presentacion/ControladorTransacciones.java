package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.TransaccionProgramada;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.CriptomonedasInsuficientesException;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

        // este es SOLO para el historial de transacciones que ya se transaccionaron
        List<Transaccion> historialTransacciones;
        Long idUsuario = userEncontrado.getId();
        try {
            TipoTransaccion tipoTransaccionEncontrada = TipoTransaccion.valueOf(tipoTransaccion);
            historialTransacciones = servicioTransacciones.filtrarTransacciones(tipoTransaccionEncontrada, idUsuario);
        } catch (IllegalArgumentException e) {
            historialTransacciones = servicioTransacciones.obtenerHistorialTransaccionesDeUsuario(idUsuario);
        }

        // y este es SOLO para las transacciones programadas, osea, las programaron pero todavia no se transaccionó
        List<TransaccionProgramada> transaccionesProgramadas;
        try {
            TipoTransaccion tipoTransaccionEncontrada = TipoTransaccion.valueOf(tipoTransaccion);
            transaccionesProgramadas = servicioTransacciones.filtrarTransaccionesProgramadas(tipoTransaccionEncontrada, idUsuario);
        } catch (IllegalArgumentException e) {
            transaccionesProgramadas = servicioTransacciones.obtenerHistorialTransaccionesDeUsuarioProgramadas(idUsuario);
        }

        model.put("criptos", servicioCriptomoneda.obtenerCriptosHabilitadas());
        model.put("emailUsuario", userEncontrado.getEmail());

        model.put("historialTransacciones", historialTransacciones);
        model.put("transaccionesProgramadas", transaccionesProgramadas);
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

        //precioDeCripto = servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre(nombreDeCripto);
        precioDeCripto = criptomonedaEncontrada.getPrecioActual();
        String mensaje = "";
        try{
            mensaje = servicioTransacciones.crearTransaccion(criptomonedaEncontrada,precioDeCripto, cantidadDeCripto, tipoDeTransaccion, usuarioEncontrado, null, null,false);
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
            mensaje = servicioTransacciones.crearTransaccion(criptoADar,precioDeCriptoADar, cantidadDeCripto, tipoDeTransaccion, usuarioEncontrado, criptoAObtener, precioDeCriptoAObtener,false);
            return new ModelAndView("redirect:/transacciones?mensaje=" + mensaje  + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }catch (SaldoInsuficienteException | CriptomonedasInsuficientesException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage()  + "&nombreDeCriptoADarSeleccionada=" + nombreDeCripto + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCripto2 + "&tipoTransaccionSeleccionada=" + tipoDeTransaccion);
        }
    }

    // -------------------- PROGRAMADA alguna transaccion ---------------------------------------------------------------
    @RequestMapping(path = "/programarTransaccion", method = RequestMethod.POST)
    public ModelAndView programarTransaccion(@RequestParam(value = "selectorTransaccionProgramada") TipoTransaccion tipoTransaccionProgramada,
                                             @RequestParam(value = "selectorCriptoProgramada") String nombreCriptoProgramada,
                                             @RequestParam(value = "cantidadDeCriptoProgramada") Double cantidadDeCriptoProgramada,
                                             @RequestParam(value = "selectorCondicionProgramada") String selectorCondicionProgramada,
                                             @RequestParam(value = "precioACumplir") Double precioACumplir,
                                             @RequestParam(value = "nombreDeCriptoAObtenerProgramada", required = false, defaultValue = "") String nombreDeCriptoAObtenerProgramada,
                                             HttpServletRequest request){

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        String emailUsuario = (String) request.getSession().getAttribute("emailUsuario");

        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailUsuario);
        if (userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }

        ModelMap model = new ModelMap();
        //-------------------------------------------- a partid de aca es logica de programadas -----------
        if (cantidadDeCriptoProgramada == null){
            return new ModelAndView("redirect:/transacciones?mensaje=Debe especificar la cantidad.&nombreDeCriptoADarSeleccionada=" + nombreCriptoProgramada + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCriptoAObtenerProgramada + "&tipoTransaccionSeleccionada=" + tipoTransaccionProgramada);
        }

        if (precioACumplir == null){
            return new ModelAndView("redirect:/transacciones?mensaje=Debe especificar el precio a cumplir.&nombreDeCriptoADarSeleccionada=" + nombreCriptoProgramada + "&nombreDeCriptoAObtenerSeleccionada=" + nombreDeCriptoAObtenerProgramada + "&tipoTransaccionSeleccionada=" + tipoTransaccionProgramada);
        }

        if (tipoTransaccionProgramada == TipoTransaccion.INTERCAMBIO && nombreDeCriptoAObtenerProgramada == null){
            return new ModelAndView("redirect:/transacciones?mensaje=Debe especificar la criptomoneda a recibir.&nombreDeCriptoADarSeleccionada=" + nombreCriptoProgramada + "&tipoTransaccionSeleccionada=" + tipoTransaccionProgramada);
        }
        // todas las puse en el mismo metodo pq hacen lo mismo, se hace el new y se guarda je
        return intentarProgramarUnaTransaccion(nombreCriptoProgramada, nombreDeCriptoAObtenerProgramada, cantidadDeCriptoProgramada, tipoTransaccionProgramada, selectorCondicionProgramada, precioACumplir, userEncontrado);
    }

    public ModelAndView intentarProgramarUnaTransaccion(String nombreCriptoProgramada, String nombreDeCriptoAObtenerProgramada, Double cantidadDeCriptoProgramada, TipoTransaccion tipoTransaccionProgramada, String condicionProgramada, Double precioACumplir, Usuario userEncontrado){

        Criptomoneda criptomonedaEncontrada;
        Criptomoneda criptomonedaAObtenerEncontrada = null;
        try {
            criptomonedaEncontrada = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreCriptoProgramada);
        }catch (NoSeEncontroLaCriptomonedaException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage() + "&nombreDeCriptoADarSeleccionada=" + nombreCriptoProgramada + "&tipoTransaccionSeleccionada=" + tipoTransaccionProgramada);
        }

        if (tipoTransaccionProgramada == TipoTransaccion.INTERCAMBIO){
            try {
                criptomonedaAObtenerEncontrada = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCriptoAObtenerProgramada);
            }catch (NoSeEncontroLaCriptomonedaException e){
                return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage() + "&nombreDeCriptoADarSeleccionada=" + nombreCriptoProgramada + "&tipoTransaccionSeleccionada=" + tipoTransaccionProgramada);
            }
        }

        String mensaje = "";
        try{
            mensaje = servicioTransacciones.programarTransaccion(criptomonedaEncontrada, cantidadDeCriptoProgramada, tipoTransaccionProgramada, userEncontrado, condicionProgramada, precioACumplir, criptomonedaAObtenerEncontrada); // estos dos ultimos son cripto a obtener y precio de esa cripto a obtener (pero es para el intercambio)
            return new ModelAndView("redirect:/transacciones?mensaje=" + mensaje + "&nombreDeCriptoADarSeleccionada=" + nombreCriptoProgramada + "&tipoTransaccionSeleccionada=" + tipoTransaccionProgramada);
        }catch (SaldoInsuficienteException | CriptomonedasInsuficientesException e){
            return new ModelAndView("redirect:/transacciones?mensaje=" + e.getMessage()  + "&nombreDeCriptoADarSeleccionada=" + nombreCriptoProgramada + "&tipoTransaccionSeleccionada=" + tipoTransaccionProgramada);
        }
    }

    @RequestMapping(path = "/eliminarTransaccionProgramada/{idTransaccion}")
    public ModelAndView eliminarTransaccionProgramada(@PathVariable Long idTransaccion , HttpServletRequest request){
        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());
        if (userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }

        if (idTransaccion == null){
            return new ModelAndView("redirect:/home");
        }

        Transaccion transaccionAEliminar = servicioTransacciones.buscarTransaccionPorId(idTransaccion);

        if (transaccionAEliminar == null){
            return new ModelAndView("redirect:/home");
        }

        servicioTransacciones.eliminarTransaccion(transaccionAEliminar);
        return new ModelAndView("redirect:/transacciones?mensaje=Transaccion eliminada con exito.");
    }
}
