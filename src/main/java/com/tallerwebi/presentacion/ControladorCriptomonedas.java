package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorCriptomonedas {

    private ServicioCriptomoneda servicioCriptomoneda;
    private ServicioSubirImagen servicioSubirImagen;

    @Autowired
    public ControladorCriptomonedas(ServicioCriptomoneda servicioCriptomoneda, ServicioSubirImagen servicioSubirImagen) {
        this.servicioCriptomoneda = servicioCriptomoneda;
        this.servicioSubirImagen = servicioSubirImagen;
    }

    @RequestMapping(path = "/criptomonedas", method = RequestMethod.GET)
    public ModelAndView cargarPrecioDeCryptos(HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }
        ModelMap model = new ModelMap();

        model.addAttribute("criptosBdd", servicioCriptomoneda.obtenerNombreDeTodasLasCriptos());
        model.addAttribute("usuario", request.getSession().getAttribute("usuario"));
        return new ModelAndView("criptomonedas", model);
    }

    @RequestMapping(path = "/agregarCriptomoneda", method = RequestMethod.POST)
    public ModelAndView agregarCriptomoneda(@RequestParam("nombreCripto") String nombreCripto,
                                            @RequestParam("imagenCripto") MultipartFile imagenCripto,
                                            HttpServletRequest request) {

        if (request.getSession().getAttribute("emailUsuario") == null){
            return new ModelAndView("redirect:/login?error=Debe ingresar primero");
        }

        // q lo q ingrese no este vacio
        if (imagenCripto.isEmpty() || nombreCripto.isEmpty()) {
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
        String msj = servicioSubirImagen.subirImagen(criptoAgregada.getNombreConMayus(),imagenCripto, rutaASubir);
        return new ModelAndView("redirect:/criptomonedas?mensaje=" + msj);
    }
}
