package com.example.lyrio.data.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lyrio.models.Musica;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MusicasFavoritasDao {

    // Room ou Retrofi2
    @Query("SELECT * FROM musica")
    Flowable<List<Musica>> getAll();

    @Insert
    void inserir(Musica... musica);

    @Delete
    void delete(Musica  musica);

    @Update
    void update(Musica musica);


}
