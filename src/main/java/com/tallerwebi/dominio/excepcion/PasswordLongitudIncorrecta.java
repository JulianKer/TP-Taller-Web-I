package com.tallerwebi.dominio.excepcion;

public class PasswordLongitudIncorrecta extends RuntimeException {
  public PasswordLongitudIncorrecta(String message) {
    super(message);
  }
}
