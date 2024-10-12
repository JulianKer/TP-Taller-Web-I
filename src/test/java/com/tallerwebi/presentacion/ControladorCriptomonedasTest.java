package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorCriptomonedasTest {
    private MockHttpServletRequest request = new MockHttpServletRequest();

    private ServicioCriptomoneda servicioCriptomoneda = mock(ServicioCriptomoneda.class);
    private ServicioSubirImagen servicioSubirImagen = mock(ServicioSubirImagen.class);
    private ControladorCriptomonedas controladorCriptomonedas = new ControladorCriptomonedas(servicioCriptomoneda, servicioSubirImagen);

    @Test
    public void queAlIntentarAgregarUnaCriptoConElNombreVacioFalle() {
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");

        MockMultipartFile imagenCripto = new MockMultipartFile(
                "imagenCripto",
                "bitocin.png",
                "image/png",
                "contenido imagen".getBytes()
        );
        // Llamar directamente al metod del controlador
        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("", imagenCripto, request);

        // Verificar el resultado
        assertEquals("redirect:/criptomonedas?mensaje=Los campos no deben estar vacios.", mav.getViewName());
    }

    @Test
    public void queAlIntentarAgregarUnaCriptoConLaImagenVaciaFalle() {
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");

        MockMultipartFile imagenCripto = null;
        // Llamar directamente al metod del controlador
        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("nombreIngresadoje", imagenCripto, request);

        // Verificar el resultado
        assertEquals("redirect:/criptomonedas?mensaje=Los campos no deben estar vacios.", mav.getViewName());
    }

    @Test
    public void queAlIngresarUnaCriptoQueNoEsteEnElPaginadoElAgregarFalle() {
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");

        MockMultipartFile imagenCripto = new MockMultipartFile(
                "imagenCripto",
                "bitocin.png",
                "image/png",
                "contenido imagen".getBytes()
        );

        // Simular el comportamiento del servicio
        when(servicioCriptomoneda.dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla("bitcoin")).thenReturn(false);

        // Llamar directamente al metod del controlador
        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("bitcoin", imagenCripto, request);

        // Verificar el resultado
        assertEquals("redirect:/criptomonedas?mensaje=Por el momento no podemos agregar esa criptomoneda. Intente con otra.", mav.getViewName());
    }

    @Test
    public void queSePuedaAgregarUnaCriptomoneda() {
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");

        MockMultipartFile imagenCripto = new MockMultipartFile(
                "imagenCripto",
                "avalanche.png",
                "image/png",
                "contenido imagen".getBytes()
        );

        // Simular el comportamiento del servicio
        when(servicioCriptomoneda.dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla("avalanche")).thenReturn(true);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("avalanche");
        criptomoneda.setImagen("Avalanche.png");
        criptomoneda.setNombreConMayus("Avalanche");
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre("avalanche")).thenReturn(criptomoneda);

        when(servicioSubirImagen.subirImagen(criptomoneda.getNombreConMayus(),
                imagenCripto,
                request.getServletContext().getRealPath("/resources/core/img/logoCriptomonedas/")))
                .thenReturn("Criptomoneda agregada con exito.");

        // Llamar directamente al metod del controlador
        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("avalanche", imagenCripto, request);

        // Verificar el resultado
        assertEquals("redirect:/criptomonedas?mensaje=Criptomoneda agregada con exito.", mav.getViewName());
    }
}
