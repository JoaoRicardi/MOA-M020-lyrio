package com.example.lyrio.modules.view.menu;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.adapters.BuscaAdapter;
import com.example.lyrio.api.VagalumeBuscaApi;
import com.example.lyrio.api.base_vagalume.ApiArtista;
import com.example.lyrio.api.base_vagalume.ApiItem;
import com.example.lyrio.api.base_vagalume.ApiResponse;
import com.example.lyrio.api.base_vagalume.VagalumeBusca;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.interfaces.ApiBuscaListener;
import com.example.lyrio.modules.view.Artista.PaginaArtistaActivity;
import com.example.lyrio.modules.view.musica.TelaLetras;
import com.example.lyrio.modules.viewModel.ListaMusicasViewModel;
import com.example.lyrio.util.Constantes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBuscar extends Fragment implements ApiBuscaListener{

    private static final String TAG = "VAGALUME";
//    private ArrayList<ApiItem> listaTemApi = new ArrayList<>();
    private Retrofit retrofit;
    private EditText userInputBusca;
    private TextView campoUserFriendly;
    private TextView campoLetras;
    private TextView campoArtista;
    private View campoDiv;
    private Button botaoBuscar;
    private RecyclerView recyclerLetras;
    private RecyclerView recyclerArtistas;
    private BuscaAdapter buscaLetrasAdapter;
    private BuscaAdapter buscaArtistasAdapter;
    private List<Musica> listaMusicasFavoritas;

    //Room ETC
    private ListaMusicasViewModel listaMusicasViewModel;

    public FragmentBuscar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_buscar, container, false);

        listaMusicasFavoritas = new ArrayList<>();


        // Iniciar retrofit para buscar infos da API
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vagalume.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        campoLetras = view.findViewById(R.id.buscar_letras_text_view);
        campoArtista = view.findViewById(R.id.buscar_artistas_text_view);
        campoUserFriendly = view.findViewById(R.id.buscar_friendly_message);
        campoDiv = view.findViewById(R.id.busca_resultado_div);

        campoLetras.setText("");
        campoArtista.setText("");
        campoUserFriendly.setText(Constantes.BUSCAR_FRIENDLY_WELCOME);
        campoDiv.setAlpha(0);


        userInputBusca = view.findViewById(R.id.buscar_campo_de_busca);
        botaoBuscar = view.findViewById(R.id.buscar_botao_busca);

        botaoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getContext(), "QTDADE: " + listaMusicasFavoritas.size(), Toast.LENGTH_SHORT).show();


                campoLetras.setText("");
                campoArtista.setText("");
                campoUserFriendly.setText("");
                campoDiv.setAlpha(0);

                buscaLetrasAdapter.removerTudo();
                buscaArtistasAdapter.removerTudo();
                fazerBusca(userInputBusca.getText().toString(), "ambos", 5);
            }
        });

        recyclerLetras = view.findViewById(R.id.buscar_letras_recycler);
        buscaLetrasAdapter = new BuscaAdapter(this.getActivity(), this, listaMusicasFavoritas); // "this" adicionado por causa do Glide
        recyclerLetras.setAdapter(buscaLetrasAdapter);
        RecyclerView.LayoutManager layoutLetrasManager = new LinearLayoutManager(this.getActivity());
        recyclerLetras.setLayoutManager(layoutLetrasManager);

        recyclerArtistas = view.findViewById(R.id.buscar_artistas_recycler);
        buscaArtistasAdapter = new BuscaAdapter(this.getActivity(), this, listaMusicasFavoritas); // "this" adicionado por causa do Glide
        recyclerArtistas.setAdapter(buscaArtistasAdapter);
        RecyclerView.LayoutManager layoutArtistasManager = new LinearLayoutManager(this.getActivity());
        recyclerArtistas.setLayoutManager(layoutArtistasManager);


        listaMusicasViewModel = ViewModelProviders.of(this).get(ListaMusicasViewModel.class);
        listaMusicasViewModel.atualizarLista();

        listaMusicasViewModel.getListaMusicasLiveData()
                .observe(this, listaMusicas -> {
                    listaMusicasFavoritas = listaMusicas;
                });


        return view;
    }

    private void fazerBusca(String termoBuscado, String artistaOuMusica, Integer qtdResultados) {

        Date curTime = Calendar.getInstance().getTime();

        termoBuscado = termoBuscado.trim().replace(" ", "%20");
        String vagaKey =  Constantes.VAGALUME_KEY + curTime.toString().trim().replace(" ","")
                ;
        String limitador = "&limit="+qtdResultados.toString();

        String buscaBase = "";
        switch (artistaOuMusica){
            case "artista":
                buscaBase = "search.art?apikey=" + vagaKey + "&q=";
                break;
            case "musica":
                buscaBase = "search.excerpt?apikey=" + vagaKey + "&q=";
                break;
            default:
                buscaBase = "search.artmus?apikey=" + vagaKey + "&q=";
        }

        String buscaFull = buscaBase+termoBuscado+limitador;

        VagalumeBuscaApi service = retrofit.create(VagalumeBuscaApi.class);
        Call<VagalumeBusca> vagalumeBuscaCall = service.getBuscaResponse(buscaFull);
        vagalumeBuscaCall.enqueue(new Callback<VagalumeBusca>() {
            @Override
            public void onResponse(Call<VagalumeBusca> call, Response<VagalumeBusca> response) {
                if(response.isSuccessful()){
                    VagalumeBusca vagalumeBusca = response.body();
                    ApiResponse buscaResponse = vagalumeBusca.getResponse();
                    int qtdResult = buscaResponse.getDocs().size();
                    String gotResult = "";

                    ArrayList<ApiItem> listaDeArtistas = new ArrayList<>();
                    ArrayList<ApiItem> listaDeMusicas = new ArrayList<>();

                    if(qtdResult>0){
                        for(int i=0; i<qtdResult; i++){

                            String bandName = buscaResponse.getDocs().get(i).getBand();
                            String songTitle = buscaResponse.getDocs().get(i).getTitle();
                            String pgUrl = buscaResponse.getDocs().get(i).getUrl();
                            String theId = buscaResponse.getDocs().get(i).getId();
                            ApiItem curApiItem = new ApiItem();

                            if(bandName != null && songTitle != null){

                                // Logar em txt
                                gotResult = gotResult+"MÚSICA - "+bandName+" - "+songTitle+"\n";

                                // Adicionar ao Recycler
                                curApiItem.setBand(bandName);
                                curApiItem.setCampoTop(songTitle);
                                curApiItem.setCampoBottom(bandName);
                                curApiItem.setUrl(pgUrl);
                                curApiItem.setId(theId);
                                listaDeMusicas.add(curApiItem);
                                Log.i(TAG, " Musica API: " +curApiItem.getCampoTop());


                            } else if(songTitle == null){

                                // Printar txt
                                gotResult = gotResult+"ARTISTA - "+bandName+"\n";

                                // Adicionar ao Recycler
                                curApiItem.setBand(bandName);
                                curApiItem.setCampoTop(bandName);
                                curApiItem.setCampoBottom("Ver músicas");
                                curApiItem.setUrl(pgUrl);
                                listaDeArtistas.add(curApiItem);
                                Log.i(TAG, " Artista_old API: " +curApiItem.getCampoTop());
                            }
                        }

                    }

                    buscaLetrasAdapter.adicionarListaDeApiItems(listaDeMusicas, listaMusicasFavoritas);
                    buscaArtistasAdapter.adicionarListaDeApiItems(listaDeArtistas, listaMusicasFavoritas);

                    if(listaDeMusicas.size()>0 && listaDeArtistas.size()>0){
                        campoLetras.setText("Letras");
                        campoArtista.setText("Artistas");
                        campoUserFriendly.setText("");
                        campoDiv.setAlpha(1);
                    }else if(listaDeMusicas.size()==0 && listaDeArtistas.size()>0){
                        campoLetras.setText("");
                        campoArtista.setText("Artistas");
                        campoUserFriendly.setText("");
                        campoDiv.setAlpha(0);
                    }else if(listaDeMusicas.size()>0){
                        campoLetras.setText("Letras");
                        campoArtista.setText("");
                        campoUserFriendly.setText("");
                        campoDiv.setAlpha(0);
                    }else{
                        campoLetras.setText("");
                        campoArtista.setText("");
                        campoUserFriendly.setText(Constantes.BUSCAR_NAO_ENCONTRAMOS);
                        campoDiv.setAlpha(0);
                    }

                }else {Log.e(TAG, " onResponse: "+response.errorBody());}
            }

            @Override
            public void onFailure(Call<VagalumeBusca> call, Throwable t){Log.e(TAG, " onFailure: "+t.getMessage());}
        });
    }


    @Override
    public void onApiBuscarClicado(ApiItem apiItem) {

        if(apiItem.getCampoBottom().equals("Ver músicas")){

            ApiArtista apiArtista = new ApiArtista();
            apiArtista.setUrl(apiItem.getUrl());
            apiArtista.setDesc(null);

            Intent intent = new Intent(getContext(), PaginaArtistaActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("ARTISTA", apiArtista);
            intent.putExtras(bundle);

            startActivity(intent);

        }else{

//            listaMusicasViewModel.favoritarApiItem(musicaSalva);

            Intent intent = new Intent(getContext(), TelaLetras.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("MUSICA_ID", apiItem.getId());
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    @Override
    public void favoritarApiItem(ApiItem apiItem) {
        if(!apiItem.getCampoBottom().equals("Ver músicas")){
            Musica musicaSalva = new Musica();
            musicaSalva.setId(apiItem.getId());

            listaMusicasViewModel.favoritarMusica(musicaSalva);
        }
    }

    @Override
    public void removerApiItem(ApiItem apiItem) {
        if(!apiItem.getCampoBottom().equals("Ver músicas")){

            listaMusicasViewModel.getMusicaPorId(apiItem.getId());
            listaMusicasViewModel.getMusicaLiveData()
                    .observe(this, musicaDoBanco -> {
                        listaMusicasViewModel.removerMusica(musicaDoBanco);
                    });
        }
    }
}
