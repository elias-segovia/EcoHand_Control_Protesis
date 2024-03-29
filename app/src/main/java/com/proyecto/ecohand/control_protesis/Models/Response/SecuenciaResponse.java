package com.proyecto.ecohand.control_protesis.Models.Response;

public class SecuenciaResponse {

    private int id;
    private String nombre;
    private String descripcion;
    private String codigoEjecutable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoEjecutable() {
        return codigoEjecutable;
    }

    public void setCodigoEjecutable(String codigoEjecutable) {
        this.codigoEjecutable = codigoEjecutable;
    }

}
