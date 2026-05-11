package com.connectwork.proyecto2.models;

public class Propuesta {
    private int idPropuesta;
    private int idProyecto;
    private int idFreelancer;
    private double montoOfertado;
    private int plazoDias;
    private String cartaPresentacion;
    private String estado; // PENDIENTE, ACEPTADA, RECHAZADA
    private String nombreFreelancer; // Para mostrar el nombre del freelancer en la lista de propuestas
    private double calificacionFreelancer;

    public Propuesta() {}

    public Propuesta(int idProyecto, int idFreelancer, double montoOfertado, int plazoDias, String cartaPresentacion, String estado, String nombreFreelancer, double calificacionFreelancer) {
        this.idProyecto = idProyecto;
        this.idFreelancer = idFreelancer;
        this.montoOfertado = montoOfertado;
        this.plazoDias = plazoDias;
        this.cartaPresentacion = cartaPresentacion;
        this.estado = estado;
        this.nombreFreelancer = nombreFreelancer;
        this.calificacionFreelancer = calificacionFreelancer;
    }

    public int getIdPropuesta() { return idPropuesta; }
    public void setIdPropuesta(int idPropuesta) { this.idPropuesta = idPropuesta; }

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public int getIdFreelancer() { return idFreelancer; }
    public void setIdFreelancer(int idFreelancer) { this.idFreelancer = idFreelancer; }

    public double getMontoOfertado() { return montoOfertado; }
    public void setMontoOfertado(double montoOfertado) { this.montoOfertado = montoOfertado; }

    public int getPlazoDias() { return plazoDias; }
    public void setPlazoDias(int plazoDias) { this.plazoDias = plazoDias; }

    public String getCartaPresentacion() { return cartaPresentacion; }
    public void setCartaPresentacion(String cartaPresentacion) { this.cartaPresentacion = cartaPresentacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreFreelancer() {
        return nombreFreelancer;
    }

    public void setNombreFreelancer(String nombreFreelancer) {
        this.nombreFreelancer = nombreFreelancer;
    }

    public double getCalificacionFreelancer() {
        return calificacionFreelancer;
    }

    public void setCalificacionFreelancer(double calificacionFreelancer) {
        this.calificacionFreelancer = calificacionFreelancer;
    }
}
