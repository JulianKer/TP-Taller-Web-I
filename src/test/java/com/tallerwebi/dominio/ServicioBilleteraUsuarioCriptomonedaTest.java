package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.impl.ServicioBilleteraUsuarioCriptomonedaImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioBilleteraUsuarioCriptomonedaTest {

    private RepositorioBilleteraUsuarioCriptomoneda repositorioBilleteraUsuarioCriptomoneda = mock(RepositorioBilleteraUsuarioCriptomoneda.class);
    private ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda = new ServicioBilleteraUsuarioCriptomonedaImpl(repositorioBilleteraUsuarioCriptomoneda);

    @Test
    public void queSePuedaBuscarUnaBilletera() {
        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        Criptomoneda criptomoneda = new Criptomoneda();
        Usuario usuario = new Usuario();

        when(repositorioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario)).thenReturn(billetera);
        assertNotNull(repositorioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario));
    }

    @Test
    public void queNoSePuedaBuscarUnaBilletera() {
        Criptomoneda criptomoneda = new Criptomoneda();
        Usuario usuario = new Usuario();

        when(repositorioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario)).thenReturn(null);
        assertNull(repositorioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario));
    }

    @Test
    public void queSeVerifiqueQueTengaLaCantidaddeCriptosSuficientesParaVender() {
        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setCantidadDeCripto(5.0);
        Double cantidadDeCripto = 4.0;

        assertTrue(servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaVender( billetera, cantidadDeCripto));
    }

    @Test
    public void queSeVerifiqueQueNOTengaLaCantidaddeCriptosSuficientesParaIntercambiar() {
        BilleteraUsuarioCriptomoneda billetera = new BilleteraUsuarioCriptomoneda();
        billetera.setCantidadDeCripto(2.0);
        Double cantidadDeCripto = 6.0;

        assertFalse(servicioBilleteraUsuarioCriptomoneda.verificarQueTengaLaCantidaddeCriptosSuficientesParaIntercambiar( billetera, cantidadDeCripto));
    }
}
