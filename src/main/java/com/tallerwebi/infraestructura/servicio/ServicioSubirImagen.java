package com.tallerwebi.infraestructura.servicio;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ServicioSubirImagen {
    String subirImagen(String nombreArchivo, MultipartFile imagen, String directorio);
    boolean validarExtension(String filename);
    String dameLaExtencionDelArchivo(String nombreArchivo);
}
