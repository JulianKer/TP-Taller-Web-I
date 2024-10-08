package com.tallerwebi.dominio.servicio.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ServicioCriptomonedaImpl implements ServicioCriptomoneda {


    private RepositorioCriptomoneda repositorioCriptomoneda;

    @Autowired
    public ServicioCriptomonedaImpl(RepositorioCriptomoneda repositorioCriptomoneda) {
        this.repositorioCriptomoneda = repositorioCriptomoneda;
    }

    @Override
    public Criptomoneda buscarCriptomonedaPorNombre(String nombreDeCripto) {
        Criptomoneda criptomonedaEncontrada = repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
        if (criptomonedaEncontrada == null){
            throw new NoSeEncontroLaCriptomonedaException("Criptomoneda no encontrada");
        }
        return criptomonedaEncontrada;
    }

    @Override
    public ArrayList<Criptomoneda> obtenerNombreDeTodasLasCriptos() {
        return repositorioCriptomoneda.dameElNombreDeTodasLasCriptos();
    }

    @Override
    public Double obtenerPrecioDeCriptoPorNombre(String nombreDeCripto) {

        // aca hay q hacer lo de la API y devolver SOLO ese precio de la cripto
        // pero tengo q esperar a que lo haga julian por lo q le dijo la profe, asiq
        // por el momento dejo este return y cualquier cripto vale eso jaja
        return 100.0;
    }

    @Override
    public Map<Criptomoneda, Double> obtenerCrypto(ArrayList<Criptomoneda> misCriptos, String moneda) {

        RestTemplate restTemplate = new RestTemplate();
        Map<Criptomoneda, Double> precios = new HashMap<>();

        String url = "https://api.coincap.io/v2/assets?limit=20"; // aca esta lo del "paginado", osea, nose si seria un "paginado" sino q le pido solo la primeras 20 (no uso todas sino q filtro solo las q quiero)
        String response = restTemplate.getForObject(url, String.class);
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray arrayData = jsonResponse.getAsJsonArray("data");

        for (JsonElement criptoDelArrayApi : arrayData) {
            JsonObject objCripto = criptoDelArrayApi.getAsJsonObject();
            String id = objCripto.get("id").getAsString();
            String name = objCripto.get("name").getAsString();
            double precio = objCripto.get("priceUsd").getAsDouble();
            System.out.println(id);
            System.out.println(precio);

            for (Criptomoneda criptoDeMiBdd : misCriptos){
                System.out.println(id);
                System.out.println(criptoDeMiBdd.getNombre());
                System.out.println(id.equals(criptoDeMiBdd.getNombre()));

                if (id.equals(criptoDeMiBdd.getNombre())){
                    precio = convertiPrecioSegunLaDivisa(moneda, precio);
                    precios.put(criptoDeMiBdd, precio);
                }
            }
        }



        /*for (String cripto : misCriptos) {
             url = "https://api.coincap.io/v2/assets/" + cripto                /*moneda.toUpperCase()/; // Convierte la moneda a mayúsculas

            try {
                // Realiza la solicitud y obtiene la respuesta en formato JSON
                 response = restTemplate.getForObject(url, String.class);
                 //jsonResponse = JsonParser.parseString(response).getAsJsonObject();
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
        }*/

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
}
