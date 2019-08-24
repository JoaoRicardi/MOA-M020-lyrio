package com.example.lyrio.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.database.dao.ArtistasFavoritosDao;
import com.example.lyrio.database.dao.MusicasFavoritasDao;
import com.example.lyrio.database.models.Musica;

@Database(entities = {Musica.class, ApiArtista.class}, version = 1, exportSchema = false)
public abstract class LyrioDatabase extends RoomDatabase {

    public static String DATABASE_NAME = "lyriodb";

    public abstract MusicasFavoritasDao musicasFavoritasDao();
    public abstract ArtistasFavoritosDao artistasFavoritosDao();


}
