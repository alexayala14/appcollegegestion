package com.arz.chech.collegegestion.entidades;

public class DatosUsuario {
    private String nombre;
    private String apellido;
    private String rut;
    private String password;
    private String perfil;


    public DatosUsuario(String nombre, String apellido, String rut, String perfil, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.password = password;
        this.perfil = perfil;
    }

    public DatosUsuario() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
