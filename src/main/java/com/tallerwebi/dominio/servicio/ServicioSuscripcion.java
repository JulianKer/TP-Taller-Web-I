package com.tallerwebi.dominio.servicio;

import javax.servlet.http.HttpServletRequest;

public interface ServicioSuscripcion {
    String verificarEstadoDelPago(HttpServletRequest request, String status, String payment_id, String payment_type);

    String obtenerTipoDePago(String payment_type);
}
