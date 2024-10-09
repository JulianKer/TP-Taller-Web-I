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

import javax.servlet.http.HttpServletRequest;
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
            @RequestParam(value = "criterioDeBusqueda",required = false, defaultValue = "") String criterioDeBusqueda,
            HttpServletRequest request) {

        ModelMap model = new ModelMap();
        Map<Criptomoneda, Double> mapaMonedaPrecios = new HashMap<>();
        moneda = moneda.toUpperCase();

        ArrayList<Criptomoneda> misCriptos = servicioCriptomoneda.obtenerNombreDeTodasLasCriptos();

        if (criterioDeBusqueda.isEmpty()){
            mapaMonedaPrecios = servicioCriptomoneda.obtenerCrypto(misCriptos, moneda);
        }else{
            ArrayList<Criptomoneda> coincidenciasDeBusqueda = new ArrayList<>();
            for (Criptomoneda cripto : misCriptos) {
                if (cripto.getNombre().contains(criterioDeBusqueda)) {
                    coincidenciasDeBusqueda.add(cripto);
                }
            }

            model.addAttribute("criterioDeBusqueda", criterioDeBusqueda);

            if (coincidenciasDeBusqueda.isEmpty()) {
                mapaMonedaPrecios = servicioCriptomoneda.obtenerCrypto(misCriptos, moneda);
                model.addAttribute("msjNoHayCoincidencias", "No se encontraron coincidencias para: \"" + criterioDeBusqueda + "\"");
            } else {
                mapaMonedaPrecios = servicioCriptomoneda.obtenerCrypto(coincidenciasDeBusqueda, moneda);
            }
        }

        model.addAttribute("mapaMonedaPrecios", mapaMonedaPrecios);
        model.addAttribute("divisaAMostrar", moneda);
        return new ModelAndView("home", model);
    }


}
