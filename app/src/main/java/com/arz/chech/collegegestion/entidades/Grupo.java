package com.arz.chech.collegegestion.entidades;

public class Grupo {

    private String name;
    private String  groupId;

    public Grupo(String name) {
        this.name = name;
    }
    public Grupo(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grupo(String name, String groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "name='" + name + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
