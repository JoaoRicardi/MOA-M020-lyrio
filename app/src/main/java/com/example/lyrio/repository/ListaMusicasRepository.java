package com.example.lyrio.repository;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.service.RetrofitService;
import com.example.lyrio.service.model.ApiArtista;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class ListaMusicasRepository {

    private RetrofitService retrofitService = new RetrofitService();

    private static final String API_KEY = UUID.randomUUID()+"";
    private static final String FORMAT = "json";

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

    public Observable<Musica> getMusicaPorIdApi(String musicaId){
        return retrofitService.getMusicasApi()
                .getMusicasById(API_KEY, musicaId)
                .map(vagalumeBusca -> {
                    Musica musica = new Musica();

                    musica.setName(vagalumeBusca.getMus().get(0).getName());

                    ApiArtista apiArtista = new ApiArtista();
                    apiArtista.setName(vagalumeBusca.getArt().getName());
                    apiArtista.setUrl(vagalumeBusca.getArt().getUrl()+"images/profile.jpg");
                    musica.setArtista(apiArtista);

                    musica.setText(vagalumeBusca.getMus().get(0).getText());

                    return musica;
                });
    }

}
