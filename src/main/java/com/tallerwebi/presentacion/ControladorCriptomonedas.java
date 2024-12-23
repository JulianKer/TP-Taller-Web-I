package com.tallerwebi.presentacion;

import com.google.gson.Gson;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.PrecioCripto;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioNotificaciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorCriptomonedas {

    private ServicioCriptomoneda servicioCriptomoneda;
    private ServicioSubirImagen servicioSubirImagen;
    private ServicioUsuario servicioUsuario;
    private ServicioNotificaciones servicioNotificaciones;

    @Autowired
    public ControladorCriptomonedas(ServicioCriptomoneda servicioCriptomoneda, ServicioSubirImagen servicioSubirImagen, ServicioUsuario servicioUsuario, ServicioNotificaciones servicioNotificaciones) {
        this.servicioCriptomoneda = servicioCriptomoneda;
        this.servicioSubirImagen = servicioSubirImagen;
        this.servicioUsuario = servicioUsuario;
        this.servicioNotificaciones = servicioNotificaciones;
    }

    @RequestMapping(path = "/criptomonedas", method = RequestMethod.GET)
    public ModelAndView cargarPrecioDeCryptos(HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());
        if (!userEncontrado.getRol().equals("ADMIN")){
            return new ModelAndView("redirect:/home");
        }
        ModelMap model = new ModelMap();

        model.addAttribute("criptosBdd", servicioCriptomoneda.obtenerNombreDeTodasLasCriptos());
        model.addAttribute("usuario", userEncontrado);
        return new ModelAndView("criptomonedas", model);
    }

    @RequestMapping(path = "/agregarCriptomoneda", method = RequestMethod.POST)
    public ModelAndView agregarCriptomoneda(@RequestParam("nombreCripto") String nombreCripto,
                                            @RequestParam("imagenCripto") MultipartFile imagenCripto,
                                            HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        // q lo q ingrese no este vacio
        if (imagenCripto == null || imagenCripto.isEmpty() || nombreCripto.isEmpty()) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=Los campos no deben estar vacios.");
        }

        // aca lo hago en minusculas pq en la api los id son en minusculas, x eso (desp tengo todos sus datos pero la busco en minuscula)
        String nombreRecibidoEnMinusculas = nombreCripto.toLowerCase();
        if (!servicioCriptomoneda.dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla(nombreRecibidoEnMinusculas)) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=Por el momento no podemos agregar esa criptomoneda. Intente con otra.");
        }

        // este try lo hago por las dudas pero no deberia traerme la exepcion pq sino el anterior if no lo pasaria, pero bueno
        Criptomoneda criptoAgregada;
        try {
            // pa saber si se agregó correctamente, la busco
            criptoAgregada = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreRecibidoEnMinusculas);
        } catch (NoSeEncontroLaCriptomonedaException e) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=Criptomoneda no encontrada.");
        }

        // de la cripto agregada (como ya tiene su simbol, name, nameEnMayus, etc) le pido el nombre en mayus
        // (pq asi puse q se guarden las img) y le añado la extension del archivo q se subio (basicamente le cambio el nombre je)
        // y desp la updateo en la bdd con su debida img
        String nombreImgConExtension = criptoAgregada.getNombreConMayus() + servicioSubirImagen.dameLaExtencionDelArchivo(imagenCripto.getOriginalFilename());
        criptoAgregada.setImagen(nombreImgConExtension);
        servicioCriptomoneda.actualizarCripto(criptoAgregada);

        // creo la ruta a donde se va a subir, con el get realpath en teoria me da toda la ruta desde
        // el raiz del server agregando la q puse en el paretesis
        String rutaASubir = request.getServletContext().getRealPath("/resources/core/img/logoCriptomonedas/");
        String msj = servicioSubirImagen.subirImagen(criptoAgregada.getNombreConMayus(), imagenCripto, rutaASubir);
        return new ModelAndView("redirect:/criptomonedas?mensaje=" + msj);
    }

    @RequestMapping(path = "/inhabilitarCriptomoneda/{idCriptomoneda}", method = RequestMethod.GET)
    public ModelAndView inhabilitarCriptomoneda(@PathVariable(value = "idCriptomoneda", required = true) String idCriptomoneda) {

        if (idCriptomoneda == null || idCriptomoneda.isEmpty()) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=Debe seleccionar una criptomoneda para eliminarla.#anlca-criptomonedas");
        }

        if (servicioCriptomoneda.buscarCriptomonedaPorNombre(idCriptomoneda) == null) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=No se ha encontrado la criptomoneda.#anlca-criptomonedas");
        }

        Boolean estaHabilitada = servicioCriptomoneda.inhabilitarCriptomoneda(idCriptomoneda);
        if (estaHabilitada) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=No hemos podido inhabilitar la criptomoneda.#anlca-criptomonedas");
        }
        return new ModelAndView("redirect:/criptomonedas?mensaje=Criptomoneda inhabilitada con exito.#anlca-criptomonedas");
    }

    @RequestMapping(path = "/habilitarCriptomoneda/{idCriptomoneda}", method = RequestMethod.GET)
    public ModelAndView habilitarCriptomoneda(@PathVariable(value = "idCriptomoneda", required = true) String idCriptomoneda) {

        if (idCriptomoneda == null || idCriptomoneda.isEmpty()) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=Debe seleccionar una criptomoneda para eliminarla.#anlca-criptomonedas");
        }

        if (servicioCriptomoneda.buscarCriptomonedaPorNombre(idCriptomoneda) == null) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=No se ha encontrado la criptomoneda.#anlca-criptomonedas");
        }

        Boolean estaHabilitada = servicioCriptomoneda.habilitarCriptomoneda(idCriptomoneda);
        if (!estaHabilitada) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=No hemos podido inhabilitar la criptomoneda.#anlca-criptomonedas");
        }

        return new ModelAndView("redirect:/criptomonedas?mensaje=Criptomoneda habilitada con exito.#anlca-criptomonedas");
    }

    @RequestMapping(path = "/inicio/{nombreCripto}", method = RequestMethod.GET)
    public ModelAndView detalleCriptomoneda(@PathVariable(value = "nombreCripto") String nombreCripto,
                                            HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") == null) {
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        Usuario userDeLaSesion = (Usuario) request.getSession().getAttribute("usuario");
        Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(userDeLaSesion.getEmail());

        if (nombreCripto == null || nombreCripto.isEmpty()) {
            return new ModelAndView("redirect:/home");
        }

        Criptomoneda criptoEncontrada = null;
        try {
            criptoEncontrada = servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreCripto);
        }catch (NoSeEncontroLaCriptomonedaException e){
            return new ModelAndView("redirect:/home");
        }

        List<PrecioCripto> historialDePrecios = servicioCriptomoneda.obtenerHistorialDePrecioCriptoDeEstaCripto(nombreCripto);

        ModelMap model = new ModelMap();

        Gson gson = new Gson();
        String historialDePreciosJson = gson.toJson(historialDePrecios);
        model.addAttribute("historialDePreciosJson", historialDePreciosJson);
        model.addAttribute("historialDePrecios", historialDePrecios);

        List<Criptomoneda> criptos = servicioCriptomoneda.obtenerCriptosHabilitadas();
        model.addAttribute("criptos", criptos);

        model.addAttribute("criptomonedaEncontrada", criptoEncontrada);

        Boolean hayAlgunaNotifSinVer = servicioNotificaciones.consultarSiHayNotificacionesSinVerParaEsteUsuario(userEncontrado.getId());
        model.addAttribute("hayNotifSinVer", hayAlgunaNotifSinVer);

        model.addAttribute("usuario", userEncontrado);
        return new ModelAndView("detalleCriptomoneda", model);
    }
}
