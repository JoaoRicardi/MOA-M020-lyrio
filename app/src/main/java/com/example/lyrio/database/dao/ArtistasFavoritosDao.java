package com.example.lyrio.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lyrio.service.model.ApiArtista;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ArtistasFavoritosDao {

        @Query("select * from apiartista where id_do_artista = :idDoArtista")
        Flowable<ApiArtista> getArtistaPorId(String idDoArtista);

        @Query("select * from apiartista")
        Flowable<List<ApiArtista>> getArtistasFavoritos();

        @Insert
        void inserir(ApiArtista artista);

        @Delete
        void delete(ApiArtista artista);

        @Query("delete from apiartista where id_do_artista = :idDoArtista")
        void deletePorId(String idDoArtista);

        @Update
        void update(ApiArtista artista);
}
