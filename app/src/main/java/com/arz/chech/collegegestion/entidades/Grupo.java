package com.arz.chech.collegegestion.entidades;

import java.util.ArrayList;
import java.util.List;

public class Grupo {

    private String name;
    private String  groupId;
    private List<DatosUsuario> members;
    private List<Messages> messages ;

    public Grupo(){

    }



    public Grupo(String name, String groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    public Grupo(String name, String groupId, List<DatosUsuario> members, List<Messages> messages) {
        this.name = name;
        this.groupId = groupId;
        this.members = members;
        this.messages = messages;
    }


    public Grupo(List<DatosUsuario> members) {
        this.members = members;
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
