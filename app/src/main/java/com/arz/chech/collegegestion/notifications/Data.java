package com.arz.chech.collegegestion.notifications;

import com.arz.chech.collegegestion.entidades.DatosUsuario;

import java.util.ArrayList;

public class Data {
    private String user;
    private int icon;
    private String body;
    private String title;
    private String sented;
    private String bandera;
    private String nombregrupo;
    private ArrayList<DatosUsuario>datosUsuarios;


    public Data(String user, int icon, String body, String title, String sented, String bandera) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.bandera = bandera;
    }



    public Data(String username, int ic_launcher, String s, String appCollegeGestion, String banderaNot){

    }

    public Data(String user, int icon, String body, String title, String sented, String bandera, String nombregrupo) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.bandera = bandera;
        this.nombregrupo = nombregrupo;
    }

    public Data(String user, int icon, String body, String title, String sented, String bandera, String nombregrupo, ArrayList<DatosUsuario> datosUsuarios) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.bandera = bandera;
        this.nombregrupo = nombregrupo;
        this.datosUsuarios = datosUsuarios;
    }


    public String getBandera() {
        return bandera;
    }

    public void setBandera(String bandera) {
        this.bandera = bandera;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public String getNombregrupo() {
        return nombregrupo;
    }

    public void setNombregrupo(String nombregrupo) {
        this.nombregrupo = nombregrupo;
    }

    public ArrayList<DatosUsuario> getDatosUsuarios() {
        return datosUsuarios;
    }

    public void setDatosUsuarios(ArrayList<DatosUsuario> datosUsuarios) {
        this.datosUsuarios = datosUsuarios;
    }

    @Override
    public String toString() {
        return "Data{" +
                "user='" + user + '\'' +
                ", icon=" + icon +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", sented='" + sented + '\'' +
                ", bandera='" + bandera + '\'' +
                ", nombregrupo='" + nombregrupo + '\'' +
                ", datosUsuarios=" + datosUsuarios +
                '}';
    }
}