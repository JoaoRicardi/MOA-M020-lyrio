package com.example.lyrio.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.lyrio.data.dao.ArtistasFavoritosDao;
import com.example.lyrio.data.dao.MusicasFavoritasDao;
import com.example.lyrio.data.dao.NoticiaFavoritasDao;
import com.example.lyrio.data.dao.UserDao;
import com.example.lyrio.models.ArtistaSalvo;
import com.example.lyrio.models.Musica;
import com.example.lyrio.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class LyrioDatabase extends RoomDatabase {

    public static String DATABASE_NAME = "LyrioApp";


    public abstract UserDao userDao();

}
