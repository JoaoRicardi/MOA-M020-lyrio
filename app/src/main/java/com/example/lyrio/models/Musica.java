package com.example.lyrio.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.lyrio.api.base_vagalume.ApiArtista;

import java.io.Serializable;
//@Entity(foreignKeys = @ForeignKey(entity = ListaMusicasFavoritas.class,
//        parentColumns = "id",
//        childColumns = "lista_compras_id"))

@Entity
public class Musica implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int pk;

    @ColumnInfo(name = "id_da_musica")
    private String id;
    @Ignore
    private String name;
    @Ignore
    private String desc;
    @Ignore
    private String url;
    @Ignore
    private String albumPic;
    @Ignore
    private int lang;
    @Ignore
    private String text;
    @Ignore
    private String trans;
    @Ignore
    private ApiArtista artista;
    @Ignore
    private String emailUsuario;
    @ColumnInfo(name = "is_favorita")
    private boolean favoritarMusica;

    public Musica() {
    }

    public Musica(String id, String desc, String url) {
        this.id = id;
        this.desc = desc;
        this.url = url;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public ApiArtista getArtista() {
        return artista;
    }

    public void setArtista(ApiArtista artista) {
        this.artista = artista;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public boolean isFavoritarMusica() {
        return favoritarMusica;
    }

    public void setFavoritarMusica(boolean favoritarMusica) {
        this.favoritarMusica = favoritarMusica;
    }
}
