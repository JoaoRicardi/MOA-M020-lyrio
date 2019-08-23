package com.example.lyrio.service.api;

import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.VagalumeBusca;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ArtistaApi {


    @GET("artist")
    Observable<ApiArtista> getArtistas(@Query("api_key") String apiKey,
                                       @Query("format")String format);

    @GET
    Observable<VagalumeBusca> getArtistaApi(@Url String url);
}
