package com.arz.chech.collegegestion.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Grupo implements Serializable {

    private String name;
    private String  groupId;
    private List<DatosUsuario> members;
    private List<Messages> messages ;
    private String imagenurl;
    private String admin;

    public Grupo(){

    }



    public Grupo(String name, String groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    public Grupo(String name, String groupId, List<DatosUsuario> members, List<Messages> messages, String imagenurl, String admin) {
        this.name = name;
        this.groupId = groupId;
        this.members = members;
        this.messages = messages;
        this.imagenurl = imagenurl;
        this.admin = admin;
    }

    public Grupo(String name, String groupId, List<DatosUsuario> members, List<Messages> messages) {
        this.name = name;
        this.groupId = groupId;
        this.members = members;
        this.messages = messages;
    }

    public Grupo(String name, String groupId, List<DatosUsuario> members, List<Messages> messages, String imagenurl) {
        this.name = name;
        this.groupId = groupId;
        this.members = members;
        this.messages = messages;
        this.imagenurl = imagenurl;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getImagenurl() {
        return imagenurl;
    }

    public void setImagenurl(String imagenurl) {
        this.imagenurl = imagenurl;
    }


    public List<DatosUsuario> getMembers() {
        return members;
    }

    public void setMembers(List<DatosUsuario> members) {
        this.members = members;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }




    @Override
    public String toString() {
        return "Grupo{" +
                "name='" + name + '\'' +
                ", groupId='" + groupId + '\'' +
                ", members=" + members +
                ", messages=" + messages +
                '}';
    }
}
