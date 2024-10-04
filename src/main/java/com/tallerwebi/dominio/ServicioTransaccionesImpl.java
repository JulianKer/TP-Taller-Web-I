package com.tallerwebi.dominio;

import com.tallerwebi.dominio.enums.TipoTransaccion;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioTransaccionesImpl implements ServicioTransacciones {

    @Override
    public String crearTransaccion(String nombreDeCripto, Double precioDeCripto, Double cantidadDeCripto, TipoTransaccion tipoDeTransaccion, String emailUsuario) {

        if (cantidadDeCripto <= 0.0){
            return "La cantidad debe ser mayor que 0.";
        }



        return "Transaccion exitosa.";
    }
}
