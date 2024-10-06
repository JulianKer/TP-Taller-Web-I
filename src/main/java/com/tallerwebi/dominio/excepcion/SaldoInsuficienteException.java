package com.tallerwebi.dominio.excepcion;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String msj) {
        super(msj);
    }
}
