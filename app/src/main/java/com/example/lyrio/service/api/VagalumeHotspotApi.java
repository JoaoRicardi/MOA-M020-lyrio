package com.example.lyrio.service.api;

import com.example.lyrio.service.model.VagalumeHotspot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VagalumeHotspotApi {

    @GET("hotspots.php")
    Call<VagalumeHotspot> getListaHotspot(@Query("apiKey") String apiKey);

}
