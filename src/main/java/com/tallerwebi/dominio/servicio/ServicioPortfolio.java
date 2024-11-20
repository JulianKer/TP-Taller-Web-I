package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;
import com.tallerwebi.dominio.entidades.Criptomoneda;

import java.util.List;

public interface ServicioPortfolio {
   Double obtenerTotalDeLaCuenta(List<BilleteraUsuarioCriptomoneda> portfolio);

   List<Criptomoneda> obtenerCriptosRestantes(List<BilleteraUsuarioCriptomoneda> portfolioDelUsuario);
}
