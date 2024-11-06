package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Suscripcion;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ServicioSuscripcion {
    String verificarEstadoDelPago(HttpServletRequest request, String status, String payment_id, String payment_type);

    String obtenerTipoDePago(String payment_type);

    List<Suscripcion> obtenerSuscripciones();
}
