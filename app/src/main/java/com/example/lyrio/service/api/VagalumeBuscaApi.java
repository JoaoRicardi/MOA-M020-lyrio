package com.example.lyrio.service.api;

import com.example.lyrio.service.model.VagalumeBusca;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface VagalumeBuscaApi {

    @GET("search.artmus")
    Observable<VagalumeBusca> getBuscaResponse(@Query("apikey") String apiKey,
                                               @Query("q") String termo,
                                               @Query("limit") int limit);


}
