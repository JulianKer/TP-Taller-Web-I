package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class ControladorCriptomonedas {

    private final ServicioCriptomoneda servicioCriptomoneda;

    @Autowired
    public ControladorCriptomonedas(ServicioCriptomoneda servicioCriptomoneda) {
        this.servicioCriptomoneda = servicioCriptomoneda;
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

        // veo si la extension es la que yo quiero (puse jpeg jpg webp svg y png)
        String originalFilename = imagenCripto.getOriginalFilename();
        if (!validarExtension(originalFilename)) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=Formato de imagen no valido. Solo se aceptan JPG, JPEG, PNG, SVG y WEBP.");
        }

        // creo la ruta a donde se va a subir, con el get realpath en teoria me da toda la ruta desde
        // el raiz del server agregando la q puse en el paretesis
        String rutaASubir = request.getServletContext().getRealPath("/resources/core/img/logoCriptomonedas/");

        // aca le creo el nombre del archivo q voy a usar el q se ingreso en el input
        String nuevoNombreArchivo = nombreCripto + dameLaExtencionDelArchivo(originalFilename);

        // lo creo y me fijo si ya esa ruta con la img ya existe pa q no se repita
        // (puede ser la misma img con otro nombre y dira q son distintas pero bueno je)
        File destino = new File(rutaASubir + File.separator + nuevoNombreArchivo);
        if (destino.exists()) {
            return new ModelAndView("redirect:/criptomonedas?mensaje=Ya existe una criptomoneda con ese nombre.");
        }

        try {
            // intento guardr la img en la ruta q defini antes
            Files.createDirectories(Paths.get(rutaASubir)); // creo la ruta si no existe
            imagenCripto.transferTo(destino); // por ultimo la guardo
        } catch (IOException e) {
            //e.printStackTrace(); no lo dejo asi no me rompe la web xd
            return new ModelAndView("redirect:/criptomonedas?mensaje=Error al guardar la imagen.");
        }

        // si paso tod ok, te mando con msj de exito
        return new ModelAndView("redirect:/criptomonedas?mensaje=Criptomoneda agregada con exito.");
    }

    private boolean validarExtension(String filename) {
        String extension = dameLaExtencionDelArchivo(filename);
        return extension.equals(".jpg") ||
                extension.equals(".jpeg") ||
                extension.equals(".png") ||
                extension.equals(".svg") ||
                extension.equals(".webp");
    }

    private String dameLaExtencionDelArchivo(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            return "";
        }
        int dotIndex = nombreArchivo.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(dotIndex).toLowerCase();
        }
        return "";
    }
}
