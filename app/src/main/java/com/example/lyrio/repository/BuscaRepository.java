package com.example.lyrio.repository;

import com.example.lyrio.service.RetrofitService;
import com.example.lyrio.service.model.ApiItem;
import com.example.lyrio.service.model.VagalumeBusca;
import com.example.lyrio.util.Constantes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class BuscaRepository {

    private RetrofitService retrofitService = new RetrofitService();

    public Observable<List<ApiItem>> buscar(String termos){
        Date curTime = Calendar.getInstance().getTime();
        String vagaKey =  Constantes.VAGALUME_KEY + curTime.toString().trim().replace(" ","");
        return retrofitService.getBuscaApi()
                .getBuscaResponse(vagaKey, termos, 5)
                .map(vagalumeBusca -> vagalumeBusca.getResponse().getDocs());
    }

}
