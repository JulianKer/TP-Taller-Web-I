package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.PrecioCripto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ServicioCriptomoneda {

    Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto);

    ArrayList<Criptomoneda> obtenerNombreDeTodasLasCriptos();

    Double obtenerPrecioDeCriptoPorNombre(String nombreDeCripto);

    Map<Criptomoneda, Double> obtenerCrypto(ArrayList<Criptomoneda> misCriptos, String moneda);

    boolean dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla(String nombreRecibido);

    double convertiPrecioSegunLaDivisa(String moneda, double precio);

    boolean verificarQueNoTengaEsaCriptoEnMiBdd(String nombreRecibido);

    void actualizarCripto(Criptomoneda criptoAgregada);

    Boolean inhabilitarCriptomoneda(String idCriptomoneda);

    Boolean habilitarCriptomoneda(String idCriptomoneda);

    ArrayList<Criptomoneda> obtenerCriptosHabilitadas();

    List<PrecioCripto> obtenerHistorialDePrecioCriptoDeEstaCripto(String idCriptomoneda);
}
