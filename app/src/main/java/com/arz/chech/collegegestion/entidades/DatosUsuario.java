package com.arz.chech.collegegestion.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DatosUsuario implements Parcelable, Serializable {
    private String nombre;
    private String apellido;
    private String rut;
    private String perfil;
    private String token;
    private boolean estaEliminado;
    private String imagenurl;

    public DatosUsuario(String nombre, String apellido, String rut, String perfil, boolean estaEliminado, String token) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.perfil = perfil;
        this.estaEliminado = estaEliminado;
        this.token = token;
    }

    public DatosUsuario(String nombre, String apellido, String rut, String perfil, String token, boolean estaEliminado, String imagenurl) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.perfil = perfil;
        this.token = token;
        this.estaEliminado = estaEliminado;
        this.imagenurl = imagenurl;
    }

    public DatosUsuario() {
    }


    protected DatosUsuario(Parcel in) {
        nombre = in.readString();
        apellido = in.readString();
        rut = in.readString();
        perfil = in.readString();
        token = in.readString();
        estaEliminado = in.readByte() != 0;
    }

    public static final Creator<DatosUsuario> CREATOR = new Creator<DatosUsuario>() {
        @Override
        public DatosUsuario createFromParcel(Parcel in) {
            return new DatosUsuario(in);
        }

        @Override
        public DatosUsuario[] newArray(int size) {
            return new DatosUsuario[size];
        }
    };

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
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

    public String getImagenurl() {
        return imagenurl;
    }

    public void setImagenurl(String imagenurl) {
        this.imagenurl = imagenurl;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(rut);
        dest.writeString(perfil);
        dest.writeString(token);
        dest.writeByte((byte) (estaEliminado ? 1 : 0));
    }
}
