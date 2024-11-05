package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidades.BilleteraUsuarioCriptomoneda;

import java.util.List;

public interface ServicioPortfolio {
   Double obtenerTotalDeLaCuenta(List<BilleteraUsuarioCriptomoneda> portfolio);
}
