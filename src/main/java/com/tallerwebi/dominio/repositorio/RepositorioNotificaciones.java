package com.tallerwebi.dominio.repositorio;

import com.tallerwebi.dominio.entidades.Notificacion;

import java.util.List;

public interface RepositorioNotificaciones {
    void guardarNotificacion(Notificacion notificacionNueva);

    List<Notificacion> obtenerLasNotificacionesDelUsuario(Long id);

    void actualizarNotificacion(Notificacion notificacionAActualizar);

    Boolean consultarSiHayNotificacionesSinVerParaEsteUsuario(Long id);

    List<Notificacion> obtenerLasNotificacionesDelUsuarioOrdenadasDeAntiguoAReciente(Long id);
}
