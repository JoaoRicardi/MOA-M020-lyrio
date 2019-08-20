package com.example.lyrio.service.api;

import com.example.lyrio.service.model.VagalumeBusca;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface VagalumeHomeApi {

    @GET
    Call<VagalumeBusca> getBuscaResponse(@Url String url);
}
