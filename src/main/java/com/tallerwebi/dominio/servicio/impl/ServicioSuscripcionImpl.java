package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicio.ServicioSuscripcion;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.infraestructura.servicio.impl.ServicioEmail;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioSuscripcionImpl implements ServicioSuscripcion {

    private ServicioUsuario servicioUsuario;
    private ServicioEmail servicioEmail;

    public ServicioSuscripcionImpl(ServicioUsuario servicioUsuario, ServicioEmail servicioEmail) {
        this.servicioUsuario = servicioUsuario;
        this.servicioEmail = servicioEmail;
    }

    @Override
    public ModelAndView verificarEstadoDelPago(HttpServletRequest request, String status, String payment_id, String payment_type) {
        switch (status){
            case "approved":
                String emailABuscar = request.getSession().getAttribute("emailUsuario").toString();
                Usuario userEncontrado = servicioUsuario.buscarUsuarioPorEmail(emailABuscar);
                servicioUsuario.cambiarEstado(userEncontrado.getId(), true);

                String tipoDePago = obtenerTipoDePago(payment_type);
                servicioEmail.enviarEmail(userEncontrado.getEmail(), "Suscripcion | Crypto", servicioEmail.dameElMensajeParaSuscripcion(userEncontrado, "Aprobado", payment_id, tipoDePago));
                return new ModelAndView("redirect:/suscripcion?mensaje=Suscripcion exitosa.");
            case "pending":
                return new ModelAndView("redirect:/suscripcion?mensaje=Estamos esperando que se realize el pago.");

            case "failure":
                return new ModelAndView("redirect:/suscripcion?mensaje=Hubo un error al intentar procesar el pago.");
            default:
                return new ModelAndView("redirect:/suscripcion");
        }
    }

    @Override
    public String obtenerTipoDePago(String payment_type) {
        String medioDePago = "Medio de pago desconocido.";
        switch (payment_type){
            case "credit_card":
                medioDePago = "Tarjeta de Credito.";
                break;
                case "debit_card":
                    medioDePago = "Tarjeta de Debito.";
                    break;
                    case "account_money":
                        medioDePago = "Cuenta de Mercado Pago.";
                        break;
        }
        return medioDePago;
    }
}
