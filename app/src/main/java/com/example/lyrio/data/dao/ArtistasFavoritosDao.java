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
public interface ArtistasFavoritosDao {

//        @Query("select * from musica where id_da_musica = :idDaMusica")
//        Flowable<Musica> getMusicaPorId(String idDaMusica);
//
//        @Query("select * from musica")
//        Flowable<List<Musica>> getMusicasFavoritas();
//
//        @Insert
//        void inserir(Musica musica);
//
//        @Delete
//        void delete(Musica musica);
//
//        @Update
//        void update(Musica musica);
}