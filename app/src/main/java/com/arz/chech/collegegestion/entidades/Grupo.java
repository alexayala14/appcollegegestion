package com.arz.chech.collegegestion.entidades;

public class Grupo {

    private String nombre;
    private String token;
    private String message;
    private String members;

    public Grupo(String nombre, String token, String message, String members) {
        this.nombre = nombre;
        this.token = token;
        this.message = message;
        this.members = members;
    }

    public Grupo(String nombre) {

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }



    @Override
    public String toString() {
        return "Grupo{" +
                "nombre='" + nombre + '\'' +
                ", token='" + token + '\'' +
                ", message='" + message + '\'' +
                ", members='" + members + '\'' +
                '}';
    }
}
