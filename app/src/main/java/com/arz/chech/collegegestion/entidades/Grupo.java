package com.arz.chech.collegegestion.entidades;

public class Grupo {

    private String nombre;
    private String token;
    private boolean estaEliminado;

    public Grupo(String nombre, String token, boolean estaEliminado) {
        this.nombre = nombre;
        this.token = token;
        this.estaEliminado = estaEliminado;
    }
    public Grupo(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}
