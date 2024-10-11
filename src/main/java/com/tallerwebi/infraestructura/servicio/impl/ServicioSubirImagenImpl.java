package com.tallerwebi.infraestructura.servicio.impl;

import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Transactional
public class ServicioSubirImagenImpl implements ServicioSubirImagen {

    @Override
    public String subirImagen(String nombreArchivo, MultipartFile imagen, String directorio) {
// veo si la extension es la que yo quiero (puse jpeg jpg webp svg y png)
        String nombreOriginal = imagen.getOriginalFilename();
        if (!validarExtension(nombreOriginal)) {
            return "Formato de imagen no valido. Solo se aceptan JPG, JPEG, PNG, SVG y WEBP.";
        }

        // aca le creo el nombre del archivo q voy a usar el q se ingreso en el input
        String nuevoNombreArchivo = nombreArchivo + dameLaExtencionDelArchivo(nombreOriginal);

        // lo creo y me fijo si ya esa ruta con la img ya existe pa q no se repita
        // (puede ser la misma img con otro nombre y dira q son distintas pero bueno je)
        File destino = new File(directorio + File.separator + nuevoNombreArchivo);
        if (destino.exists()) {
            return "Ya existe una criptomoneda con ese nombre.";
        }

        try {
            // intento guardr la img en la ruta q defini antes
            Files.createDirectories(Paths.get(directorio)); // creo la ruta si no existe
            imagen.transferTo(destino); // por ultimo la guardo
        } catch (IOException e) {
            //e.printStackTrace(); no lo dejo asi no me rompe la web xd
            return "Error al guardar la imagen.";
        }

        // si paso tod ok, te mando con msj de exito
        return "Criptomoneda agregada con exito.";
    }

    @Override
    public boolean validarExtension(String filename) {
        String extension = dameLaExtencionDelArchivo(filename);
        return extension.equals(".jpg") ||
                extension.equals(".jpeg") ||
                extension.equals(".png") ||
                extension.equals(".svg") ||
                extension.equals(".webp");
    }

    @Override
    public String dameLaExtencionDelArchivo(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            return "";
        }
        int dotIndex = nombreArchivo.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(dotIndex).toLowerCase();
        }
        return "";    }
}
