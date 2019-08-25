package com.example.lyrio.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.service.RetrofitService;
import com.example.lyrio.service.model.VagalumeBusca;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class ArtistaRepository {

    private LyrioDatabase db;

    private static final String TAG = "VAGALUME";
    private RetrofitService retrofitService = new RetrofitService();

    private static final String API_KEY = UUID.randomUUID()+"";
    private static final String FORMAT = "json";

    Date curTime = Calendar.getInstance().getTime();
    String vagaKey = curTime.toString().replace(" ","");


    public Flowable<List<ApiArtista>> getAllArtistas(Context context){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
        Log.i(TAG, " Tentativa de atualizar lista de artistas no repository");

        return db.artistasFavoritosDao()
                .getArtistasFavoritos();
    }

    public Completable favoritarArtista(ApiArtista artista, Context context){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
        Log.i(TAG, " Artista "+artista.getUrl()+" - tentativa de favoritar no repository");

        return Completable.fromAction(( )-> db.artistasFavoritosDao()
                .inserir(artista));
    }

    public Completable removerArtistaPorUrl(String artistaUrl, Application context){
        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
        Log.i(TAG, " Artista "+artistaUrl+" - tentativa de remover no repository");

        return Completable.fromAction(()-> db.artistasFavoritosDao()
        .deletePorUrl(artistaUrl));
    }

    public Observable<List<ApiArtista>> getArtistasList(){
        return retrofitService.getArtistaApi()
                .getArtistas(API_KEY, FORMAT)
                .map(artistaList->artistaList.getApiArtistaList());
    }


    public Observable<ApiArtista> getArtistaPorUrl(String urlArtista){
        String buscaCorreta = urlArtista+"/index.js";
//        Log.i(TAG, " ArtistaRepository BUSCA CORRETA: "+buscaCorreta);
        return retrofitService.getArtistaApi()
                .getArtistaApi(buscaCorreta)
                .map(vagalumeBusca -> vagalumeBusca.getArtist());
    }


//    public Flowable<ApiArtista> getArtistaPorId(Context context, String idDoArtista){
//        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
//
//
//        return db.artistasFavoritosDao()
//                .getArtistaPorUrl(idDoArtista);
//    }

//    public Completable removerArtista(ApiArtista artista, Context context){
//        db = Room.databaseBuilder(context,LyrioDatabase.class,LyrioDatabase.DATABASE_NAME).build();
//        return Completable.fromAction(()->db.artistasFavoritosDao()
//        .delete(artista));
//    }

}
