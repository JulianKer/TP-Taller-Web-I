package com.tallerwebi.dominio;

import java.util.List;
import java.util.Map;

public interface ServicioHome {
    Map<String, Double> obtenerCrypto(List<String> criptos, String moneda);
    /*Double obtenerCrypto(String cripto, String moneda);*/
}
