package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioNotificaciones {
    void enviarNotificacion(String notifTitulo, String notifDescripcion, Usuario usuario);

    List<Notificacion> obtenerLasNotificacionesDelUsuario(Long id, String criterioDeOrdenamiento);

    void marcarComoVistasLasNotificacionesDelUsuario(Long id);

    Boolean consultarSiHayNotificacionesSinVerParaEsteUsuario(Long id);
}
