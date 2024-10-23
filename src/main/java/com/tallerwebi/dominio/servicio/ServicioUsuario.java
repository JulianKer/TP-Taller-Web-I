package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public interface ServicioUsuario {
    Usuario buscarUsuarioPorEmail(String email);
    Usuario registrar(String mail, String pass, String nombre, String apellido, Long telefono, String fechaNacimiento);

    void restarSaldo(Long idUsuario, Double precioTotalDeTransaccion);

    void cambiarEstado(Long id, boolean b);

    boolean verificarQueTengaSaldoSuficienteParaComprar(double v, Double saldo);

    void sumarSaldo(Long id, Double precioTotalDeTransaccion);

    ArrayList<Usuario> obtenerUnaListaDeTodosLosUsuariosNoAdmins();

    String bloquearUsuario(Long idUsuario);

    String desbloquearUsuario(Long idUsuario);

    List<Usuario> filtrarUsuarioPorBusqueda(List<Usuario> misUsuarios, String busquedaUsuario);
}
