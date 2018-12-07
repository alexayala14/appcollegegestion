package com.arz.chech.collegegestion.entidades;

public class Publicacion{
    private String nombre;
    private String asunto;
    private String descripcion;
    private boolean estaEliminado;
    private long timestamp;
    private String tokenUser;
    private String tokenPubli;
    private int prioridad;

    public Publicacion(){
    }

    public Publicacion(String nombre, String asunto, String descripcion, int prioridad, long time, boolean estaEliminado, String tokenUser, String tokenPubli) {
        this.nombre = nombre;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.timestamp = time;
        this.estaEliminado = estaEliminado;
        this.tokenUser = tokenUser;
        this.tokenPubli = tokenPubli;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public String getTokenPubli() {
        return tokenPubli;
    }

    public void setTokenPubli(String tokenPubli) {
        this.tokenPubli = tokenPubli;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
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
}
