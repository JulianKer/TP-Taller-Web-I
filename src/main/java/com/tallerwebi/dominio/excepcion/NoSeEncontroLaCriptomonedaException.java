package com.tallerwebi.dominio.excepcion;

public class NoSeEncontroLaCriptomonedaException extends RuntimeException{
    public NoSeEncontroLaCriptomonedaException(String msj){
        super(msj);
    }
}
