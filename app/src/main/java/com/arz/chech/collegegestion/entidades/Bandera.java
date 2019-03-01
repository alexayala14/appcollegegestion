package com.arz.chech.collegegestion.entidades;

public class Bandera {
    private Boolean banderaChat=false;
    private Boolean banderaPubli;

    public Bandera(Boolean banderaChat, Boolean banderaPubli) {
        this.banderaChat = banderaChat;
        this.banderaPubli = banderaPubli;
    }
    public Bandera(){

    }

    public Boolean getBanderaChat() {
        return banderaChat;
    }

    public void setBanderaChat(Boolean banderaChat) {
        this.banderaChat = banderaChat;
    }

    public Boolean getBanderaPubli() {
        return banderaPubli;
    }

    public void setBanderaPubli(Boolean banderaPubli) {
        this.banderaPubli = banderaPubli;
    }
}
