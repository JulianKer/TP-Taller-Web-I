package com.tallerwebi.dominio.entidades;

import com.tallerwebi.dominio.enums.TipoTransaccion;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class TransaccionProgramada extends Transaccion{

    private LocalDate fechaQueSeHizoLaProgramacion;
    private String condicionParaHacerla;
    private Double precioACumplir;

    public TransaccionProgramada() {
        super();
    }

    public TransaccionProgramada(Usuario usuario, Criptomoneda criptomoneda, Criptomoneda criptomoneda2,
                                 Double total, Double precioAlQueSeHizo, Double precioAlQueSeHizo2,
                                 LocalDate fecha, TipoTransaccion tipoTransaccion, Double cantidadDeCripto, Double cantidadDEcripto2,
                                 LocalDate fechaQueSeHizoLaProgramacion, String condicionParaHacerla, Double precioACumplir){
        super();
        setUsuario(usuario);
        setCriptomoneda(criptomoneda);
        setCriptomoneda2(criptomoneda2);
        setMontoTotal(total);
        setPrecioAlQueSehizo(precioAlQueSeHizo);
        setPrecioAlQueSehizo2(precioAlQueSeHizo2);
        setFechaDeTransaccion(fecha);
        setTipo(tipoTransaccion);
        setCantidadDeCripto(cantidadDeCripto);
        setCantidadDeCripto2(cantidadDEcripto2);

        this.fechaQueSeHizoLaProgramacion = fechaQueSeHizoLaProgramacion;
        this.condicionParaHacerla = condicionParaHacerla;
        this.precioACumplir = precioACumplir;
    }

    public LocalDate getFechaQueSeHizoLaProgramacion() {
        return fechaQueSeHizoLaProgramacion;
    }

    public void setFechaQueSeHizoLaProgramacion(LocalDate fechaQueSeHizoLaProgramacion) {
        this.fechaQueSeHizoLaProgramacion = fechaQueSeHizoLaProgramacion;
    }

    public Double getPrecioACumplir() {
        return precioACumplir;
    }

    public void setPrecioACumplir(Double precioACumplir) {
        this.precioACumplir = precioACumplir;
    }

    public String getCondicionParaHacerla() {
        return condicionParaHacerla;
    }

    public void setCondicionParaHacerla(String condicionParaHacerla) {
        this.condicionParaHacerla = condicionParaHacerla;
    }
}