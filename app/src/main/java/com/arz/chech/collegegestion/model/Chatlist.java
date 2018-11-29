package com.arz.chech.collegegestion.model;

public class Chatlist {
    private String id;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Chatlist(String id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;

    }

    public Chatlist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}