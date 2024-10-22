package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
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

        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("", imagenCripto, request);

        assertEquals("redirect:/criptomonedas?mensaje=Los campos no deben estar vacios.", mav.getViewName());
    }

    @Test
    public void queAlIntentarAgregarUnaCriptoConLaImagenVaciaFalle() {
        request.getSession().setAttribute("emailUsuario", "julian@gmail.com");

        MockMultipartFile imagenCripto = null;

        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("nombreIngresadoje", imagenCripto, request);

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

        when(servicioCriptomoneda.dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla("bitcoin")).thenReturn(false);

        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("bitcoin", imagenCripto, request);

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

        ModelAndView mav = controladorCriptomonedas.agregarCriptomoneda("avalanche", imagenCripto, request);

        assertEquals("redirect:/criptomonedas?mensaje=Criptomoneda agregada con exito.", mav.getViewName());
    }

    @Test
    public void queSeNOPuedaInhabilitarUnaCriptomonedaPorqueElIdEstaVacio(){
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre("")).thenReturn(null);
        ModelAndView mav = controladorCriptomonedas.inhabilitarCriptomoneda("");

        assertEquals("redirect:/criptomonedas?mensaje=Debe seleccionar una criptomoneda para eliminarla.#anlca-criptomonedas", mav.getViewName());
    }

    @Test
    public void queNoSePuedaInhabilitarUnaCriptomonedaPorqueNoExiste(){
        when(servicioCriptomoneda.buscarCriptomonedaPorNombre("criptoInexistente")).thenReturn(null);
        ModelAndView mav = controladorCriptomonedas.inhabilitarCriptomoneda("criptoInexistente");

        assertEquals("redirect:/criptomonedas?mensaje=No se ha encontrado la criptomoneda.#anlca-criptomonedas", mav.getViewName());
    }


    @Test
    public void queNoSePuedaInhabilitarUnaCriptomonedaPorqueHuboUnErrorAlIntentarEliminarlaEliminarla(){
        Usuario user = new Usuario();
        user.setId(1L);

        String nombreDeCripto = "bitcoin";
        Criptomoneda cripto = new Criptomoneda();
        cripto.setNombre(nombreDeCripto);

        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(cripto);
        // este te va a devolver el estado en el q quedo la cripto, como intente pero no pudo, me debe
        // dar habilitada = TRUE (q es el default q tiene)
        when(servicioCriptomoneda.inhabilitarCriptomoneda(cripto.getNombre())).thenReturn(true);
        ModelAndView mav = controladorCriptomonedas.inhabilitarCriptomoneda(nombreDeCripto);

        assertEquals("redirect:/criptomonedas?mensaje=No hemos podido inhabilitar la criptomoneda.#anlca-criptomonedas", mav.getViewName());
    }

    @Test
    public void queSePuedaInhabilitarUnaCriptomoneda(){
        Usuario user = new Usuario();
        user.setId(1L);

        String nombreDeCripto = "bitcoin";
        Criptomoneda cripto = new Criptomoneda();
        cripto.setNombre(nombreDeCripto);

        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(cripto);
        // este te va a devolver el estado en el q quedo la cripto, como la inhabilité, me debe dar habilitada = FALSE
        when(servicioCriptomoneda.inhabilitarCriptomoneda(cripto.getNombre())).thenReturn(false);
        ModelAndView mav = controladorCriptomonedas.inhabilitarCriptomoneda(nombreDeCripto);

        assertEquals("redirect:/criptomonedas?mensaje=Criptomoneda inhabilitada con exito.#anlca-criptomonedas", mav.getViewName());
    }

    @Test
    public void queSePuedaHabilitarUnaCriptomoneda(){
        String nombreDeCripto = "bitcoin";
        Criptomoneda cripto = new Criptomoneda();
        cripto.setNombre(nombreDeCripto);

        when(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(cripto);
        // este te va a devolver el estado en el q quedo la cripto, como la inhabilité, me debe dar habilitada = FALSE
        when(servicioCriptomoneda.habilitarCriptomoneda(cripto.getNombre())).thenReturn(true);
        ModelAndView mav = controladorCriptomonedas.habilitarCriptomoneda(nombreDeCripto);

        assertEquals("redirect:/criptomonedas?mensaje=Criptomoneda habilitada con exito.#anlca-criptomonedas", mav.getViewName());
    }
}
