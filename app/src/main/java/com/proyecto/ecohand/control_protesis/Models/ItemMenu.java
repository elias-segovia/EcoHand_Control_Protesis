package com.proyecto.ecohand.control_protesis.Models;


public class ItemMenu {

    private String titulo;
    private String subTitulo;
    private String iconoRuta;

    public ItemMenu(String titulo, String subTitulo, String iconoRuta) {
        this.titulo = titulo;
        this.subTitulo = subTitulo;
        this.iconoRuta = iconoRuta;
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

    public String getIconoRuta() {
        return iconoRuta;
    }

    public void setIconoRuta(String iconoRuta) {
        this.iconoRuta = iconoRuta;
    }
}
