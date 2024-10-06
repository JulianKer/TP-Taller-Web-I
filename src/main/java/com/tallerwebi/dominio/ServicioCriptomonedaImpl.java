package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
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
        Criptomoneda criptomonedaEncontrada = repositorioCriptomoneda.buscarCriptomonedaPorNombre(nombreDeCripto);
        if (criptomonedaEncontrada == null){
            throw new NoSeEncontroLaCriptomonedaException("Criptomoneda no encontrada");
        }
        return criptomonedaEncontrada;
    }
}
