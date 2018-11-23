package com.arz.chech.collegegestion.entidades;

public class Publicacion {
    private String nombre;
    private String asunto;
    private String descripcion;
    private int foto;

    public Publicacion(){
    }

    public Publicacion(String nombre, String asunto, String descripcion, int foto) {
        this.nombre = nombre;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
