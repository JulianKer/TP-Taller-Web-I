package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.TipoTransaccion;
import com.tallerwebi.dominio.excepcion.CriptomonedasInsuficientesException;
import com.tallerwebi.dominio.excepcion.SaldoInsuficienteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioTransaccionesTest {

    private RepositorioTransacciones repositorioTransacciones = mock(RepositorioTransacciones.class);
    private ServicioTransacciones servicioTransacciones = new ServicioTransaccionesImpl(repositorioTransacciones);


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

        //when(servicioTransacciones.buscarCriptoPorNombre(nombreDeCripto)).thenReturn(cripto);

        assertThrows(SaldoInsuficienteException.class,()->servicioTransacciones.crearTransaccion(cripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario));
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


//        when(servicioTransacciones.buscarCriptoPorNombre(nombreDeCripto)).thenReturn(cripto);
        assertEquals("Transaccion exitosa.", servicioTransacciones.crearTransaccion(cripto,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario));
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

//        when(repositorioTransacciones.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);

        when(repositorioTransacciones.buscarCantidadCompradadeUnaCriptoDeUnUsuario(nombreDeCripto, usuario.getId())).thenReturn(1.0);
        when(repositorioTransacciones.buscarCantidadVendidadeUnaCriptoDeUnUsuario(nombreDeCripto, usuario.getId())).thenReturn(1.0);


        assertThrows(CriptomonedasInsuficientesException.class,()->servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario));
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

//        when(repositorioTransacciones.buscarCriptomonedaPorNombre(nombreDeCripto)).thenReturn(criptomoneda);

        when(repositorioTransacciones.buscarCantidadCompradadeUnaCriptoDeUnUsuario(nombreDeCripto, usuario.getId())).thenReturn(3.0);
        when(repositorioTransacciones.buscarCantidadVendidadeUnaCriptoDeUnUsuario(nombreDeCripto, usuario.getId())).thenReturn(0.0);

        assertEquals("Transaccion exitosa.",servicioTransacciones.crearTransaccion(criptomoneda,precioDeCripto,cantidadDeCripto,tipoDeTransaccion,usuario));

    }
}
