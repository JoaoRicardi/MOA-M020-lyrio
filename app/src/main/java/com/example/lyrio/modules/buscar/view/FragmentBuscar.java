package com.example.lyrio.modules.buscar.view;


import android.content.Intent;
import android.os.Bundle;
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
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.interfaces.ApiBuscaListener;
import com.example.lyrio.modules.Artista.view.PaginaArtistaActivity;
import com.example.lyrio.modules.buscar.viewmodel.BuscarViewModel;
import com.example.lyrio.modules.musica.view.TelaLetrasActivity;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.ApiItem;
import com.example.lyrio.util.Constantes;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBuscar extends Fragment implements ApiBuscaListener {

    private static final String TAG = "VAGALUME";
    private ArrayList<ApiItem> listaTemApi = new ArrayList<>();
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
    private List<ApiArtista> listaArtistasFavoritos;

    //Room ETC
    private BuscarViewModel buscarViewModel;

    public FragmentBuscar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_buscar, container, false);

        listaMusicasFavoritas = new ArrayList<>();
        listaArtistasFavoritos = new ArrayList<>();

        campoLetras = view.findViewById(R.id.buscar_letras_text_view);
        campoArtista = view.findViewById(R.id.buscar_artistas_text_view);
        campoUserFriendly = view.findViewById(R.id.buscar_friendly_message);
        campoDiv = view.findViewById(R.id.busca_resultado_div);

        campoUserFriendly.setText(Constantes.BUSCAR_FRIENDLY_WELCOME);
        campoDiv.setVisibility(View.GONE);

        userInputBusca = view.findViewById(R.id.buscar_campo_de_busca);
        botaoBuscar = view.findViewById(R.id.buscar_botao_busca);

        campoLetras.setVisibility(View.GONE);
        campoArtista.setVisibility(View.GONE);

        buscarViewModel = ViewModelProviders.of(this).get(BuscarViewModel.class);

        botaoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscaLetrasAdapter.removerTudo();
                buscaArtistasAdapter.removerTudo();
                buscarViewModel.fazerBusca(userInputBusca.getText().toString());
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

        buscarViewModel.atualizarListaFavoritos();

        buscarViewModel.getListaMusicasFavoritoLiveData()
                .observe(this, listaMusicas -> {
                    listaMusicasFavoritas = listaMusicas;
                });

        buscarViewModel.getListaArtistaBuscadaLiveData()
                .observe(this, listaArtista -> {
                    buscaArtistasAdapter.adicionarListaDeApiItems(listaArtista, listaMusicasFavoritas);
                });

        buscarViewModel.getListaMusicasBuscadaLiveData()
                .observe(this, listaMusica -> {
                    buscaLetrasAdapter.adicionarListaDeApiItems(listaMusica, listaMusicasFavoritas);
                });

        buscarViewModel.getBuscaLayoutLiveData()
                .observe(this, buscaLayout -> {

                    campoDiv.setVisibility(View.GONE);
                    campoLetras.setVisibility(View.GONE);
                    campoUserFriendly.setVisibility(View.GONE);
                    campoLetras.setText("Letras");
                    campoArtista.setText("Artistas");

                    if (buscaLayout.isArtistaEncontrado() && buscaLayout.isMusicaEncontrado()) {
                        campoLetras.setVisibility(View.VISIBLE);
                        campoArtista.setVisibility(View.VISIBLE);
                        campoDiv.setVisibility(View.VISIBLE);
                    } else if (buscaLayout.isArtistaEncontrado()) {
                        campoArtista.setVisibility(View.VISIBLE);
                    } else if (buscaLayout.isMusicaEncontrado()) {
                        campoLetras.setVisibility(View.VISIBLE);
                    } else {
                        campoUserFriendly.setVisibility(View.VISIBLE);
                        campoUserFriendly.setText(Constantes.BUSCAR_NAO_ENCONTRAMOS);
                    }
                });

        return view;
    }


    @Override
    public void onApiBuscarClicado(ApiItem apiItem) {

        if (apiItem.getCampoBottom().equals("Ver músicas")) {

            ApiArtista apiArtista = new ApiArtista();
            apiArtista.setUrl(apiItem.getUrl());
            apiArtista.setDesc(null);

            Intent intent = new Intent(getContext(), PaginaArtistaActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("ARTISTA", apiArtista);
            intent.putExtras(bundle);

            startActivity(intent);

        } else {


            favoritarApiItem(apiItem);
            Intent intent = new Intent(getContext(), TelaLetrasActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("MUSICA_ID", apiItem.getId());
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    @Override
    public void favoritarApiItem(ApiItem apiItem) {
        if (!apiItem.getCampoBottom().equals("Ver músicas")) {
            Musica musicaSalva = new Musica();
            musicaSalva.setId(apiItem.getId());

            buscarViewModel.favoritarMusica(musicaSalva);
        }
    }

    @Override
    public void removerApiItem(ApiItem apiItem) {
        if (!apiItem.getCampoBottom().equals("Ver músicas")) {

            buscarViewModel.getMusicaPorId(apiItem.getId());
            buscarViewModel.getMusicaLiveData()
                    .observe(this, musicaDoBanco -> {
                        buscarViewModel.removerMusica(musicaDoBanco);
                    });
        }
    }
}
