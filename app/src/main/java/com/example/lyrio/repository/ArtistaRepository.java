package com.example.lyrio.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.service.RetrofitService;
import com.example.lyrio.service.model.ApiItem;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class ArtistaRepository {
    private LyrioDatabase db;

    private static final String TAG = "VAGALUME";
    private RetrofitService retrofitService = new RetrofitService();

    private static final String API_KEY = UUID.randomUUID()+"";
    private static final String FORMAT = "json";

    public Flowable<ApiArtista> getArtistaPorId(Context context, String idDoArtista){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();


        return db.artistasFavoritosDao()
                .getArtistaPorId(idDoArtista);
    }

    public Flowable<List<ApiArtista>> getAllArtistas(Context context){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();

        return db.artistasFavoritosDao()
                .getArtistasFavoritos();
    }

    public Completable favoritarArtista(ApiArtista artista, Context context){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
        return Completable.fromAction(( )-> db.artistasFavoritosDao()
                .inserir(artista));
    }

    public Completable removerArtista(ApiArtista artista, Context context){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
        return Completable.fromAction(()->db.artistasFavoritosDao()
        .delete(artista));
    }

    public Completable removerArtistaPorId(String artistaId, Application context){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
        return Completable.fromAction(()-> db.artistasFavoritosDao()
        .deletePorId(artistaId));
    }

    public Observable<List<ApiArtista>> getArtistasList(){
        return retrofitService.getArtistaApi()
                .getArtistas(API_KEY, FORMAT)
                .map(artistaList->artistaList.getApiArtistaList());
    }


    public Observable<ApiArtista> getArtistaPorUrl(String urlArtista){
        String buscaCorreta = urlArtista+"/index.js";
        Log.i(TAG, " BUSCA CORRETA: "+buscaCorreta);
        return retrofitService.getArtistaApi()
                .getArtistaApi(buscaCorreta)
                .map(artistaApi -> {
                    Log.i(TAG, " GOT ARTIST: "+artistaApi.getArtist().getDesc());

//                    ApiArtista apiArtista = new ApiArtista();
//                    apiArtista.setDesc(artistaApi.getArtist().getDesc());

                    return artistaApi.getArtist();
                });

    }
}
