package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PasswordLongitudIncorrecta;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioUsuarioTest {

    ServicioUsuario servicioUsuario= new ServicioUsuarioImpl();

    @Test
    public void siExisteElMailYLasPasswordElRegistroEsExitoso() {
        Usuario usuarioCreado=whenRegistroUsuario("hola@gmail.com","12345");
        thenElRegistroEsExitoso(usuarioCreado);



    }

    private void thenElRegistroEsExitoso(Usuario usuarioCreado) {
        assertThat(usuarioCreado,notNullValue());
    }

    private Usuario whenRegistroUsuario(String mail, String pass) {

       Usuario usuario= servicioUsuario.registrar(mail,pass);
        return usuario;
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
}
