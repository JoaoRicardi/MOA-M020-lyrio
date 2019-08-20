package com.example.lyrio.service.api;

import com.example.lyrio.database.models.Musica;
import com.example.lyrio.service.model.VagalumeBusca;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicaApi {

    @GET("mus")
    Observable<Musica> getMusicas(@Query("api_key") String apiKey,
                                  @Query("format") String format);

    @GET("search.php")
    Observable<VagalumeBusca> getMusicasById(@Query("api_key") String apiKey,
                                             @Query("musid") String idMusica);


}
