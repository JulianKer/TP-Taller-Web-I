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
import java.util.Set;
import java.util.stream.Collectors;

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

        List<Criptomoneda> restantes = new ArrayList<>();

        List<Criptomoneda> criptos = servicioCriptomoneda.obtenerCriptosHabilitadas();

        if (portfolioDelUsuario.isEmpty()) {
            restantes.addAll(criptos);
            return restantes;
        }

        Set<Long> idsEnPortfolio = portfolioDelUsuario.stream()
                .map(billetera -> billetera.getCriptomoneda().getId())
                .collect(Collectors.toSet());

        restantes = criptos.stream()
                .filter(cripto -> !idsEnPortfolio.contains(cripto.getId()))
                .collect(Collectors.toList());

        return restantes;
    }

}
