package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoSeEncontroLaCriptomonedaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public List<String> obtenerNombreDeTodasLasCriptos() {
        return repositorioCriptomoneda.dameElNombreDeTodasLasCriptos();
    }

    @Override
    public Double obtenerPrecioDeCriptoPorNombre(String nombreDeCripto) {

        // aca hay q hacer lo de la API y devolver SOLO ese precio de la cripto
        // pero tengo q esperar a que lo haga julian por lo q le dijo la profe, asiq
        // por el momento dejo este return y cualquier cripto vale eso jaja
        return 100.0;
    }
}
