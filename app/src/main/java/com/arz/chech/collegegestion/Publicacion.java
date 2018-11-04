package com.arz.chech.collegegestion;

public class Publicacion {
    private String nombre;
    private String descripcion;
    private String detalle;
    private int foto;

    public Publicacion(){

    }
    public Publicacion(String nombre, String descripcion,String detalle,int foto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.detalle=detalle;
        this.foto = foto;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
