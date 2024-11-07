package com.tallerwebi.dominio.entidades;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PrecioCripto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Criptomoneda criptomoneda;
    private Double precioActual;
    private String fechaDelPrecio;

    public String getFechaDelPrecio() {return fechaDelPrecio;}
    public void setFechaDelPrecio(String fechaDelPrecio) {this.fechaDelPrecio = fechaDelPrecio;}

    public Criptomoneda getCriptomoneda() {return criptomoneda;}
    public void setCriptomoneda(Criptomoneda criptomoneda) {this.criptomoneda = criptomoneda;}

    public Double getPrecioActual() {return precioActual;}
    public void setPrecioActual(Double precioActual) {this.precioActual = precioActual;}

    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}
}
