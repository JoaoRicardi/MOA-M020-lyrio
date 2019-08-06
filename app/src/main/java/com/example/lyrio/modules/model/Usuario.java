package com.example.lyrio.modules.model;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class Usuario {

    @NonNull
    private String email;
    private String senha;

    public Usuario(){

    }

    public Usuario(@NonNull String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
