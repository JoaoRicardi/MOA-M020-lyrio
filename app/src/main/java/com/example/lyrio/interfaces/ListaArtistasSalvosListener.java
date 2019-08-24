package com.example.lyrio.interfaces;

import com.example.lyrio.modules.Artista.model.ApiArtista;

public interface ListaArtistasSalvosListener {
    void abrirPaginaDoArtista(ApiArtista artistaSalvo);
    void desfavoritarArtista(ApiArtista artistaSalvo);
}
