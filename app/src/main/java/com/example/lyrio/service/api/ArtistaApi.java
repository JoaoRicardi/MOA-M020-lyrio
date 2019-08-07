package com.example.lyrio.service.api;

import com.example.lyrio.api.base_vagalume.ApiArtista;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArtistaApi {


    @GET("artist")
    Observable<ApiArtista> getArtistas(@Query("api_key") String apiKey,
                                       @Query("format")String format);
}
