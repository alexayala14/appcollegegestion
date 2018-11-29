package com.arz.chech.collegegestion.entidades;

public class DatosUsuario {
    private String nombre;
    private String apellido;
    private String rut;
    private String perfil;
    private String token;

    public DatosUsuario(String nombre, String apellido, String rut, String perfil, String token) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.perfil = perfil;
        this.token = token;
    }

    public DatosUsuario() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
