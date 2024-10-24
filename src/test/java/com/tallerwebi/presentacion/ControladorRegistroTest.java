package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioUsuarioImpl;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorRegistroTest {

    ServicioUsuario servicioUsuario = mock(ServicioUsuario.class);
    ControladorRegistro controladorRegistro = new ControladorRegistro(servicioUsuario);

    @Test
    public void siExisteemailYPasswordElRegistroEsExitoso() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "juli@gmail.com";
        String nombre = "juli";
        String apellido = "schmuker";
        String fechaNacimiento = "2004-10-13";
        Long telefono = 12345678L;

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2, nombre, apellido, telefono, fechaNacimiento);

        //comprobacion --> then
        thenElRegistroesExitoso(mav);
    }

    private void thenElRegistroesExitoso(ModelAndView mav) {
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    private ModelAndView whenRegistroUsuario(String email, String pass1, String pass2, String nombre, String apellido, Long telefono, String fechaNacimiento) {
        ModelAndView mav = controladorRegistro.registrar(email, pass1, pass2, nombre, apellido, telefono, fechaNacimiento);
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
        String nombre = "juli";
        String apellido = "schmuker";
        String fechaNacimiento = "2004-10-13";
        Long telefono = 12345678L;
        ModelAndView mav = whenRegistroUsuario(emailVacio, pass1, pass2, nombre, apellido, telefono, fechaNacimiento);

        //comprobacion --> then
        String msjError = "El email es obligatorio";
        thenElRegistroFalla(mav, msjError);
    }

    private void thenElRegistroFalla(ModelAndView mav, String msjError) {
        assertThat(mav.getViewName(), equalToIgnoringCase("register"));
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
        String pass1Vacia = "";
        String pass2Vacia = "";
        String email = "juli@gmail.com";
        String nombre = "juli";
        String apellido = "schmuker";
        String fechaNacimiento = "2004-10-13";
        Long telefono = 12345678L;

        ModelAndView mav = whenRegistroUsuario(email, pass1Vacia, pass2Vacia, nombre, apellido, telefono, fechaNacimiento);
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
        String pass1 = "11";
        String pass2Repetida = "12";
        String email = "juli@gmail.com";
        String nombre = "juli";
        String apellido = "schmuker";
        String fechaNacimiento = "2004-10-13";
        Long telefono = 12345678L;

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2Repetida, nombre, apellido, telefono, fechaNacimiento);
        String msjError = "Las contraseñas no coinciden.";

        //comprobacion --> then
        thenElRegistroFalla(mav, msjError);
    }

    @Test
    public void siExisteUnUserConElMismoMailElRegistroFalla() {
        String pass1 = "11";
        String pass2Repetida = "11";
        String email = "juli@gmail.com";
        String nombre = "juli";
        String apellido = "schmuker";
        String fechaNacimiento = "2004-10-13";
        Long telefono = 12345678L;

        String msjError = "El usuario ya existe";
        ModelMap modelo = new ModelMap();
        modelo.put("error", msjError);
        ModelAndView mavEsperado = new ModelAndView("register", modelo);

        when(servicioUsuario.registrar(email, pass1, nombre, apellido,telefono,fechaNacimiento)).thenThrow(UsuarioExistente.class);
        ModelAndView mav = controladorRegistro.registrar(email, pass1, pass2Repetida, nombre, apellido,telefono,fechaNacimiento);

        String msjRecibido = mav.getModel().get("error").toString();
        assertEquals(msjError, msjRecibido);
    }

    @Test
    public void siElNombreEstaVacioElRegistroFalla() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "julian@gmail.com";
        String nombreVacio = "";
        String apellido = "schmuker";
        String fechaNacimiento = "2004-10-13";
        Long telefono = 12345678L;

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2,nombreVacio, apellido, telefono, fechaNacimiento);

        //comprobacion --> then
        String msjError = "El nombre es obligatorio.";
        thenElRegistroFalla(mav, msjError);
    }

    @Test
    public void siElApellidoEstaVacioElRegistroFalla() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "julian@gmail.com";
        String nombre = "Julian";
        String apellidoVacio = "";
        String fechaNacimiento = "2004-10-13";
        Long telefono = 12345678L;

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2,nombre, apellidoVacio, telefono, fechaNacimiento);

        //comprobacion --> then
        String msjError = "El apellido es obligatorio.";
        thenElRegistroFalla(mav, msjError);
    }

    @Test
    public void siElTelefonoEstaVacioElRegistroFalla() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "julian@gmail.com";
        String nombre = "Julian";
        String apellido = "schmuker";
        Long telefono = null;
        String fechaNacimientoVacia = "2004-10-13";

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2,nombre, apellido, telefono, fechaNacimientoVacia);

        //comprobacion --> then
        String msjError = "El telefono es obligatorio.";
        thenElRegistroFalla(mav, msjError);
    }

    @Test
    public void siLaFechaDeNacimientosEstaVaciaElRegistroFalla() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "julian@gmail.com";
        String nombre = "Julian";
        String apellido = "schmuker";
        Long telefono = 1234567L;
        String fechaNacimientoVacia = "";

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2,nombre, apellido, telefono, fechaNacimientoVacia);

        //comprobacion --> then
        String msjError = "La fecha de nacimiento es obligatoria.";
        thenElRegistroFalla(mav, msjError);
    }
/* este deberiamos hacerlo en el servicio usuario impl porque es logica del servicio el q sean +18 y ademas como
   tira una excepcionn hay q usar el when(....).thenThrows(MenorDeEdadException.class)
    @Test
    public void siLaElUserTieneMenosDe18ElRegistroFalla() {
        //preparacion --> given
        givenNoExisteUsuario();

        //ejecucion --> when
        String pass1 = "11";
        String pass2 = "11";
        String email = "julian@gmail.com";
        String nombre = "Julian";
        String apellido = "schmuker";
        Long telefono = 1234567L;
        String fechaNacimientoVacia = "2008-12-29";

        ModelAndView mav = whenRegistroUsuario(email, pass1, pass2,nombre, apellido, telefono, fechaNacimientoVacia);

        //comprobacion --> then
        String msjError = "El nombre es obligatorio.";
        thenElRegistroFalla(mav, msjError);
    }*/
}