package com.tallerwebi.dominio.entidades;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

    @Entity
    public class AlertaPrecio {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        public void setCriptomoneda(Criptomoneda criptomoneda) {
            this.criptomoneda = criptomoneda;
        }

        public void setId(Long id) {
            this.id = id;
        }


        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public Criptomoneda getCriptomoneda() {
            return criptomoneda;
        }

        public Long getId() {
            return id;
        }

        public void setPrecioAlerta(Double precioAlerta) {
            this.precioAlerta = precioAlerta;
        }

        public Double getPrecioAlerta() {
            return precioAlerta;
        }


        @ManyToOne
        private Usuario usuario;

        @ManyToOne
        private Criptomoneda criptomoneda;

        private Double precioAlerta;

        public boolean verificarAlerta(Double precioActual) {
            return precioActual <= this.precioAlerta;
        }
    }

