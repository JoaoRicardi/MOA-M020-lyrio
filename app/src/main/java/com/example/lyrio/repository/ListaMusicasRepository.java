package com.example.lyrio.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.service.RetrofitService;
import com.example.lyrio.modules.Artista.model.ApiArtista;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class ListaMusicasRepository {

    private LyrioDatabase db;

    private static final String TAG = "VAGALUME";
    private RetrofitService retrofitService = new RetrofitService();

    private static final String API_KEY = UUID.randomUUID()+"";
    private static final String FORMAT = "json";

    Date curTime = Calendar.getInstance().getTime();
    String vagaKey = curTime.toString().replace(" ","");

    //Escolher observable do reactiveX
    //Entre Flowable e Observable, Flowable entrega mais parâmetros
    public Flowable<Musica> getMusicaPorId (Context context, String idDaMusica){
        db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        return db.musicasFavoritasDao()
                .getMusicaPorId(idDaMusica);
    }

    public Flowable<List<Musica>> getAllMusicas (Context context){
        db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();
//        Log.i(TAG, " Tentativa de atualizar lista de músicas no repository");
        return db.musicasFavoritasDao()
                .getMusicasFavoritas();
    }

    public Flowable<List<Musica>> getAllMusicasDoArtista (Context context, String urlDoArtista){
        db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();
        Log.i(TAG, " Buscando musicas do artista: "+urlDoArtista);
        return db.musicasFavoritasDao()
                .getAllMusicasDoArtista(urlDoArtista);
    }

    public Completable favoritarMusica(Musica musica, Context context){
        db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();
//        Log.i(TAG, " Música "+musica.getId()+" - tentativa de favoritar no repository");

        // transformar de void para completable
        return Completable.fromAction(() -> db.musicasFavoritasDao()
                .inserir(musica));
    }

    public Completable removerMusica(Musica musica, Context context){
        db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        // transformar de void para completable
        return Completable.fromAction(() -> db.musicasFavoritasDao()
                .delete(musica));
    }


    public Completable removerMusicaPorId(String musicaId, Application context) {
        db = Room.databaseBuilder(context, LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();
        Log.i(TAG, " Música "+musicaId+" - tentativa de remoção no repository");

        // transformar de void para completable
        return Completable.fromAction(() -> db.musicasFavoritasDao()
                .deletePorId(musicaId));
    }

    public Observable<Musica> getMusicaPorIdApi(String musicaId){
        Log.i(TAG, " Buscando no Vagalume a música com id: "+musicaId);
        return retrofitService.getMusicasApi()
                .getMusicasById(vagaKey, musicaId)
                .map(vagalumeBusca -> {
                    Musica musica = new Musica();

                    musica.setName(vagalumeBusca.getMus().get(0).getName());
                    musica.setId(vagalumeBusca.getMus().get(0).getId());

                    ApiArtista apiArtista = new ApiArtista();
                    apiArtista.setName(vagalumeBusca.getArt().getName());
//                    Log.i(TAG, "API GOT URL -> "+vagalumeBusca.getArt().getUrl());
                    apiArtista.setUrl(vagalumeBusca.getArt().getUrl()+"images/profile.jpg");
                    musica.setArtista(apiArtista);
                    musica.setText(vagalumeBusca.getMus().get(0).getText());
                    musica.setTranslate(vagalumeBusca.getMus().get(0).getTranslate());

                    return musica;
                });
    }

    public Observable<Musica> getMusicaPorIdApiV2(String musicaId, String increaseNum){
        Log.i(TAG, " Buscando no Vagalume a música com id: "+musicaId);
        return retrofitService.getMusicasApi()
                .getMusicasById(increaseNum+curTime.toString().replace(" ",""), musicaId)
                .map(vagalumeBusca -> {
                    Musica musica = new Musica();

                    musica.setName(vagalumeBusca.getMus().get(0).getName());
                    musica.setId(vagalumeBusca.getMus().get(0).getId());

                    ApiArtista apiArtista = new ApiArtista();
                    apiArtista.setName(vagalumeBusca.getArt().getName());
                    apiArtista.setUrl(vagalumeBusca.getArt().getUrl()+"images/profile.jpg");
                    musica.setArtista(apiArtista);
                    musica.setText(vagalumeBusca.getMus().get(0).getText());
                    musica.setTranslate(vagalumeBusca.getMus().get(0).getTranslate());

                    return musica;
                });
    }

}
