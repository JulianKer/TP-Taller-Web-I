package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.TipoTransaccion;


public interface ServicioTransacciones {

    String crearTransaccion(String nombreDeCripto, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, String emailUsuario);
}
