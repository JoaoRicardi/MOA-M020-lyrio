package com.example.lyrio.service.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.lyrio.database.models.Musica;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
@Entity
public class ApiArtista implements Serializable{
    @PrimaryKey(autoGenerate = true)
    private int pk;

    @SerializedName("name")
    @Ignore
    private String name;

    @SerializedName("id")
    @ColumnInfo(name = "id_do_artista")
    private String id;

    @SerializedName("desc")
    @Ignore
    private String desc;

    @SerializedName("url")
    @Ignore
    private String url;

    @SerializedName("pic_small")
    @Ignore
    private String pic_small;

    @SerializedName("pic_medium")
    @Ignore
    private String pic_medium;

    @Ignore
    private int qtdMusicas;

    @SerializedName("toplyrics")
    @Ignore
    private TopLyrics toplyrics;

    @SerializedName("lyrics")
    @Ignore
    private ApiItem lyrics;

    @Ignore
    private List<Musica> musicasSalvas;

    @ColumnInfo(name = "is_favorito")
    private boolean favoritarArtista;

    @Ignore
    private List<ApiArtista> apiArtistaList;


    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getPic_medium() {
        return pic_medium;
    }

    public void setPic_medium(String pic_medium) {
        this.pic_medium = pic_medium;
    }

    public int getQtdMusicas() {
        return qtdMusicas;
    }

    public void setQtdMusicas(int qtdMusicas) {
        this.qtdMusicas = qtdMusicas;
    }

    public TopLyrics getToplyrics() {
        return toplyrics;
    }

    public void setToplyrics(TopLyrics toplyrics) {
        this.toplyrics = toplyrics;
    }

    public ApiItem getLyrics() {
        return lyrics;
    }

    public void setLyrics(ApiItem lyrics) {
        this.lyrics = lyrics;
    }

    public List<Musica> getMusicasSalvas() {
        return musicasSalvas;
    }

    public void setMusicasSalvas(List<Musica> musicasSalvas) {
        this.musicasSalvas = musicasSalvas;
    }

    public boolean isFavoritarArtista() {
        return favoritarArtista;
    }

    public void setFavoritarArtista(boolean favoritarArtista) {
        this.favoritarArtista = favoritarArtista;
    }

    public List<ApiArtista> getApiArtistaList() {
        return apiArtistaList;
    }

    public void setApiArtistaList(List<ApiArtista> apiArtistaList) {
        this.apiArtistaList = apiArtistaList;
    }
}
