package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioUsuario {
    Usuario registrar(String mail, String pass);
}
