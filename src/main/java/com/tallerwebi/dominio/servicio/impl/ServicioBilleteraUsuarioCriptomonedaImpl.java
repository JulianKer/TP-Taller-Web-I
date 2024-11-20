package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ServicioBilleteraUsuarioCriptomonedaImpl implements ServicioBilleteraUsuarioCriptomoneda {

    private final RepositorioBilleteraUsuarioCriptomoneda repositorioBilleteraUsuarioCriptomoneda;

    @Autowired
    public ServicioBilleteraUsuarioCriptomonedaImpl(RepositorioBilleteraUsuarioCriptomoneda repositorioBilleteraUsuarioCriptomoneda) {
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

    @Override
    public List<BilleteraUsuarioCriptomoneda> obtenerPortfolioDelUsuario(Long id, Boolean ignorarCriptos, String criterio) {  //si no tiene billeteras devuelve vacio, si tiene billeteras depende de la decision del usario si las filtra o no
        List<BilleteraUsuarioCriptomoneda> listaObtenida= repositorioBilleteraUsuarioCriptomoneda.obtenerPortfolioDelUsuario(id);
        List<BilleteraUsuarioCriptomoneda>listaDevolver= new ArrayList<BilleteraUsuarioCriptomoneda>();

        if(!listaObtenida.isEmpty()){
            //listaObtenida= this.obtenerPortfolioDelUsuarioOrdenado( listaObtenida, criterio);
            if(ignorarCriptos){
                for (BilleteraUsuarioCriptomoneda billetera : listaObtenida) {
                    if(billetera.getCantidadDeCripto() > 0 ){
                        listaDevolver.add(billetera);
                    }
                }
                listaDevolver= this.obtenerPortfolioDelUsuarioOrdenado( listaDevolver, criterio);
            }else{
                listaObtenida= this.obtenerPortfolioDelUsuarioOrdenado( listaObtenida, criterio);

                return listaObtenida;
            }
        }else{
            return listaObtenida;
        }
        return listaDevolver;
    }

    @Override
    public List<BilleteraUsuarioCriptomoneda> obtenerPortfolioDelUsuarioOrdenado(List<BilleteraUsuarioCriptomoneda> portfolio, String criterio) {
        if (criterio == null || criterio.isEmpty()) {
            return portfolio;
        }

        switch (criterio) {
            case "precioAsc":
                portfolio.sort(Comparator.comparingDouble(b ->
                        b.getCantidadDeCripto() * b.getCriptomoneda().getPrecioActual()));
                break;
            case "precioDesc":
                portfolio.sort((b1, b2) -> Double.compare(
                        b2.getCantidadDeCripto() * b2.getCriptomoneda().getPrecioActual(),
                        b1.getCantidadDeCripto() * b1.getCriptomoneda().getPrecioActual()));
                break;
            default:
                break;
        }

        return portfolio;
    }

}
