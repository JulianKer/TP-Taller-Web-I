package com.tallerwebi.dominio.servicio.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.PrecioCripto;
import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import com.tallerwebi.dominio.repositorio.RepositorioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.infraestructura.servicio.ServicioSubirImagen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ServicioCriptomonedaImpl implements ServicioCriptomoneda {


    private ServicioSubirImagen servicioSubirImagen;
    private RepositorioCriptomoneda repositorioCriptomoneda;

    @Autowired
    public ServicioCriptomonedaImpl(RepositorioCriptomoneda repositorioCriptomoneda, ServicioSubirImagen servicioSubirImagen) {
        this.repositorioCriptomoneda = repositorioCriptomoneda;
        this.servicioSubirImagen = servicioSubirImagen;
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

        // para este metodo voy a usar la logicq que habia hecho de pedir solo UNA cripto por peticion ya que el metodo
        // necesito que haga eso, no llamo al metoodo de esta clase (obtenerCrypto) pq me devuelve un map y no me sirve
        // asiq prefiero hacer la una peticion nueva y listo
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coincap.io/v2/assets/" + nombreDeCripto;
        Double precio = 0.0;

        try {
            // hago la peticion y tengo la respuesta en json
            String response = restTemplate.getForObject(url, String.class);
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            precio = jsonResponse.getAsJsonObject("data").get("priceUsd").getAsDouble(); // no lo convierto pq me innteresa en usd directamente
            return precio;
        }catch (Exception e) {
            e.printStackTrace();
            // aca podria hacer que te tire una excepcion pero supongo yo
            // q nunca falla pq son correctas las criptos q te paso, PERO puede ser
        }
        return precio;
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
            String id = objCripto.get("id").getAsString(); //bitcoin
            String name = objCripto.get("name").getAsString(); // Bitcoin
            String simbolo = objCripto.get("symbol").getAsString(); // BTC
            double precio = objCripto.get("priceUsd").getAsDouble(); // en usd

            for (Criptomoneda criptoDeMiBdd : misCriptos){

                if (id.equals(criptoDeMiBdd.getNombre())){
                    precio = convertiPrecioSegunLaDivisa(moneda, precio);
                    precios.put(criptoDeMiBdd, precio);

                    criptoDeMiBdd.setPrecioActual(precio);
                    criptoDeMiBdd.setNombre(id);
                    criptoDeMiBdd.setNombreConMayus(name);
                    criptoDeMiBdd.setSimbolo(simbolo);
                    repositorioCriptomoneda.actualizarCriptomoneda(criptoDeMiBdd); //este seeria el update

                    PrecioCripto precioCripto = new PrecioCripto();
                    LocalDateTime fechaDeHoy = LocalDateTime.now();
                    precioCripto.setCriptomoneda(criptoDeMiBdd);
                    precioCripto.setPrecioActual(precio);
                    precioCripto.setFechaDelPrecio(fechaDeHoy);
                    // aca faltaria guardarlo el preciocripto en la bdd
                }
            }
        }
        return precios;
    }

    @Override
    public double convertiPrecioSegunLaDivisa(String moneda, double precio) {

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

    @Override
    public boolean dameLaCriptoVerificandoSiEstaEnElPaginadoYAgregarla(String nombreRecibido) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.coincap.io/v2/assets?limit=20"; // aca esta lo del "paginado", osea, nose si seria un "paginado" sino q le pido solo la primeras 20 (no uso todas sino q filtro solo las q quiero)
        String response = restTemplate.getForObject(url, String.class);
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray arrayData = jsonResponse.getAsJsonArray("data");

        for (JsonElement criptoDelArrayApi : arrayData) {
            JsonObject objCripto = criptoDelArrayApi.getAsJsonObject();
            String id = objCripto.get("id").getAsString(); //bitcoin

            if (id.equals(nombreRecibido) && verificarQueNoTengaEsaCriptoEnMiBdd(nombreRecibido)){
                // las pongo aca asi no las estoy inicializando en cada iteracion
                String name = objCripto.get("name").getAsString(); // Bitcoin
                String simbolo = objCripto.get("symbol").getAsString(); // BTC
                double precio = objCripto.get("priceUsd").getAsDouble(); // en usd

                Criptomoneda criptoAAgregar = new Criptomoneda(); // cre0 la cripto y le seteo sus parametros menos la img
                criptoAAgregar.setPrecioActual(precio);
                criptoAAgregar.setNombre(id);
                criptoAAgregar.setNombreConMayus(name);
                criptoAAgregar.setSimbolo(simbolo);
                repositorioCriptomoneda.guardarCriptomoneda(criptoAAgregar);

                // y este para q se guarde su primer fluctuacion d precio :)
                PrecioCripto precioCripto = new PrecioCripto();
                LocalDateTime fechaDeHoy = LocalDateTime.now();
                precioCripto.setCriptomoneda(criptoAAgregar);
                precioCripto.setPrecioActual(precio);
                precioCripto.setFechaDelPrecio(fechaDeHoy);
                // aca faltaria guardarlo el precioCripto en la bdd
                return true; // si la encontre en el paginado, la puedo agregar
            }
        }
        return false; //sino, no.
    }

    @Override
    public boolean verificarQueNoTengaEsaCriptoEnMiBdd(String nombreRecibido) {
        return repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreRecibido) == null;
    }

    @Override
    public void actualizarCripto(Criptomoneda criptoAActualizar) {
        repositorioCriptomoneda.actualizarCriptomoneda(criptoAActualizar);
    }
}
