package com.arz.chech.collegegestion.entidades;

public class Messages {

    private String message, type;
    private long time;
    private boolean seen;
    private String from;
    private String receiver;

    public Messages(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Messages(String from, String receiver, String message, String type, long time, boolean seen) {
        this.from = from;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }

    public Messages(String from, String message, String type, long time, boolean seen) {
        this.from = from;
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }



    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Messages(){

    }
}
