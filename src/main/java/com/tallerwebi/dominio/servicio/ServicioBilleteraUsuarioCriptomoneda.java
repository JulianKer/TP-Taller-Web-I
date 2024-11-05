package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.List;

public interface ServicioBilleteraUsuarioCriptomoneda {
    BilleteraUsuarioCriptomoneda buscarBilleteraCriptoUsuario(Criptomoneda criptomoneda, Usuario usuario);

    void guardarBilletera(BilleteraUsuarioCriptomoneda billeteraNueva);

    void actualizarBilletera(BilleteraUsuarioCriptomoneda billetera);

    boolean verificarQueTengaLaCantidaddeCriptosSuficientesParaVender(BilleteraUsuarioCriptomoneda billetera, Double cantidadDeCripto);

    boolean verificarQueTengaLaCantidaddeCriptosSuficientesParaIntercambiar(BilleteraUsuarioCriptomoneda billeteraCriptoADar, Double cantidadDeCriptoADar);

    List<BilleteraUsuarioCriptomoneda> obtenerPortfolioDelUsuario(Long id);
}
