package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.MenorDeEdadException;
import com.tallerwebi.dominio.excepcion.PasswordLongitudIncorrecta;
import com.tallerwebi.dominio.excepcion.TelefonoConLongitudIncorrectaException;
import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {

    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);
    ServicioUsuario servicioUsuario= new ServicioUsuarioImpl(repositorioUsuario);

    @Test
    public void siExisteElMailYLasPasswordElRegistroEsExitoso() {
        Usuario usuarioCreado=whenRegistroUsuario("hola@gmail.com","12345","Julian","perez",12345678L,"2004-10-13");
        thenElRegistroEsExitoso(usuarioCreado);
    }

    private void thenElRegistroEsExitoso(Usuario usuarioCreado) {
        assertThat(usuarioCreado,notNullValue());
        verify(repositorioUsuario, times(1)).guardar(usuarioCreado);
        // ademas de asertar de que el usuario creado NO sea null,
        // verificamos que el metodo guardar del mock() de repositorio, ejecute ALMENOS UNA VEZ el metodo guardar(),
        // si NO se ejecuto, me tirarÃ¡ error (el verify SOLO funciona para objetos mock())
    }

    private Usuario whenRegistroUsuario(String mail, String pass, String nombre, String apellido, Long telefono, String fechaNacimiento) {
        return servicioUsuario.registrar(mail,pass,nombre,apellido,telefono,fechaNacimiento);
    }

    @Test
    public void siLaPasswordTieneMenosDe5CaracteresElRegistroFalla() {
        // Usuario usuarioCreado=whenRegistroUsuario("hola@gmail.com","123");
        assertThrows(PasswordLongitudIncorrecta.class,()->whenRegistroUsuario("hola@gmail.com","123","Julian","perez",12345678L,"2004-10-13"));
        //thenElRegistroFalla(usuarioCreado);
    }

    private void thenElRegistroFalla(Usuario usuarioCreado) {
        assertThat(usuarioCreado,nullValue());
    }

    @Test
    public void siYaHayUnUsuarioCreadoConElMismoEmailElRegistroFalla(){

        givenExisteUsuario("german@gmail.com","12345","Julian","perez",12345678L,"2004-10-13");

        when(repositorioUsuario.buscar("german@gmail.com")).thenReturn(new Usuario());
        Usuario usuarioCreado = whenRegistroUsuario("german@gmail.com","12345","Julian","perez",12345678L,"2004-10-13");

        thenElRegistroFalla(usuarioCreado);
    }

    private void givenExisteUsuario(String mail, String password, String nombre, String apellido, Long telefono, String fechaNacimiento) {
        whenRegistroUsuario(mail,password, nombre, apellido, telefono, fechaNacimiento);
    }

    @Test
    public void siElTelefonoTieneMenosDe8DigitosElRegistroFalla() {
        assertThrows(TelefonoConLongitudIncorrectaException.class,()->whenRegistroUsuario("hola@gmail.com","12345","Julian","perez",12345L,"2004-10-13"));
    }

    @Test
    public void siLaElUserTieneMenosDe18ElRegistroFalla() {
        assertThrows(MenorDeEdadException.class,()->whenRegistroUsuario("hola@gmail.com","12345","Julian","perez",12345678L,"2010-12-29"));
    }
}