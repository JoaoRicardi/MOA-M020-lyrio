package com.example.lyrio.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.lyrio.data.dao.MusicasFavoritasDao;

import java.util.List;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "nome")
    private String nome;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "senha")
    private String senha;
    @ColumnInfo(name = "notifica")
    private boolean notifica;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isNotifica() {
        return notifica;
    }

    public void setNotifica(boolean notifica) {
        this.notifica = notifica;
    }
//private List<Musica> musicasFavoritasList;

    //private List<ArtistaSalvo> artistaSalvoList;

    //private List<NoticiaSalva>noticiaSalvaList;




}
