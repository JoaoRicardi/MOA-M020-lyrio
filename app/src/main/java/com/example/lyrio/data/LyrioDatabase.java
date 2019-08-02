package com.example.lyrio.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.lyrio.data.dao.MusicasFavoritasDao;
import com.example.lyrio.models.Musica;

@Database(entities = {Musica.class}, version = 1, exportSchema = false)
public abstract class LyrioDatabase extends RoomDatabase {

    public static String DATABASE_NAME = "lyriodb";

    public abstract MusicasFavoritasDao musicasFavoritasDao();


}
