package com.example.lyrio.service.model;

import com.example.lyrio.database.models.Musica;

import java.util.List;

public class TopLyrics {

    private List<Musica> item;

    public List<Musica> getItem() {
        return item;
    }

    public void setItem(List<Musica> item) {
        this.item = item;
    }
}
