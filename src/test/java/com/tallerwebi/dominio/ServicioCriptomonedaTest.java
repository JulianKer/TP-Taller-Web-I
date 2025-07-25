package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.PrecioCripto;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import com.tallerwebi.dominio.repositorio.RepositorioPrecioCripto;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioCriptomonedaImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioCriptomonedaTest {

    private RepositorioCriptomoneda repositorioCriptomoneda = mock(RepositorioCriptomoneda.class);
    private RepositorioPrecioCripto repositorioPrecioCripto = mock(RepositorioPrecioCripto.class);
    private ServicioUsuario servicioUsuario = mock(ServicioUsuario.class);
    private ServicioTransacciones servicioTransacciones = mock(ServicioTransacciones.class);
    private ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda = mock(ServicioBilleteraUsuarioCriptomoneda.class);
    private ServicioCriptomoneda servicioCriptomoneda = new ServicioCriptomonedaImpl(repositorioCriptomoneda,servicioUsuario, servicioTransacciones, servicioBilleteraUsuarioCriptomoneda, repositorioPrecioCripto);

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

    /*@Test
    public void queSePuedaAgregarUnaCriptomoneda() {
        assertTrue(servicioCriptomoneda.dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla("bitcoin"));
    }*/

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
    public void queSePuedaInhabilitarUnaCriptomoneda(){
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
        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(repositorioCriptomoneda.inhabilitarCriptomoneda(criptomoneda)).thenReturn(false);

        assertFalse(servicioCriptomoneda.inhabilitarCriptomoneda(criptomoneda.getNombre()));
    }

    @Test
    public void queNoSePuedaInhabilitarUnaCriptomonedaPorqueHuboUnErrorAlInhabilitarla(){
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
        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(repositorioCriptomoneda.inhabilitarCriptomoneda(criptomoneda)).thenReturn(true);

        assertTrue(servicioCriptomoneda.inhabilitarCriptomoneda(criptomoneda.getNombre()));
    }

    @Test
    public void queSePuedaHabilitarUnaCriptomoneda(){
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);
        criptomoneda.setHabilitada(false);

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);
        when(repositorioCriptomoneda.habilitarCriptomoneda(criptomoneda)).thenReturn(true);

        assertTrue(servicioCriptomoneda.habilitarCriptomoneda(criptomoneda.getNombre()));
    }

    @Test
    public void queNoSePuedaHabilitarUnaCriptomonedaPorqueNoExiste(){
        String nombreDeCripto = "bitcoin";
        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);
        criptomoneda.setHabilitada(false);

        when(repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto)).thenThrow(NoSeEncontroLaCriptomonedaException.class);
        when(repositorioCriptomoneda.habilitarCriptomoneda(criptomoneda)).thenReturn(false);
    }

    @Test
    public void queSeObtenganLasCriptosHabilitadas(){
        Criptomoneda criptomoneda1 = new Criptomoneda();
        criptomoneda1.setId(1L);
        criptomoneda1.setNombre("bitcoin");
        criptomoneda1.setHabilitada(true);

        Criptomoneda criptomoneda2 = new Criptomoneda();
        criptomoneda2.setId(2L);
        criptomoneda2.setNombre("ethereum");
        criptomoneda2.setHabilitada(false);

        Criptomoneda criptomoneda3 = new Criptomoneda();
        criptomoneda3.setId(4L);
        criptomoneda3.setNombre("dogecoin");
        criptomoneda3.setHabilitada(true);

        List<Criptomoneda> criptomonedasHabilitadas = new ArrayList<>();
        criptomonedasHabilitadas.add(criptomoneda1);
        criptomonedasHabilitadas.add(criptomoneda3);


        List<Criptomoneda> criptomonedasTotales = new ArrayList<>();
        criptomonedasTotales.add(criptomoneda1);
        criptomonedasTotales.add(criptomoneda2);
        criptomonedasTotales.add(criptomoneda3);

        when(repositorioCriptomoneda.obtenerCriptosHabilitadas()).thenReturn((ArrayList<Criptomoneda>) criptomonedasHabilitadas);
        ArrayList<Criptomoneda> criptosRecibidas = servicioCriptomoneda.obtenerCriptosHabilitadas();
        assertEquals(criptosRecibidas, criptomonedasHabilitadas);
    }

    @Test
    public void queSePuedaObtenerUnaListaDeTodasLasCriptosHabilitadas(){
        ArrayList<Criptomoneda> lista = new ArrayList<>();

        Criptomoneda criptomoneda1 = new Criptomoneda();
        criptomoneda1.setId(1L);
        criptomoneda1.setNombre("bitcoin");
        criptomoneda1.setHabilitada(true);

        Criptomoneda criptomoneda2 = new Criptomoneda();
        criptomoneda2.setId(2L);
        criptomoneda2.setNombre("ethereum");
        criptomoneda2.setHabilitada(false);

        lista.add(criptomoneda1);

        when(repositorioCriptomoneda.obtenerCriptosHabilitadas()).thenReturn(lista);

        assertEquals(repositorioCriptomoneda.obtenerCriptosHabilitadas().size(), lista.size());
    }

    @Test
    public void queSeobtenengaHistorialDePrecioCriptoDeUnaCripto(){
        Criptomoneda criptomoneda1 = new Criptomoneda();
        criptomoneda1.setId(1L);
        criptomoneda1.setNombre("bitcoin");
        criptomoneda1.setHabilitada(true);

        Double precioDeCripto = 100.0;
        PrecioCripto precioCripto = new PrecioCripto();
        precioCripto.setCriptomoneda(criptomoneda1);
        precioCripto.setPrecioActual(precioDeCripto);

        PrecioCripto precioCripto2 = new PrecioCripto();
        precioCripto2.setCriptomoneda(criptomoneda1);
        precioCripto2.setPrecioActual(precioDeCripto);

        PrecioCripto precioCripto3 = new PrecioCripto();
        precioCripto3.setCriptomoneda(criptomoneda1);
        precioCripto2.setPrecioActual(precioDeCripto);

        List<PrecioCripto> precios = new ArrayList<>();
        precios.add(precioCripto);
        precios.add(precioCripto2);
        precios.add(precioCripto3);

        when(repositorioPrecioCripto.obtenerHistorialDePrecioCriptoDeEstaCripto(criptomoneda1.getNombre())).thenReturn(precios);

        assertEquals(repositorioPrecioCripto.obtenerHistorialDePrecioCriptoDeEstaCripto(criptomoneda1.getNombre()).size(), precios.size());
    }
}
