package com.example.lyrio.modules.noticiasHotspot.view;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lyrio.R;
import com.example.lyrio.adapters.HotspotAdapter;
import com.example.lyrio.modules.Artista.view.PaginaArtistaActivity;
import com.example.lyrio.modules.vagalumeAbrirLink.view.VagalumeAbrirLink;
import com.example.lyrio.service.api.VagalumeHotspotApi;
import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.service.model.VagalumeHotspot;
import com.example.lyrio.model.Hotspot;
import com.example.lyrio.interfaces.HotspotListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNoticias extends Fragment implements HotspotListener {

    private static final String TAG = "VAGALUME";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private HotspotAdapter hotspotAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtUltimaAtualização;

    public FragmentNoticias() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_noticias, container, false);

        // Iniciar retrofit para buscar infos da API
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vagalume.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Configurar retrofit
        recyclerView = view.findViewById(R.id.noticias_recycler_view);
        hotspotAdapter = new HotspotAdapter(this.getActivity(), this); // "this" adicionado por causa do Glide
        recyclerView.setAdapter(hotspotAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        txtUltimaAtualização = view.findViewById(R.id.hotspot_ultima_atualizacao);
        String atualizadoText = "Atualizado em:\n";
        txtUltimaAtualização.setText(atualizadoText +android.text.format.DateFormat.format("kk:mm:ss", new java.util.Date()));

        // Executar retrofit para buscar dados da API
        getRetrofitData();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRetrofitData();
                txtUltimaAtualização.setText(atualizadoText +android.text.format.DateFormat.format("kk:mm:ss", new java.util.Date()));
            }
        });

        return view;
    }

    private void  getRetrofitData(){
        VagalumeHotspotApi service = retrofit.create(VagalumeHotspotApi.class);
        Call<VagalumeHotspot> vagalumeHotspotCall = service.getListaHotspot(UUID.randomUUID()+"");
        vagalumeHotspotCall.enqueue(new Callback<VagalumeHotspot>() {
            @Override
            public void onResponse(Call<VagalumeHotspot> call, Response<VagalumeHotspot> response) {
                if(response.isSuccessful()){
                    VagalumeHotspot vagalumeHotspot = response.body();
                    ArrayList<Hotspot> listaHotspot = vagalumeHotspot.getHotspots();
                    hotspotAdapter.adicionarListaHotspot(listaHotspot);

                    // Logar no console cada info recebida pela API
                    for(int i=0; i<listaHotspot.size(); i++){
                        Hotspot h = listaHotspot.get(i);
//                        Log.i(TAG, " Hotspot: " +h.getTitle());
                    }

                }else {Log.e(TAG, " onResponse: "+response.errorBody());}
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<VagalumeHotspot> call, Throwable t){Log.e(TAG, " onFailure: "+t.getMessage());}
        });
    }

    @Override
    public void onHotspotClicado(Hotspot hotspot) {
        String url = hotspot.getLink();

        Intent intent = new Intent(getActivity(), VagalumeAbrirLink.class);
        Bundle bundle = new Bundle();

        // Para poder adicionar ao bundle, a classe tem que implementar "Serializable"
        bundle.putString("HOTSPOT_LINK", hotspot.getLink());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onArtistaClicado(String artistaUrl) {

        ApiArtista apiArtista = new ApiArtista();
        apiArtista.setUrl(artistaUrl);
        apiArtista.setDesc(null);

        Intent intent = new Intent(getContext(), PaginaArtistaActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("ARTISTA", apiArtista);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
