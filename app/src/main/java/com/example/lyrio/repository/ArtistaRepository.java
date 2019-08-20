package com.example.lyrio.repository;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.lyrio.database.models.Musica;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.service.RetrofitService;
import com.example.lyrio.service.model.VagalumeBusca;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class ArtistaRepository {
    private LyrioDatabase db;

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
//    public Observable<VagalumeBusca> getArtistaIdApi(String artistaId){
//        return  retrofitService.getBuscaApi()
//                .getBuscaResponse(API_KEY,artistaId,7)
//                .map(vagalumeBusca -> {
//                    ApiArtista apiArtista = new ApiArtista();
//                    apiArtista.setName(vagalumeBusca.getArt().getName());
//
//                    Musica musica = new Musica();
//                    musica.setName(vagalumeBusca.getMus().get(0).getName());
//                    musica.setArtista(apiArtista);
//                    return vagalumeBusca;
//                });
//    }
}
