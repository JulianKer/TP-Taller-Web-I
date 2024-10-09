package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.Usuario;

public interface ServicioUsuario {
    Usuario buscarUsuarioPorEmail(String email);
    Usuario registrar(String mail, String pass, String nombre, String apellido, Long telefono, String fechaNacimiento);

    void restarSaldo(Long idUsuario, Double precioTotalDeTransaccion);

    void cambiarEstado(Long id, boolean b);

    boolean verificarQueTengaSaldoSuficienteParaComprar(double v, Double saldo);

    void sumarSaldo(Long id, Double precioTotalDeTransaccion);
}
