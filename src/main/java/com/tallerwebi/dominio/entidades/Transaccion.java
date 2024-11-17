package com.tallerwebi.dominio.entidades;

import javax.persistence.*;
import java.time.LocalDate;

import com.tallerwebi.dominio.enums.TipoTransaccion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Criptomoneda criptomoneda;
    @ManyToOne
    private Criptomoneda criptomoneda2;

    private Double montoTotal;
    private Double precioalQueSehizo;
    private Double precioalQueSehizo2;
    private LocalDate fechaDeTransaccion;
    private TipoTransaccion tipo;
    private Double cantidadDeCripto;
    private Double cantidadDeCripto2;
    private Boolean fueProgramada = false;

    public Transaccion() {
    }

    //metodos (agregue algunos)
    public Criptomoneda getCriptomoneda() {
        return criptomoneda;
    }
    public void setCriptomoneda(Criptomoneda criptomoneda) {
        this.criptomoneda = criptomoneda;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }
    public void setMontoTotal(Double monto) {
        this.montoTotal = monto;
    }


    public TipoTransaccion getTipo() {
        return tipo;
    }
    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }


    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public Double getPrecioAlQueSehizo() {
        return precioalQueSehizo;
    }
    public void setPrecioAlQueSehizo(Double precioAlQueSehizo) {
        this.precioalQueSehizo = precioAlQueSehizo;
    }


    public void setFechaDeTransaccion(LocalDate fechaDeTransaccion) {this.fechaDeTransaccion = fechaDeTransaccion;}
    public LocalDate getFechaDeTransaccion() {return this.fechaDeTransaccion;}

    public void setCantidadDeCripto(Double cantidadDeCripto) {this.cantidadDeCripto = cantidadDeCripto; }
    public Double getCantidadDeCripto() {return this.cantidadDeCripto;}


    public Criptomoneda getCriptomoneda2() {
        return criptomoneda2;
    }

    public void setCriptomoneda2(Criptomoneda criptomoneda2) {
        this.criptomoneda2 = criptomoneda2;
    }

    public Double getPrecioAlQueSehizo2() {
        return precioalQueSehizo2;
    }

    public void setPrecioAlQueSehizo2(Double precioalQueSehizo2) {
        this.precioalQueSehizo2 = precioalQueSehizo2;
    }

    public Double getCantidadDeCripto2() {
        return cantidadDeCripto2;
    }

    public void setCantidadDeCripto2(Double cantidadDeCripto2) {
        this.cantidadDeCripto2 = cantidadDeCripto2;
    }

    public Boolean getFueProgramada() {
        return fueProgramada;
    }

    public void setFueProgramada(Boolean fueProgramada) {
        this.fueProgramada = fueProgramada;
    }
}