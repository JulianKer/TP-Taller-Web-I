package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        return repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
    }
}
