package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioPortfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class ServicioPortfolioImpl implements ServicioPortfolio {
    private ServicioCriptomoneda servicioCriptomoneda;
    private ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda;

    @Autowired
    public ServicioPortfolioImpl(ServicioCriptomoneda servicioCriptomoneda, ServicioBilleteraUsuarioCriptomoneda servicioBilleteraUsuarioCriptomoneda) {
        this.servicioCriptomoneda = servicioCriptomoneda;
        this.servicioBilleteraUsuarioCriptomoneda = servicioBilleteraUsuarioCriptomoneda;
    } ;


    @Override
    public Double obtenerTotalDeLaCuenta(List<BilleteraUsuarioCriptomoneda> portfolio) {

        Double total = 0.0;

        for (BilleteraUsuarioCriptomoneda billetera : portfolio) {
            total += billetera.getCantidadDeCripto()* billetera.getCriptomoneda().getPrecioActual();
        }

        return total;
    }

    @Override
    public List<Criptomoneda> obtenerCriptosRestantes(List<BilleteraUsuarioCriptomoneda> portfolioDelUsuario) {

        List<Criptomoneda> restantes= new ArrayList<Criptomoneda>();

        List<Criptomoneda> criptos = servicioCriptomoneda.obtenerCriptosHabilitadas();
        if(!portfolioDelUsuario.isEmpty()){
            for (BilleteraUsuarioCriptomoneda billetera : portfolioDelUsuario) {
                for (Criptomoneda cripto : criptos) {
                    if(!billetera.getCriptomoneda().getId().equals(cripto.getId())) {
                        restantes.add(cripto);
                    }
                }
            }
        }else{
            restantes.addAll(criptos);
        }

        return restantes;
    }
}
