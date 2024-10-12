package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioCriptomonedaImpl;
import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioCriptomonedaTest {

    private RepositorioCriptomoneda repositorioCriptomoneda = mock(RepositorioCriptomoneda.class);
    private ServicioUsuario servicioUsuario = mock(ServicioUsuario.class);
    private ServicioTransacciones servicioTransacciones = mock(ServicioTransacciones.class);
    private ServicioCriptomoneda servicioCriptomoneda = new ServicioCriptomonedaImpl(repositorioCriptomoneda,servicioUsuario, servicioTransacciones);

    @Test
    public void queAlBuscarCriptomonedaPorNombreLaEncuentre() {
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre("bitcoin")).thenReturn(criptomoneda);
        assertNotNull(servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto));
    }

    @Test
    public void queSeIntenteBuscarCriptomonedaInexistenteYLanceUnaExcepcion() {
        String nombreDeCripto = "bitcoin";

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(null);
        assertThrows(NoSeEncontroLaCriptomonedaException.class,()->servicioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto));
    }

    @Test
    public void queSeObtengaElMapaConLasCriptosQueTengaEnMiBddATravezDeLaApi() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre("bitcoin");

        ArrayList<Criptomoneda> criptomonedas = new ArrayList<>();
        criptomonedas.add(criptomoneda);

        Map<Criptomoneda, Double> mapaRecibido = servicioCriptomoneda.obtenerCrypto(criptomonedas, "usd");
        assertEquals(criptomonedas.size(), mapaRecibido.size());
    }

    @Test
    public void queSePuedaObtenerElPrecioDeUnaCriptomonedaEnEspecifico() {
        assertTrue(servicioCriptomoneda.obtenerPrecioDeCriptoPorNombre("bitcoin") > 0);
    }

    @Test
    public void queSePuedaAgregarUnaCriptomoneda() {
        assertTrue(servicioCriptomoneda.dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla("avalanche"));
    }

    @Test
    public void queAlBuscarlaEnLaBDDNOExistaEsaCripto() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombreConMayus("Bitcoin");
        criptomoneda.setNombre("bitcoin");
        criptomoneda.setImagen("Bitcoin.png");

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre("criptoX")).thenReturn(null);

        assertTrue(servicioCriptomoneda.verificarQueNoTengaEsaCriptoEnMiBdd("criptoX"));
    }

    @Test
    public void queAlBuscarlaEnLaBDDSIExistaEsaCripto() {
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombreConMayus("Bitcoin");
        criptomoneda.setNombre("bitcoin");
        criptomoneda.setImagen("Bitcoin.png");

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre("criptoX")).thenReturn(new Criptomoneda());

        assertFalse(servicioCriptomoneda.verificarQueNoTengaEsaCriptoEnMiBdd("criptoX"));
    }


    @Test
    public void queSePuedaEliminarUnaCriptomoneda(){
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        Usuario user1 = new Usuario();
        user1.setId(1L);
        Usuario user2 = new Usuario();
        user2.setId(2L);

        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(user1);
        usuarios.add(user2);

        when(servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins()).thenReturn(usuarios);
        when(servicioTransacciones.dameLaCantidadQueEsteUsuarioTieneDeEstaCripto(user1, nombreDeCripto)).thenReturn(2.0);
        when(servicioTransacciones.dameLaCantidadQueEsteUsuarioTieneDeEstaCripto(user2, nombreDeCripto)).thenReturn(0.0);
        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(repositorioCriptomoneda.eliminarCriptomoneda(criptomoneda)).thenReturn(true);

        assertTrue(servicioCriptomoneda.eliminarCriptomoneda(criptomoneda.getNombre()));
    }

    @Test
    public void queNoSePuedaEliminarUnaCriptomonedaPorqueHuboUnErrorAlEliminarla(){
        // este test lo pongo para probar el metodo de eliminarCriptomoneda pero en TEORIA
        // no deberia nunca NO poder eliminarse (salvo q falle el .delete() de la bdd) pero bueno
        // lo pongo para q se testea igual, ademas si es que no se elimino pq no se encontró, NUNCA se ejecuta este
        // metdo, pq en el controlador ya controlo ese caso xd

        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        Usuario user1 = new Usuario();
        user1.setId(1L);
        Usuario user2 = new Usuario();
        user2.setId(2L);

        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(user1);
        usuarios.add(user2);

        when(servicioUsuario.obtenerUnaListaDeTodosLosUsuariosNoAdmins()).thenReturn(usuarios);
        when(servicioTransacciones.dameLaCantidadQueEsteUsuarioTieneDeEstaCripto(user1, nombreDeCripto)).thenReturn(2.0);
        when(servicioTransacciones.dameLaCantidadQueEsteUsuarioTieneDeEstaCripto(user2, nombreDeCripto)).thenReturn(0.0);
        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(repositorioCriptomoneda.eliminarCriptomoneda(criptomoneda)).thenReturn(false);

        assertFalse(servicioCriptomoneda.eliminarCriptomoneda(criptomoneda.getNombre()));
    }
}
