package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioBilleteraUsuarioCriptomonedaImpl implements ServicioBilleteraUsuarioCriptomoneda {

    private RepositorioBilleteraUsuarioCriptomoneda repositorioBilleteraUsuarioCriptomoneda;

    @Autowired
    public ServicioBilleteraUsuarioCriptomonedaImpl(RepositorioBilleteraUsuarioCriptomoneda repositorioBilleteraUsuarioCriptomoneda){
        this.repositorioBilleteraUsuarioCriptomoneda = repositorioBilleteraUsuarioCriptomoneda;
    }

    @Override
    public BilleteraUsuarioCriptomoneda buscarBilleteraCriptoUsuario(Criptomoneda criptomoneda, Usuario usuario) {
        return repositorioBilleteraUsuarioCriptomoneda.buscarBilleteraCriptoUsuario(criptomoneda, usuario);
    }

    @Override
    public void guardarBilletera(BilleteraUsuarioCriptomoneda billeteraNueva) {
        repositorioBilleteraUsuarioCriptomoneda.guardarBilletera(billeteraNueva);
    }

    @Override
    public void actualizarBilletera(BilleteraUsuarioCriptomoneda billetera) {
        repositorioBilleteraUsuarioCriptomoneda.actualizarBilletera(billetera);
    }

    @Override
    public boolean verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(BilleteraUsuarioCriptomoneda billetera, Double cantidadDeCripto) {
        return billetera.getCantidadDeCripto() >= cantidadDeCripto;
    }

    @Override
    public boolean verificarQueTengaLaCantidaddeCriptosSuficientesParaIntercambiar(BilleteraUsuarioCriptomoneda billeteraCriptoADar, Double cantidadDeCriptoADar) {
        return billeteraCriptoADar.getCantidadDeCripto() >= cantidadDeCriptoADar;
    }
}
