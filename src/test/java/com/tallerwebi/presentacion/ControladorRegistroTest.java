package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.ServicioUsuarioImpl;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorRegistroTest {

    ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    ControladorRegistro controladorRegistro = new ControladorRegistro(servicioUsuario);


    @Test
    public void siExisteemailYPasswordElRegistroEsExitoso() {

        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "julian@gmail.com";
        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2);

        //comprobacion --> then
        thenElRegistroesExitoso(mav);
    }

    private void thenElRegistroesExitoso(ModelAndView mav) {
       assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    private ModelAndView whenRegistroUsuario(String email, String pass1, String pass2) {
        ModelAndView mav = controladorRegistro.registrar(email, pass1, pass2);
        return mav;
    }

    private void givenNoExisteUsuario() {
    }

    @Test
    public void siElEmailEstaVacioElRegistroFalla() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String emailVacio = "";
        ModelAndView mav = whenRegistroUsuario(emailVacio, pass1, pass2);

        //comprobacion --> then
        String msjError = "El email es obligatorio";
        thenElRegistroFalla(mav, msjError);
    }

    private void thenElRegistroFalla(ModelAndView mav, String msjError) {
        assertThat(mav.getViewName(), equalToIgnoringCase("registro2"));
        assertThat(mav.getModel().get("error").toString(), equalToIgnoringCase(msjError));
    }

    @Test
    public void siLasPasswordsSEstaEstaVaciaElRegistroFalla() {
        //HACER DE TAREA
        //deberiamos cambiar el then, que reciba en total 3 parametros mas (ModelAnView mav, String viewNameEsperado, String claveEsperada, String msjModelEsperado)
        //porque ese then lo vyo a usar en muchos test y no todos verifican el mismo error, al ser en variables, le pasamos lo que queremos que verifique en el test especifico.

        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "";
        String pass2 = "";
        String email = "julian@gmail.com";

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2);
        String msjError = "La/s contraseñas estan vacias.";

        //comprobacion --> then
        thenElRegistroFalla(mav, msjError);
    }

    @Test
    public void siAlRepetirLasPasswordsSonDistintasElRegistroFalla() {
        //HACER DE TAREA

        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "123";
        String pass2 = "456";
        String email = "julian@gmail.com";

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2);
        String msjError = "Las contraseñas no coinciden.";

        //comprobacion --> then
        thenElRegistroFalla(mav, msjError);
    }


    @Test
    public void siExisteUsuarioConMailDelRegistroElRegistroFalla() {

        // la excepcion UsuarioExistente le cambie su extencion a runTime pq si estaba en Exception, esta linea
        // me decia que debia controlarla con trycatch o relanzar la excepcion, pero al ponerle
        // runtime, no es necesario controlarla pero si que la podemos esperar
        when(servicioUsuario.registrar("julian@gmail.com","11")).thenThrow(UsuarioExistente.class);
        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "julian@gmail.com";


        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2);
        thenElRegistroFalla(mav, "EI usuario ya existe");
    }

}
