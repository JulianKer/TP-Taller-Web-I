package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioPortfolio;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
public class ServicioPortfolioImpl implements ServicioPortfolio {


    @Override
    public Double obtenerTotalDeLaCuenta(List<BilleteraUsuarioCriptomoneda> portfolio) {

        Double total = 0.0;

        for (BilleteraUsuarioCriptomoneda billetera : portfolio) {
            total += billetera.getCantidadDeCripto()* billetera.getCriptomoneda().getPrecioActual();
        }

        return total;
    }
}
