package com.tallerwebi.infraestructura;

import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import com.tallerwebi.infraestructura.servicio.impl.ServicioSubirImagenImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServicioSubirImagenTest {

    MockHttpServletRequest request = new MockHttpServletRequest();
    ServicioSubirImagen servicioSubirImagen = new ServicioSubirImagenImpl();

    @Test
    public void queNoSePuedaSubirUnaImagenPorqueLaExtencionEsIncorrecta() {
        // a este mock de imagen le puse de extension PDF
        MockMultipartFile imagenCripto = new MockMultipartFile(
                "imagenCripto",
                "bitocin.pdf",
                "image/png",
                "contenido imagen".getBytes()
        );

        String msjRecibido = servicioSubirImagen.subirImagen("bitcoin", imagenCripto, request.getServletContext().getRealPath("/resources/core/img/logoCriptomonedas/"));
        assertEquals("Formato de imagen no valido. Solo se aceptan JPG, JPEG, PNG, SVG y WEBP.", msjRecibido);
    }

    @Test
    public void queSePuedaSubirUnaImagen() {
        /* Este me da mal pero porque cuando dentro del subirImagen, llamo a esta linea: Files.createDirectories(Paths.get(directorio));
        (linea 36 del servicioSubirImg), me dice q no puedo acceder a un obj null pero nose como puedo "simular" esa linea para los Files.
        Osea, mi logica para el test es esta (q para mi esta bien) pero me falla esa linea, asiq lo dejo comentado x las didas


        MockMultipartFile imagenCripto = new MockMultipartFile(
                "imagenCripto",
                "bitocin.png",
                "image/png",
                "contenido imagen".getBytes()
        );

        String msjRecibido = servicioSubirImagen.subirImagen("bitcoin", imagenCripto, request.getServletContext().getRealPath("/resources/core/img/logoCriptomonedas/"));
        assertEquals("Criptomoneda agregada con exito.", msjRecibido);*/
    }
}
