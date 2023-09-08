package com.example.filafx.controller;

public class SenhaAtendente {
    private String senha;
    private String guiche;

    public SenhaAtendente(String senha, String guiche){
        this.guiche = guiche;
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getGuiche() {
        return guiche;
    }

    public void setGuiche(String guiche) {
        this.guiche = guiche;
    }
}
