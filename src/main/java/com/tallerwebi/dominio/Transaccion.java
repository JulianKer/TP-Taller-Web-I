package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import com.tallerwebi.dominio.Criptomoneda;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.enums.TipoTransaccion;

@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Criptomoneda criptomoneda;

    private Double montoTotal;
    private Double precioalQueSehizo;
    private LocalDate fechaDeTransaccion;
    private TipoTransaccion tipo;
    private Double cantidadDeCripto;

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
}