package com.example.lyrio.database.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
//@Entity(foreignKeys = @ForeignKey(entity = ListaMusicasFavoritas.class,
//        parentColumns = "id",
//        childColumns = "lista_compras_id"))

@Entity
public class Musica implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int pk;

    @SerializedName("id")
    @ColumnInfo(name = "id_da_musica")
    private String id;

    @SerializedName("mus")
    @Ignore
    private String name;

    @SerializedName("desc")
    @Ignore
    private String desc;

    @SerializedName("url")
    @Ignore
    private String url;

    @SerializedName("albumPic")
    @Ignore
    private String albumPic;

    @SerializedName("lang")
    @Ignore
    private int lang;
    @SerializedName("text")
    @Ignore
    private String text;

    @SerializedName("translate")
    @Ignore
    private List<Musica> translate;

    @SerializedName("art")
    @Ignore
    private ApiArtista artista;

    @SerializedName("emailUsuario")
    @Ignore
    private String emailUsuario;

    @SerializedName("favoritarMusica")
    @ColumnInfo(name = "is_favorita")
    private boolean favoritarMusica = false;

    @SerializedName("musicasList")
    @Ignore
    private List<Musica> musicasList;

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

    public List<Musica> getTranslate() {
        return translate;
    }

    public void setTranslate(List<Musica> translate) {
        this.translate = translate;
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

    public List<Musica> getMusicasList() {
        return musicasList;
    }

    public void setMusicasList(List<Musica> musicasList) {
        this.musicasList = musicasList;
    }
}
