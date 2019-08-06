package com.proyecto.ecohand.control_protesis.Models;

public class ItemMenu {

    private String titulo;
    private String subTitulo;
    private int iconoResource;

    public ItemMenu(String titulo, String subTitulo, int iconoResource) {
        this.titulo = titulo;
        this.subTitulo = subTitulo;
        this.iconoResource = iconoResource;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubTitulo() {
        return subTitulo;
    }

    public void setSubTitulo(String subTitulo) {
        this.subTitulo = subTitulo;
    }

    public int getIconoResource() {
        return iconoResource;
    }

    public void setIconoResource(int iconoResource) {
        this.iconoResource = iconoResource;
    }
}
