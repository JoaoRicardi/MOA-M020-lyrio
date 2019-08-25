package com.example.lyrio.service;

import com.example.lyrio.service.api.ArtistaApi;
import com.example.lyrio.service.api.MusicaApi;
import com.example.lyrio.service.api.VagalumeBuscaApi;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;

    private static  final  String BASE_URL = "https://api.vagalume.com.br/";
    private static  final  String BASE_URL_ART = "https://www.vagalume.com.br/";

    private Retrofit getRetrofit(String base){
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
            clientBuilder.readTimeout(30, TimeUnit.SECONDS);
            clientBuilder.writeTimeout(30, TimeUnit.SECONDS);

            clientBuilder.addNetworkInterceptor(new StethoInterceptor());

            retrofit = new Retrofit.Builder()
                    .baseUrl(base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(clientBuilder.build())
                    .build();
        }
        return retrofit;
    }

    public ArtistaApi getArtistaApi(){
        return getRetrofit(BASE_URL_ART).create(ArtistaApi.class);
    }

    public MusicaApi getMusicasApi(){
        return getRetrofit(BASE_URL).create(MusicaApi.class);
    }

    public VagalumeBuscaApi getBuscaApi(){
        return getRetrofit(BASE_URL).create(VagalumeBuscaApi.class);
    }
}
