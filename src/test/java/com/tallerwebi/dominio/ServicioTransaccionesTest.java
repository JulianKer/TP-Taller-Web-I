package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.CriptomonedasInsuficientesException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import com.tallerwebi.dominio.repositorio.RepositorioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioTransacciones;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.impl.ServicioTransaccionesImpl;
import com.tallerwebi.infraestructura.servicio.impl.ServicioEmail;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioTransaccionesTest {

    private RepositorioTransacciones repositorioTransacciones = mock(RepositorioTransacciones.class);
    private ServicioUsuario servicioUsuario = mock(ServicioUsuario.class);
    private ServicioEmail servicioEmail = mock(ServicioEmail.class);
    private ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda = mock(ServicioBilleteraUsuarioCriptomoneda.class);
    private ServicioTransacciones servicioTransacciones = new ServicioTransaccionesImpl(repositorioTransacciones, servicioUsuario, servicioEmail, servicioBilleteraUsuarioCriptomoneda);


    @Test
    public void queSiIntentaComprarConSaldoInsuficienteLaTransaccionFallaYLanzaUnaExcepcion() {
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 1.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.COMPRA;
        String emailUsuario = "gern@gmail.com";

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(50.0);

        Criptomoneda cripto = new Criptomoneda();
        cripto.setNombre(nombreDeCripto);
        cripto.setPrecioActual(precioDeCripto);

        assertThrows(SaldoInsuficienteException.class,()->servicioTransacciones.crearTransaccion(cripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false));
    }

    @Test
    public void queSiIntentaComprarYTodoEstaBienLaTransaccionSeaExitosa() {
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 1.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.COMPRA;
        String emailUsuario = "german@gmail.com";

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(14000.0);

        Criptomoneda cripto = new Criptomoneda();
        cripto.setNombre(nombreDeCripto);
        cripto.setPrecioActual(precioDeCripto);

        assertEquals("Transaccion exitosa.", servicioTransacciones.crearTransaccion(cripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false));
    }

    @Test
    public void queSiIntentaVenderConCantidadDeCriptosInsuficientesLaTransaccionFallaYLanzaUnaExcepcion() {
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 10.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.VENTA;
        String emailUsuario = "gern@gmail.com";

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(50.0);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        assertThrows(CriptomonedasInsuficientesException.class,()->servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false));
    }

    @Test
    public void queSiIntentaVenderConCantidadDeCriptosSuficientesLaTransaccionSeaExitosa(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 2.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.VENTA;
        String emailUsuario = "gern@gmail.com";

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(1000.0);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setUsuario(usuario);
        billetera.setCriptomoneda(criptomoneda);
        billetera.setCantidadDeCripto(0.0);

        when(servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario)).thenReturn(billetera);
        when(servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(billetera, cantidadDeCripto)).thenReturn(true);

        assertEquals("Transaccion exitosa.",servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false));
    }

    @Test
    public void queSiIntentaDevolverConCantidadDeCriptosInsuficientesLaTransaccionFallaYLanzaUnaExcepcion() {
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 10.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.DEVOLUCION;
        String emailUsuario = "gern@gmail.com";

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(50.0);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        assertThrows(CriptomonedasInsuficientesException.class,()->servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false));
    }

    @Test
    public void queSiIntentaDevolverConCantidadDeCriptosSuficientesLaTransaccionSeaExitosa(){
        String nombreDeCripto = "bitcoin";
        Double precioDeCripto = 100.0;
        Double cantidadDeCripto = 2.0;
        TipoTransaccion tipoDeTransaccion = TipoTransaccion.DEVOLUCION;
        String emailUsuario = "gern@gmail.com";

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(1000.0);

        Criptomoneda criptomoneda = new Criptomoneda();
        criptomoneda.setNombre(nombreDeCripto);

        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setUsuario(usuario);
        billetera.setCriptomoneda(criptomoneda);
        billetera.setCantidadDeCripto(0.0);

        when(servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario)).thenReturn(billetera);
        when(servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(billetera, cantidadDeCripto)).thenReturn(true);

        assertEquals("Transaccion exitosa.",servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario, null, null,false));
    }

    @Test
    public void queSiIntentaIntercambiarConCriptosQueNoTengoLaTransaccionFalle(){
        String nombreDeCriptoADar = "bitcoin";
        String nombreDeCriptoAObtener = "ethereum";

        Double precioDeCriptoADar = 100.0;
        Double precioDeCriptoAObtener = 10.0;

        Double cantidadDeCriptoADar = 1.0;

        TipoTransaccion tipoDeTransaccion = TipoTransaccion.INTERCAMBIO;
        String emailUsuario = "german@gmail.com";

        Criptomoneda criptomonedaADar = new Criptomoneda();
        criptomonedaADar.setNombre(nombreDeCriptoADar);

        Criptomoneda criptomonedaAObtener = new Criptomoneda();
        criptomonedaAObtener.setNombre(nombreDeCriptoAObtener);

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(1000.0);


        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setUsuario(usuario);
        billetera.setCriptomoneda(criptomonedaADar);
        billetera.setCantidadDeCripto(0.0); // a esta le pongo que NO tenga criptos por ende no puede intercambiar

        BilleteraUsuarioCriptomoneda billeteraDos = new BilleteraUsuarioCriptomoneda();
        billeteraDos.setUsuario(usuario);
        billeteraDos.setCriptomoneda(criptomonedaAObtener);
        billeteraDos.setCantidadDeCripto(0.0);

        when(servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomonedaADar, usuario)).thenReturn(billetera);
        when(servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomonedaAObtener, usuario)).thenReturn(billeteraDos);

        assertThrows(CriptomonedasInsuficientesException.class,()-> servicioTransacciones.crearTransaccion(criptomonedaADar,precioDeCriptoADar,cantidadDeCriptoADar,tipoDeTransaccion,usuario, criptomonedaAObtener, precioDeCriptoAObtener,false));
    }

    @Test
    public void queSiIntentaIntercambiarCriptomonedasLaTransaccionSeaExitosa(){
        String nombreDeCriptoADar = "bitcoin";
        String nombreDeCriptoAObtener = "ethereum";

        Double precioDeCriptoADar = 100.0;
        Double precioDeCriptoAObtener = 10.0;

        Double cantidadDeCriptoADar = 1.0;

        TipoTransaccion tipoDeTransaccion = TipoTransaccion.INTERCAMBIO;
        String emailUsuario = "german@gmail.com";

        Criptomoneda criptomonedaADar = new Criptomoneda();
        criptomonedaADar.setNombre(nombreDeCriptoADar);

        Criptomoneda criptomonedaAObtener = new Criptomoneda();
        criptomonedaAObtener.setNombre(nombreDeCriptoAObtener);

        Usuario usuario = new Usuario();
        usuario.setNombre("German");
        usuario.setApellido("Schmuker");
        usuario.setTelefono(12345678L);
        usuario.setEmail(emailUsuario);
        usuario.setSaldo(1000.0);


        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setUsuario(usuario);
        billetera.setCriptomoneda(criptomonedaADar);
        billetera.setCantidadDeCripto(8.0);

        BilleteraUsuarioCriptomoneda billeteraDos = new BilleteraUsuarioCriptomoneda();
        billeteraDos.setUsuario(usuario);
        billeteraDos.setCriptomoneda(criptomonedaAObtener);
        billeteraDos.setCantidadDeCripto(0.0);

        when(servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomonedaADar, usuario)).thenReturn(billetera);
        when(servicioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomonedaAObtener, usuario)).thenReturn(billeteraDos);

        when(servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaIntercambiar(billetera, cantidadDeCriptoADar)).thenReturn(true);

        assertEquals("Transaccion exitosa.",servicioTransacciones.crearTransaccion(criptomonedaADar,precioDeCriptoADar,cantidadDeCriptoADar,tipoDeTransaccion,usuario, criptomonedaAObtener, precioDeCriptoAObtener,false));
    }

    @Test
    public void testFiltrarTransaccionesPorFecha() {
        // Datos de prueba: transacciones con distintas fechas
        Transaccion trans1 = new Transaccion();
        trans1.setFechaDeTransaccion(LocalDate.of(2023, 1, 10));

        Transaccion trans2 = new Transaccion();
        trans2.setFechaDeTransaccion(LocalDate.of(2023, 2, 5));

        Transaccion trans3 = new Transaccion();
        trans3.setFechaDeTransaccion(LocalDate.of(2023, 3, 15));

        List<Transaccion> transaccionesMock = Arrays.asList(trans1, trans2, trans3);

        // Configuración del mock
        when(repositorioTransacciones.filtrarTransacciones(any(TipoTransaccion.class), anyLong()))
                .thenReturn(transaccionesMock);

        // Rango de fechas
        LocalDate desde = LocalDate.of(2023, 1, 15);
        LocalDate hasta = LocalDate.of(2023, 3, 1);

        // Ejecuta el método de servicio
        List<Transaccion> resultado = servicioTransacciones.filtrarTransacciones(TipoTransaccion.COMPRA, 1L, desde, hasta);

        // Verifica que solo se incluyan las transacciones dentro del rango
        assertEquals(1, resultado.size());
        assertEquals(LocalDate.of(2023, 2, 5), resultado.get(0).getFechaDeTransaccion());
    }

    @Test
    public void testTransaccionExactamenteEnElLimite() {
        // Datos de prueba
        Transaccion trans1 = new Transaccion();
        trans1.setFechaDeTransaccion(LocalDate.of(2023, 1, 15));

        Transaccion trans2 = new Transaccion();
        trans2.setFechaDeTransaccion(LocalDate.of(2023, 2, 15));

        List<Transaccion> transaccionesMock = Arrays.asList(trans1, trans2);

        when(repositorioTransacciones.filtrarTransacciones(any(TipoTransaccion.class), anyLong()))
                .thenReturn(transaccionesMock);

        // Rango de fechas exacto
        LocalDate desde = LocalDate.of(2023, 1, 15);
        LocalDate hasta = LocalDate.of(2023, 2, 15);

        List<Transaccion> resultado = servicioTransacciones.filtrarTransacciones(TipoTransaccion.COMPRA, 1L, desde, hasta);

        assertEquals(2, resultado.size());
        assertEquals(LocalDate.of(2023, 1, 15), resultado.get(0).getFechaDeTransaccion());
        assertEquals(LocalDate.of(2023, 2, 15), resultado.get(1).getFechaDeTransaccion());
    }


    @Test
    public void testRangoSinLimite() {
        // Datos de prueba
        Transaccion trans1 = new Transaccion();
        trans1.setFechaDeTransaccion(LocalDate.of(2023, 1, 10));

        Transaccion trans2 = new Transaccion();
        trans2.setFechaDeTransaccion(LocalDate.of(2023, 2, 5));

        Transaccion trans3 = new Transaccion();
        trans3.setFechaDeTransaccion(LocalDate.of(2023, 3, 15));

        List<Transaccion> transaccionesMock = Arrays.asList(trans1, trans2, trans3);

        when(repositorioTransacciones.filtrarTransacciones(any(TipoTransaccion.class), anyLong()))
                .thenReturn(transaccionesMock);

        // Rango de fechas vacío
        List<Transaccion> resultado = servicioTransacciones.filtrarTransacciones(TipoTransaccion.COMPRA, 1L, null, null);

        assertEquals(3, resultado.size());
    }


    @Test
    public void testSinTransaccionesDentroDelRango() {
        // Datos de prueba
        Transaccion trans1 = new Transaccion();
        trans1.setFechaDeTransaccion(LocalDate.of(2023, 1, 10));

        Transaccion trans2 = new Transaccion();
        trans2.setFechaDeTransaccion(LocalDate.of(2023, 2, 5));

        Transaccion trans3 = new Transaccion();
        trans3.setFechaDeTransaccion(LocalDate.of(2023, 3, 15));

        List<Transaccion> transaccionesMock = Arrays.asList(trans1, trans2, trans3);

        when(repositorioTransacciones.filtrarTransacciones(any(TipoTransaccion.class), anyLong()))
                .thenReturn(transaccionesMock);

        // Rango de fechas que no incluye ninguna transacción
        LocalDate desde = LocalDate.of(2023, 4, 1);
        LocalDate hasta = LocalDate.of(2023, 5, 1);

        List<Transaccion> resultado = servicioTransacciones.filtrarTransacciones(TipoTransaccion.COMPRA, 1L, desde, hasta);

        assertEquals(0, resultado.size());
    }

    @Test
    public void testSoloFechaDeInicio() {
        // Datos de prueba
        Transaccion trans1 = new Transaccion();
        trans1.setFechaDeTransaccion(LocalDate.of(2023, 1, 10));

        Transaccion trans2 = new Transaccion();
        trans2.setFechaDeTransaccion(LocalDate.of(2023, 2, 5));

        Transaccion trans3 = new Transaccion();
        trans3.setFechaDeTransaccion(LocalDate.of(2023, 3, 15));

        List<Transaccion> transaccionesMock = Arrays.asList(trans1, trans2, trans3);

        when(repositorioTransacciones.filtrarTransacciones(any(TipoTransaccion.class), anyLong()))
                .thenReturn(transaccionesMock);

        // Solo fecha de inicio
        LocalDate desde = LocalDate.of(2023, 2, 1);

        List<Transaccion> resultado = servicioTransacciones.filtrarTransacciones(TipoTransaccion.COMPRA, 1L, desde, null);

        assertEquals(2, resultado.size());
        assertEquals(LocalDate.of(2023, 2, 5), resultado.get(0).getFechaDeTransaccion());
        assertEquals(LocalDate.of(2023, 3, 15), resultado.get(1).getFechaDeTransaccion());
    }

    @Test
    public void testSoloFechaDeFin() {
        // Datos de prueba
        Transaccion trans1 = new Transaccion();
        trans1.setFechaDeTransaccion(LocalDate.of(2023, 1, 10));

        Transaccion trans2 = new Transaccion();
        trans2.setFechaDeTransaccion(LocalDate.of(2023, 2, 5));

        Transaccion trans3 = new Transaccion();
        trans3.setFechaDeTransaccion(LocalDate.of(2023, 3, 15));

        List<Transaccion> transaccionesMock = Arrays.asList(trans1, trans2, trans3);

        when(repositorioTransacciones.filtrarTransacciones(any(TipoTransaccion.class), anyLong()))
                .thenReturn(transaccionesMock);

        // Solo fecha de fin
        LocalDate hasta = LocalDate.of(2023, 2, 5);

        List<Transaccion> resultado = servicioTransacciones.filtrarTransacciones(TipoTransaccion.COMPRA, 1L, null, hasta);

        assertEquals(2, resultado.size());
        assertEquals(LocalDate.of(2023, 1, 10), resultado.get(0).getFechaDeTransaccion());
        assertEquals(LocalDate.of(2023, 2, 5), resultado.get(1).getFechaDeTransaccion());
    }

}
