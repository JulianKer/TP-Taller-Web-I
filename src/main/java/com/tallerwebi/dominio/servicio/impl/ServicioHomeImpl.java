package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.servicio.ServicioHome;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
@Transactional
public class ServicioHomeImpl implements ServicioHome {


    /*@Override
    public Map<String, Double> obtenerCrypto(List<String> criptos, String moneda) {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Double> precios = new HashMap<>();

        for (String cripto : criptos) {
            String url = "https://api.coincap.io/v2/assets/" + cripto                /*moneda.toUpperCase()/; // Convierte la moneda a mayúsculas

            try {
                // Realiza la solicitud y obtiene la respuesta en formato JSON
                String response = restTemplate.getForObject(url, String.class);
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                double precio = jsonResponse.getAsJsonObject("data").get("priceUsd").getAsDouble();


                precio = convertiPrecioSegunLaDivisa(moneda, precio);
                System.out.println(precio);

                // Formatea la clave con la primera letra en mayúscula
                String minusculas = cripto.toLowerCase();//Bitcoin
                String resultado = minusculas.substring(0, 1).toUpperCase() + minusculas.substring(1);

                precios.put(resultado, precio);
            } catch (Exception e) {
                e.printStackTrace();
                precios.put(cripto, 0.0); // Agrega null si hay un error
            }
        }

        return precios;
    }

    private static double convertiPrecioSegunLaDivisa(String moneda, double precio) {

        switch (moneda){
            case "EUR":
                precio = precio * 0.90;
                break;
            case "BRL":
                precio = precio * 5.45;
                break;
            case "GBP":
                precio = precio * 0.75;
                break;
            case "CNY":
                precio = precio * 7.0;
                break;
            case "ARS":
                precio = precio * 958.0;
                break;
        }
        return precio;
    }
*/








/*
    @Override
    public Double obtenerCrypto(String cryptoId, String moneda) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + cryptoId + "&vs_currencies=" + moneda;
        // hago solicitud a la api
        String response = restTemplate.getForObject(url, String.class);

        // proceso la respuesta JSON para obtener el valor del la crypto q pedí por parametro
        if (response != null) {
            // como la respuesta es asi: {"bitcoin":{"usd":64565.0}}
            //ver aca lo del "usd\"" -->  moneda+"\":" porque no viene siempre usd sino q puede venir otra monda
            String monedaDevuelta = moneda + "\":";
            // separo ese "string" quedandome con el valor nomas
            int startIndex = response.indexOf(monedaDevuelta) + monedaDevuelta.length();
            int endIndex = response.indexOf("}", startIndex);
            String price = response.substring(startIndex, endIndex);

            return Double.parseDouble(price);
        }

        // este lo pongo paradevolver un valor por defecto si la respuesta no es válida
        throw new RuntimeException("aa");
    }*/


}
