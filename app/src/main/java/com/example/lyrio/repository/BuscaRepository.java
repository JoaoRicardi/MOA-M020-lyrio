package com.example.lyrio.repository;

import com.example.lyrio.service.RetrofitService;
import com.example.lyrio.service.model.ApiItem;
import com.example.lyrio.service.model.VagalumeBusca;
import com.example.lyrio.util.Constantes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

public class BuscaRepository {

    private RetrofitService retrofitService = new RetrofitService();

    public Observable<List<ApiItem>> buscar(String termos){
        Date curTime = Calendar.getInstance().getTime();
        String vagaKey = curTime.toString().replace(" ","");
//        String vagaKey = "52433bd778677b92342a16ddf927e4bf";
        return retrofitService.getBuscaApi()
                .getBuscaResponse(vagaKey, termos, 5)
                .map(vagalumeBusca -> vagalumeBusca.getResponse().getDocs());
    }

}
