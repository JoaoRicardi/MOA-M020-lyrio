package com.example.lyrio.service.api;

import com.example.lyrio.database.models.Musica;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicaApi {

    @GET("mus")
    Observable<Musica> getMusicas(@Query("api_key") String apiKey,
                                 @Query("format") String format);
}
