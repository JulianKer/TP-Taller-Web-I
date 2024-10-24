package com.tallerwebi.dominio.excepcion;

public class UsuarioExistente extends RuntimeException {

    public UsuarioExistente(String msj) {
        super(msj);
    }
}

