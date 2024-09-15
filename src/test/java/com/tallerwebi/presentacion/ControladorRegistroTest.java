package com.tallerwebi.presentacion;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class ControladorRegistroTest {

    ControladorRegistro controladorRegistro = new ControladorRegistro();
    @Test
    public void siExisteemailYPasswordElRegistroEsExitoso() {

        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        ModelAndView mav = whenRegistroUsuario("julian@gmail.com");

        //comprobacion --> then
        thenElRegistroesExitoso(mav);
    }

    private void thenElRegistroesExitoso(ModelAndView mav) {
       assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    private ModelAndView whenRegistroUsuario(String email) {
        ModelAndView mav = controladorRegistro.registrar(email);
        return mav;
    }

    private void givenNoExisteUsuario() {
    }

    @Test
    public void siElEmailEstaVacioElRegistroFalla() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String emailVacio = "";
        ModelAndView mav = whenRegistroUsuario(emailVacio);

        //comprobacion --> then
        thenElRegistroFalla(mav);
    }

    private void thenElRegistroFalla(ModelAndView mav) {
        assertThat(mav.getViewName(), equalToIgnoringCase("registro"));
        assertThat(mav.getModel().get("error").toString(), equalToIgnoringCase("El email es obligatorio"));
    }

    @Test
    public void siLasPasswordsSEstaEstaVaciaElRegistroFalla() {
        //HACER DE TAREA
        //deberiamos cambiar el then, que reciba en total 3 parametros mas (ModelAnView mav, String viewNameEsperado, String claveEsperada, String msjModelEsperado)
        //porque ese then lo vyo a usar en muchos test y no todos verifican el mismo error, al ser en variables, le pasamos lo que queremos que verifique en el test especifico.
    }

    @Test
    public void siAlRepetirLasPasswordsSonDistintasElRegistroFalla() {
        //HACER DE TAREA
    }
}
