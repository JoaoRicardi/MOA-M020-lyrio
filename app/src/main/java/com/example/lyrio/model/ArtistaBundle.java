package com.example.lyrio.model;

import com.example.lyrio.service.model.ApiArtista;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtistaBundle implements Serializable {

    private ArrayList<ApiArtista> listaDeApiArtista;

    public ArrayList<ApiArtista> getListaDeApiArtista() {
        return listaDeApiArtista;
    }

    public void setListaDeApiArtista(ArrayList<ApiArtista> listaDeApiArtista) {
        this.listaDeApiArtista = listaDeApiArtista;
    }
}
