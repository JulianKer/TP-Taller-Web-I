package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.TipoTransaccion;


public interface ServicioTransacciones {


    Transaccion generarTransaccion(Double precioDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Criptomoneda criptoEncontrada, Double cantidadDeCripto);

//    Criptomoneda buscarCriptoPorNombre(String nombreDeCripto);

    Boolean verificarQueTengaSaldoSuficienteParaComprar(Double precioTotalDeTransaccion, Double saldo);

    String crearTransaccion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario);

    Boolean verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(String nombreDeCripto, Double cantidadDeCripto, Long id);
}
