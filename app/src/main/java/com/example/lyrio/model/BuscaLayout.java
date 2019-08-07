package com.example.lyrio.model;

public class BuscaLayout {

    private boolean artistaEncontrado;
    private boolean musicaEncontrado;

    public BuscaLayout(boolean artistaEncontrado, boolean musicaEncontrado) {
        this.artistaEncontrado = artistaEncontrado;
        this.musicaEncontrado = musicaEncontrado;
    }

    public boolean isArtistaEncontrado() {
        return artistaEncontrado;
    }

    public void setArtistaEncontrado(boolean artistaEncontrado) {
        this.artistaEncontrado = artistaEncontrado;
    }

    public boolean isMusicaEncontrado() {
        return musicaEncontrado;
    }

    public void setMusicaEncontrado(boolean musicaEncontrado) {
        this.musicaEncontrado = musicaEncontrado;
    }
}
