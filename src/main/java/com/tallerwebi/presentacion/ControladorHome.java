package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioHome;
import com.tallerwebi.dominio.servicio.impl.ServicioCriptomonedaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class ControladorHome {

    private ServicioCriptomoneda servicioCriptomoneda;

    @Autowired
    public ControladorHome(ServicioCriptomoneda servicioCriptomoneda){
        this.servicioCriptomoneda = servicioCriptomoneda;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView cargarPrecioDeCryptos(
            @RequestParam(value = "moneda",required = false, defaultValue = "usd") String moneda,
            @RequestParam(value = "criterioDeBusqueda",required = false, defaultValue = "") String criterioDeBusqueda) {

        ModelMap model = new ModelMap();
        Map<Criptomoneda, Double> mapaMonedaPrecios = new HashMap<>();
        moneda = moneda.toUpperCase();

        ArrayList<Criptomoneda> misCriptos = servicioCriptomoneda.obtenerNombreDeTodasLasCriptos();
        /*
        misCriptos.add("bitcoin");
        misCriptos.add("ethereum");
        misCriptos.add("tether");
        misCriptos.add("solana");
        misCriptos.add("steth");
        misCriptos.add("dogecoin");
        misCriptos.add("binance-coin");
        */

        if (criterioDeBusqueda.isEmpty()){
            mapaMonedaPrecios = servicioCriptomoneda.obtenerCrypto(misCriptos, moneda);
        }else{
            ArrayList<Criptomoneda> coincidenciasDeBusqueda = new ArrayList<>();
            for (Criptomoneda cripto : misCriptos) {
                if (cripto.getNombre().contains(criterioDeBusqueda)) {
                    coincidenciasDeBusqueda.add(cripto);
                }
            }
            mapaMonedaPrecios = servicioCriptomoneda.obtenerCrypto(coincidenciasDeBusqueda, moneda);
        }



/* ESTE CODIGO LO USE PARA LAS CONSULTAS A LA API DE COINGECKO PERO TENIA MUY POCAS REQUEST Y ME TIRABA ERROE MUY SEGUIDO ASIQ LA CAMBIÉ
PERO LA DEJO POR LAS DUDAS SI EN ALGUN MOMENTO LA LLEGO A USAR, LA TENGO ACA


        if (criterioDeBusqueda.isEmpty()){
            for(String cripto : misCriptos) {
                String minusculas = cripto.toLowerCase();
                String resultado = minusculas.substring(0, 1).toUpperCase() + minusculas.substring(1);
                // esto seria un metodo en servicio donde alla creo un map metiendo lo q hago aca, pasandole el array de strings
                // y aca solo hago: mapaMonedaPrecios = servicio.dameUnMapaConTodasLasCriptos(misCriptos);
                mapaMonedaPrecios.put(resultado, servicioHome.obtenerCrypto(cripto, moneda));
            }
        }else{
            ArrayList<String> coincidenciasDeBusqueda = new ArrayList<>();
            for (String cripto : misCriptos) {
                if (cripto.contains(criterioDeBusqueda)) {
                    coincidenciasDeBusqueda.add(cripto);
                }
            }

            for(String coincidencia : coincidenciasDeBusqueda) {
                String minusculas = coincidencia.toLowerCase();
                String resultado = minusculas.substring(0, 1).toUpperCase() + minusculas.substring(1);
                // esto seria un metodo en servicio donde alla creo un map metiendo lo q hago aca, pasandole el array de strings
                // y aca solo hago: mapaMonedaPrecios = servicio.dameUnMapaConTodasLasCriptos(misCriptos);
                mapaMonedaPrecios.put(resultado, servicioHome.obtenerCrypto(coincidencia, moneda));
            }
        }
        mapaMonedaPrecios.put("Ethereum", servicioHome.obtenerCrypto("ethereum", moneda));
        mapaMonedaPrecios.put("Bitcoin", servicioHome.obtenerCrypto("bitcoin", moneda));
        */

        model.addAttribute("mapaMonedaPrecios", mapaMonedaPrecios);
        model.addAttribute("divisaAMostrar", moneda);
        return new ModelAndView("home", model);
    }


}
