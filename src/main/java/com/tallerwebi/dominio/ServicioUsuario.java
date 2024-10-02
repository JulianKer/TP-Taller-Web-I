package com.tallerwebi.dominio;

public interface ServicioUsuario {
    Usuario registrar(String mail, String pass, String nombre, String apellido, Long telefono, String fechaNacimiento);
}
