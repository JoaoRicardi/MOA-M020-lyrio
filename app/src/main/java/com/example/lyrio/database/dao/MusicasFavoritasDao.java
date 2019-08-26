package com.example.lyrio.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lyrio.modules.musica.model.Musica;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MusicasFavoritasDao {

    @Query("select * from musica where id_da_musica = :idDaMusica")
    Flowable<Musica> getMusicaPorId(String idDaMusica);

    @Query("select * from musica")
    Flowable<List<Musica>> getMusicasFavoritas();

    @Query("select * from musica where url_artista = :urlDoArtista")
    Flowable<List<Musica>> getAllMusicasDoArtista(String urlDoArtista);

    @Insert
    void inserir(Musica musica);

    @Delete
    void delete(Musica musica);

    @Query("delete from musica where id_da_musica = :idDaMusica")
    void deletePorId(String idDaMusica);

    @Update
    void update(Musica musica);

}
