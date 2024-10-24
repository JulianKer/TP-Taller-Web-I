package com.tallerwebi.dominio.entidades;

import javax.persistence.*;

@Entity
public class BilleteraUsuarioCriptomoneda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Criptomoneda criptomoneda;
    @ManyToOne
    private Usuario usuario;
    private Double cantidadDeCripto;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Criptomoneda getCriptomoneda() {
        return criptomoneda;
    }

    public void setCriptomoneda(Criptomoneda criptomoneda) {
        this.criptomoneda = criptomoneda;
    }

    public Double getCantidadDeCripto() {
        return cantidadDeCripto;
    }

    public void setCantidadDeCripto(Double cantidad) {
        this.cantidadDeCripto = cantidad;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void incrementarCantidadDeCripto(Double cantidadDeCripto) {
        Double cantASetear = getCantidadDeCripto() + cantidadDeCripto;
        setCantidadDeCripto(cantASetear);
    }

    public void decrementarCantidadDeCripto(Double cantidadDeCripto) {
        Double cantASetear = getCantidadDeCripto() - cantidadDeCripto;
        setCantidadDeCripto(cantASetear);
    }
}
