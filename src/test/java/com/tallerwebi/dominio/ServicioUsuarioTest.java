package com.tallerwebi.dominio;

import com.sun.security.auth.UnixNumericUserPrincipal;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.repositorio.RepositorioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
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
        assertThrows(UsuarioExistente.class, ()->whenRegistroUsuario("german@gmail.com","12345","Julian","perez",12345678L,"2004-10-13"));
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

    @Test
    public void queEncuentreAlUserPorMail(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setEmail(mail);

        when(repositorioUsuario.buscar(mail)).thenReturn(user);
        assertNotNull(servicioUsuario.buscarUsuarioPorEmail(mail));
    }

    @Test
    public void queNoEncuentreAlUserPorMail(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setEmail(mail);

        when(repositorioUsuario.buscar(mail)).thenReturn(null);
        assertNull(servicioUsuario.buscarUsuarioPorEmail(mail));
    }

   @Test
    public void queSePuedaObtenerUnaListaDeTodosLosUsuariosNoAdmins(){
       Usuario user = new Usuario();
       user.setRol("ADMIN");

       Usuario user2 = new Usuario();
       user.setRol("CLIENTE");

       Usuario user3 = new Usuario();
       user.setRol("CLIENTE");

       Usuario user4 = new Usuario();
       user.setRol("CLIENTE");

       ArrayList<Usuario> listaDeNoAdmins = new ArrayList<>();
       listaDeNoAdmins.add(user2);
       listaDeNoAdmins.add(user3);
       listaDeNoAdmins.add(user4);

       when(repositorioUsuario.obtenerUnaListaDeTodosLosUsuariosClientes()).thenReturn(listaDeNoAdmins);

       assertEquals(listaDeNoAdmins, servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins());
   }

    @Test
    public void queSePuedaBloquearUsuario(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setId(1L);
        user.setEstaBloqueado(false);
        when(repositorioUsuario.buscarUsuarioPorId(user.getId())).thenReturn(user);

        assertEquals("Usuario bloqueado con exito", servicioUsuario.bloquearUsuario(user.getId()));
    }

    @Test
    public void queNoSePuedaBloquearUsuarioPorqueNoExiste(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setId(1L);
        user.setEstaBloqueado(false);
        when(repositorioUsuario.buscarUsuarioPorId(2L)).thenReturn(null);// como le paso otro id, no deberia encontrarlo

        assertEquals("No se pudo bloquear", servicioUsuario.bloquearUsuario(2L));
    }

    @Test
    public void queNoSePuedaBloquearUsuarioPorqueYaEstaBloqueado(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setId(1L);
        user.setEstaBloqueado(true);
        when(repositorioUsuario.buscarUsuarioPorId(1L)).thenReturn(user);// como le paso otro id, no deberia encontrarlo

        assertEquals("ya esta bloqueado", servicioUsuario.bloquearUsuario(user.getId()));
    }










    @Test
    public void queSePuedaDesbloquearUsuario(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setId(1L);
        user.setEstaBloqueado(true);
        when(repositorioUsuario.buscarUsuarioPorId(user.getId())).thenReturn(user);

        assertEquals("Usuario desbloqueado con exito", servicioUsuario.desbloquearUsuario(user.getId()));
    }

    @Test
    public void queNoSePuedaDesloquearUsuarioPorqueNoExiste(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setId(1L);
        user.setEstaBloqueado(true);
        when(repositorioUsuario.buscarUsuarioPorId(2L)).thenReturn(null);// como le paso otro id, no deberia encontrarlo

        assertEquals("No se pudo bloquear", servicioUsuario.bloquearUsuario(2L));
    }

    @Test
    public void queNoSePuedaDesloquearUsuarioPorqueYaEstaDesbloqueado(){
        String mail = "hola@gmail.com";

        Usuario user = new Usuario();
        user.setId(1L);
        user.setEstaBloqueado(false);
        when(repositorioUsuario.buscarUsuarioPorId(1L)).thenReturn(user);// como le paso otro id, no deberia encontrarlo

        assertEquals("no esta bloqueado", servicioUsuario.desbloquearUsuario(user.getId()));
    }

    @Test
    public void queSePuedaVerificarQueTengaSaldoSuficienteParaComprar(){
        double precioTotal = 2000.0;
        double saldoDelUser = 5000.0;

        Usuario user = new Usuario();
        user.setSaldo(saldoDelUser);

        assertTrue(servicioUsuario.verificarQueTengaSaldoSuficienteParaComprar(precioTotal, user.getSaldo()));
    }

    @Test
    public void queNoSePuedaVerificarQueTengaSaldoSuficienteParaComprarPorqueLanzoExcepcion(){
        double precioTotal = 2000.0;
        double saldoDelUser = 100.0;

        Usuario user = new Usuario();
        user.setSaldo(saldoDelUser);

        assertThrows(SaldoInsuficienteException.class,()->servicioUsuario.verificarQueTengaSaldoSuficienteParaComprar(precioTotal, user.getSaldo()));
    }
}