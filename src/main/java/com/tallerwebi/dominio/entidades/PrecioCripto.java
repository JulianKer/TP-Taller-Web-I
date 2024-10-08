package com.tallerwebi.dominio.entidades;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class PrecioCripto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Criptomoneda criptomoneda;
    private Double precioActual;
    private LocalDateTime fechaDelPrecio;

    public LocalDateTime getFechaDelPrecio() {return fechaDelPrecio;}
    public void setFechaDelPrecio(LocalDateTime fechaDelPrecio) {this.fechaDelPrecio = fechaDelPrecio;}

    public Criptomoneda getCriptomoneda() {return criptomoneda;}
    public void setCriptomoneda(Criptomoneda criptomoneda) {this.criptomoneda = criptomoneda;}

    public Double getPrecioActual() {return precioActual;}
    public void setPrecioActual(Double precioActual) {this.precioActual = precioActual;}

    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}
}
