package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PasswordLongitudIncorrecta;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.Test;
import org.mockito.verification.VerificationMode;

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
        Usuario usuarioCreado=whenRegistroUsuario("hola@gmail.com","12345");
        thenElRegistroEsExitoso(usuarioCreado);
    }

    private void thenElRegistroEsExitoso(Usuario usuarioCreado) {
        assertThat(usuarioCreado,notNullValue());
        verify(repositorioUsuario, times(1)).guardar(usuarioCreado);
        // ademas de asertar de que el usuario creado NO sea null,
        // verificamos que el metodo guardar del mock() de repositorio, ejecute ALMENOS UNA VEZ el metodo guardar(),
        // si NO se ejecuto, me tirará error (el verify SOLO funciona para objetos mock())

    }

    private Usuario whenRegistroUsuario(String mail, String pass) {
        return servicioUsuario.registrar(mail,pass);
    }


    @Test
    public void siLaPasswordTieneMenosDe5CaracteresElRegistroFalla() {
       // Usuario usuarioCreado=whenRegistroUsuario("hola@gmail.com","123");
        assertThrows(PasswordLongitudIncorrecta.class,()->whenRegistroUsuario("hola@gmail.com","123"));
        //thenElRegistroFalla(usuarioCreado);
    }

    private void thenElRegistroFalla(Usuario usuarioCreado) {
        assertThat(usuarioCreado,nullValue());
    }


    @Test
    public void siYaHayUnUsuarioCreadoConElMismoEmailElRegistroFalla(){

        givenExisteUsuario("german@gmail.com", "12345");

        when(repositorioUsuario.buscar("german@gmail.com")).thenReturn(new Usuario());
        Usuario usuarioCreado = whenRegistroUsuario("german@gmail.com","12345");

        thenElRegistroFalla(usuarioCreado);
    }

    private void givenExisteUsuario(String mail, String password) {
        whenRegistroUsuario(mail,password);
    }


}
