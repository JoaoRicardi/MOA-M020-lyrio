package com.example.lyrio.modules.musica.repository;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.lyrio.data.LyrioDatabase;
import com.example.lyrio.models.Musica;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class ListaMusicasRepository {

    //Escolher observable do reactiveX
    //Entre Flowable e Observable, Flowable entrega mais parâmetros
    public Flowable<Musica> getMusicaPorId (Context context, String idDaMusica){
        LyrioDatabase db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        return db.musicasFavoritasDao()
                .getMusicaPorId(idDaMusica);
    }

    public Flowable<List<Musica>> getAllMusicas (Context context){
        LyrioDatabase db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        return db.musicasFavoritasDao()
                .getMusicasFavoritas();
    }


    public Completable favoritarMusica(Musica musica, Context context){
        LyrioDatabase db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        // transformar de void para completable
        return Completable.fromAction(() -> db.musicasFavoritasDao()
                .inserir(musica));
    }

    public Completable removerMusica(Musica musica, Context context){
        LyrioDatabase db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        // transformar de void para completable
        return Completable.fromAction(() -> db.musicasFavoritasDao()
                .delete(musica));
    }


    public Completable removerMusicaPorId(String musicaId, Application context) {
        LyrioDatabase db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        // transformar de void para completable
        return Completable.fromAction(() -> db.musicasFavoritasDao()
                .deletePorId(musicaId));
    }
}
