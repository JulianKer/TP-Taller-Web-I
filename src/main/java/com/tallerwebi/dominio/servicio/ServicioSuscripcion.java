package com.tallerwebi.dominio.servicio;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public interface ServicioSuscripcion {
    ModelAndView verificarEstadoDelPago(HttpServletRequest request, String status, String payment_id, String payment_type);

    String obtenerTipoDePago(String payment_type);
}
