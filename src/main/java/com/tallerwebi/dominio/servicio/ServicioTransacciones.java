package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;

import java.util.List;


public interface ServicioTransacciones {

    Double dameLaCantidadQueEsteUsuarioTieneDeEstaCripto(Usuario usuario, String idCriptomoneda);

    List<Transaccion> obtenerHistorialTransaccionesDeUsuario(Long idDeUsuario);

    Transaccion generarTransaccion(Double precioDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario, Double precioTotalDeTransaccion, Criptomoneda criptoEncontrada, Double cantidadDeCripto);

    Boolean verificarQueTengaSaldoSuficienteParaComprar(Double precioTotalDeTransaccion, Double saldo);

    String crearTransaccion(Criptomoneda criptomoneda, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, Usuario usuario);

    Boolean verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(String nombreDeCripto, Double cantidadDeCripto, Long id);
}
