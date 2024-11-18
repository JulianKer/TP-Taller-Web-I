package com.tallerwebi.dominio.servicio.impl;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.repositorio.RepositorioBilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.servicio.ServicioBilleteraUsuarioCriptomoneda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public List<BilleteraUsuarioCriptomoneda> obtenerPortfolioDelUsuario(Long id) {
        return repositorioBilleteraUsuarioCriptomoneda.obtenerPortfolioDelUsuario(id);
    }

    @Override
    public List<BilleteraUsuarioCriptomoneda> obtenerPortfolioDelUsuarioOrdenado(Long id, String criterio) {
        List<BilleteraUsuarioCriptomoneda> portfolio = repositorioBilleteraUsuarioCriptomoneda.obtenerPortfolioDelUsuario(id);

        if (criterio == null || criterio.isEmpty()) {
            return portfolio; // Si no se proporciona un criterio, devolvemos la lista sin orden
        }

        switch (criterio) {
            case "precioAsc":
                // Ordenar por precio ascendente (de menor a mayor)
                portfolio.sort((b1, b2) -> Double.compare(b1.getCriptomoneda().getPrecioActual(), b2.getCriptomoneda().getPrecioActual()));
                break;
            case "precioDesc":
                // Ordenar por precio descendente (de mayor a menor)
                portfolio.sort((b1, b2) -> Double.compare(b2.getCriptomoneda().getPrecioActual(), b1.getCriptomoneda().getPrecioActual()));
                break;
            default:
                // Si no hay criterio válido, devolvemos la lista original
                break;
        }

        return portfolio;
    }

}
