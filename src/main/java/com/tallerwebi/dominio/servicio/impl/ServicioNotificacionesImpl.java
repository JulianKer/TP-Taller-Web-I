package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioNotificaciones;
import com.tallerwebi.dominio.servicio.ServicioNotificaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ServicioNotificacionesImpl implements ServicioNotificaciones {

    RepositorioNotificaciones repositorioNotificaciones;

    @Autowired
    public ServicioNotificacionesImpl(RepositorioNotificaciones repositorioNotificaciones) {
        this.repositorioNotificaciones = repositorioNotificaciones;
    }

    @Override
    public void enviarNotificacion(String notifTitulo, String notifDescripcion, Usuario usuario) {
        Notificacion notificacionNueva = this.generarNotificacion(notifTitulo, notifDescripcion, usuario);
        repositorioNotificaciones.guardarNotificacion(notificacionNueva);
    }

    private Notificacion generarNotificacion(String notifTitulo, String notifDescripcion, Usuario usuario) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTitulo(notifTitulo);
        notificacion.setDescripcion(notifDescripcion);
        notificacion.setUsuario(usuario);
        notificacion.setFechaNotificacion(LocalDate.now());
        notificacion.setVista(false);
        return notificacion;
    }

    @Override
    public List<Notificacion> obtenerLasNotificacionesDelUsuario(Long id, String criterioDeOrdenamiento) {
        List<Notificacion> notificacionesOrdenadas;

        if (criterioDeOrdenamiento.isEmpty()){
            notificacionesOrdenadas = repositorioNotificaciones.obtenerLasNotificacionesDelUsuario(id);
        }else {
            if (criterioDeOrdenamiento.equals("antiguoAReciente")){
                notificacionesOrdenadas = repositorioNotificaciones.obtenerLasNotificacionesDelUsuarioOrdenadasDeAntiguoAReciente(id);
            }else {
                notificacionesOrdenadas = repositorioNotificaciones.obtenerLasNotificacionesDelUsuario(id);
            }
        }
        return notificacionesOrdenadas;
    }

    @Override
    public void marcarComoVistasLasNotificacionesDelUsuario(Long id) {
        List<Notificacion> todasLasNotifDelUser = this.obtenerLasNotificacionesDelUsuario(id, "");

        for (Notificacion notificacion : todasLasNotifDelUser) {
            notificacion.setVista(true);
            repositorioNotificaciones.actualizarNotificacion(notificacion);
        }
    }

    @Override
    public Boolean consultarSiHayNotificacionesSinVerParaEsteUsuario(Long id) {
        return repositorioNotificaciones.consultarSiHayNotificacionesSinVerParaEsteUsuario(id);
    }
}
