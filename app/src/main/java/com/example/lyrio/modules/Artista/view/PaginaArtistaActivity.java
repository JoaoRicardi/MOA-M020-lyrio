package com.example.lyrio.modules.Artista.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.interfaces.Filterable;
import com.example.lyrio.modules.Artista.viewmodel.ArtistasViewModel;
import com.example.lyrio.modules.musica.view.TelaLetrasActivity;
import com.example.lyrio.adapters.ArtistaListaMusicasRecyclerAdapter;
import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.interfaces.ListaMusicasSalvasListener;
import com.example.lyrio.util.Constantes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaginaArtistaActivity extends AppCompatActivity implements ListaMusicasSalvasListener {

    private ArtistaListaMusicasRecyclerAdapter artistaListaMusicasRecyclerAdapter;
    private CircleImageView imagemArtistaImageView;
    private TextView nomeArtistaTextView;
    private ToggleButton seguirButton;
    private ToggleButton showSearchview;

    private RecyclerView recyclerView;
    private TextView userFriendlyText;

    private List<Musica> listaDeMusicasFavoritasDoBanco;
    private List<Musica> listaDeMusicasFavoritasDoArtista;
    private TextView buttonTextTopLyrics;
    private TextView buttonTextAllLyrics;
    private TextView buttonTextFavLyrics;
    private List<Musica> listaTopLyrics;
    private List<Musica> listaLyrics;
    private String imgUrlBase = "https://www.vagalume.com";
    private ApiArtista artistaBundle;
    private ApiArtista artistaApi;
    private ArtistasViewModel artistasViewModel;
    private boolean isFavoritado;
    private boolean isSearchviewVisible;
    private SearchView searchView;

    private String txtFriendlyTop = "OPS!\n\nEste artista ainda\nnão tem Top Músicas\n\n:(";
    private String txtFriendlyFavs = "OMG!\n\nVocê ainda não favoritou\nmúsicas deste artista\n\n:(";
    private String txtFriendlyNoResults = "EITA!\n\nNenhuma música com\nesse nome...\n\n:(";
    private String currentSearch;

    //Botões fake
    private boolean txtButtonTopSongs;
    private boolean txtButtonAllSongs;
    private boolean txtButtonFavSongs;

    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_artista_sem_album);

        //Definir as variaveis
        nomeArtistaTextView = findViewById(R.id.artista_nome_artista_text_view);
        imagemArtistaImageView = findViewById(R.id.artista_profile_image_view);
        seguirButton = findViewById(R.id.letras_favorito_button);
        buttonTextTopLyrics = findViewById(R.id.artista_txt_button_top_lyrics);
        buttonTextAllLyrics = findViewById(R.id.artista_txt_button_all_lyrics);
        buttonTextFavLyrics = findViewById(R.id.artista_txt_button_fav_lyrics);
        showSearchview = findViewById(R.id.artista_toggle_button_filtrar);
        recyclerView = findViewById(R.id.pagina_artista_lista_musicas_recycler_view);
        userFriendlyText = findViewById(R.id.txt_friendly_top_musicas);

        searchView = findViewById(R.id.artista_search_view);
        searchView.setVisibility(View.GONE);
        isSearchviewVisible = false;
        currentSearch = "";
        seguirButton.setVisibility(View.GONE);


        //Inicializar listas
        listaTopLyrics = new ArrayList<>();
        listaLyrics = new ArrayList<>();

        //Definir artista com base nas infos que vieram do bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        artistaBundle = (ApiArtista) bundle.getSerializable("ARTISTA");

//        if(artistaBundle.isFavoritarArtista()){
//            seguirButton.setVisibility(View.VISIBLE);
//            seguirButton.setChecked(true);
//        }

        //Definir visibilidade do texto user friendly, quando não tem musicas no recycler
        userFriendlyText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        //Inicializar ViewModel
        artistasViewModel = ViewModelProviders.of(this).get(ArtistasViewModel.class);
        listaDeMusicasFavoritasDoBanco = new ArrayList<>();
        artistasViewModel.getMusicasFavoritasDoBanco();
        artistasViewModel.atualizarListadeArtistas();
        artistasViewModel.getArtistaPorUrl(artistaBundle.getUrl().split("/")[1]);
        artistasViewModel.getMusicasFavoritasDoArtista(artistaBundle.getUrl());

        //Puxar listas de musicas do banco
        artistasViewModel.getListaDeMusicasFavoritasDoBanco()
                .observe(this, listaMusicas->{
                    listaDeMusicasFavoritasDoBanco = listaMusicas;
                });

        artistasViewModel.getListaDeMusicasFavoritasDoArtista()
                .observe(this, listMusArt->{
                    listaDeMusicasFavoritasDoArtista = listMusArt;
                    if(txtButtonFavSongs){
                        swichListas("FAV", currentSearch, false);
                        artistaListaMusicasRecyclerAdapter.atualizarLista(listaDeMusicasFavoritasDoArtista, artistaApi, true);
                    }
                });


        //Conferir no banco se o artista é favorito
        artistasViewModel.getIsFavorito()
                .observe(this, isFav->{
                    isFavoritado = isFav;
                    seguirButton.setVisibility(View.VISIBLE);
                    if(isFavoritado){
                        seguirButton.setChecked(true);
                    }else{
                        seguirButton.setChecked(false);
                    }
                });

        //Configuração do recycler
        artistaListaMusicasRecyclerAdapter = new ArtistaListaMusicasRecyclerAdapter(listaTopLyrics, this, artistaApi);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(artistaListaMusicasRecyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);


        //Toggle Button para alternar visibilidade do SearchView
        showSearchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearchviewVisible){
                    currentSearch = "";
                    searchView.setQuery(currentSearch, true);
                    artistaListaMusicasRecyclerAdapter.getFilter().filter(currentSearch);
                    artistaListaMusicasRecyclerAdapter.restoreList();
                    searchView.setVisibility(View.GONE);
                }else{
                    searchView.setVisibility(View.VISIBLE);
                }

                friendlyIfEmpty();
                isSearchviewVisible = !isSearchviewVisible;
            }
        });

        //Configuração do SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearch = newText;
                artistaListaMusicasRecyclerAdapter.getFilter().filter(currentSearch);
//                friendlyIfEmpty();
                return false;
            }
        });


        artistasViewModel.getArtistaLiveData()
                .observe(this, apiArtista -> {
                    artistaApi = apiArtista;

                    //Mostrar botão de seguir apenas quando o artista for carregado
                    seguirButton.setVisibility(View.VISIBLE);

                    if(artistaApi.isFavoritarArtista()){
                        seguirButton.setChecked(true);
                    }

                    nomeArtistaTextView.setText(artistaApi.getDesc());
                    Picasso.get()
                            .load(imgUrlBase+artistaApi.getPic_small())
                            .placeholder(R.drawable.placeholder_logo)
                            .into(imagemArtistaImageView);
                    listaTopLyrics = artistaApi.getToplyrics().getItem();
                    listaLyrics = artistaApi.getLyrics().getItem();

                    if(listaTopLyrics.size()==0 || listaTopLyrics==null){
                        swichListas("ALL", currentSearch, false);
                        artistaListaMusicasRecyclerAdapter.atualizarLista(listaLyrics, artistaApi, true);
//                        friendlyIfEmpty();
                    }else{
                        swichListas("TOP", currentSearch, false);
                        artistaListaMusicasRecyclerAdapter.atualizarLista(listaTopLyrics, artistaApi, true);
//                        friendlyIfEmpty();
                    }
                });


        buttonTextAllLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtButtonAllSongs){
                    swichListas("ALL", currentSearch, false);
                }
            }
        });

        buttonTextTopLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtButtonTopSongs) {
                    swichListas("TOP", currentSearch, false);
                }
            }
        });

        buttonTextFavLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtButtonFavSongs){
                    swichListas("FAV", currentSearch, false);
                }
            }
        });


        //Favoritar ou desfavoritar artista no banco
        seguirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seguirButton.isChecked()){
                    Toast.makeText(PaginaArtistaActivity.this, Constantes.TOAST_ARTISTA_FAVORITO_ADICIONAR, Toast.LENGTH_SHORT).show();

                    String urlArt = artistaBundle.getUrl().replace("/","");
                    ApiArtista apiArtSalvo = new ApiArtista();
                    apiArtSalvo.setUrl(urlArt);

                    artistasViewModel.favoritarArtista(apiArtSalvo);
                    artistasViewModel.atualizarListadeArtistas();
                }else{
                    Toast.makeText(PaginaArtistaActivity.this, Constantes.TOAST_ARTISTA_FAVORITO_EXCLUIR, Toast.LENGTH_SHORT).show();

                    String urlArt = artistaBundle.getUrl().replace("/","");
                    ApiArtista apiArtSalvo = new ApiArtista();
                    apiArtSalvo.setUrl(urlArt);

                    artistasViewModel.removerArtista(apiArtSalvo);
                    artistasViewModel.atualizarListadeArtistas();
                }
            }
        });
    }

    //Função que mostra o texto friendly se o recycler estiver vazio
    private void friendlyIfEmpty(){
//        Log.i(TAG, " Tamanho da lista: "+artistaListaMusicasRecyclerAdapter.getItemCount());
        if(artistaListaMusicasRecyclerAdapter.getItemCount()>0){
            userFriendlyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            String defineMsg;
            if(currentSearch.equals("")){
                if(txtButtonTopSongs){
                    defineMsg = txtFriendlyTop;
                }else{
                    defineMsg = txtFriendlyFavs;
                }
            }else{
                defineMsg = txtFriendlyNoResults;
            }

            userFriendlyText.setText(defineMsg);
            userFriendlyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    //Função que alterna o conteúdo dos recyclers com base no botão escolhido
    private void swichListas(String whatList, String curSearch, boolean notify){
        switch (whatList){
            case "TOP":
                txtButtonTopSongs = true;
                txtButtonAllSongs = false;
                txtButtonFavSongs = false;
                buttonTextTopLyrics.setTextAppearance(R.style.toggleTextSelected);
                buttonTextAllLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextFavLyrics.setTextAppearance(R.style.toggleTextOff);
                artistaListaMusicasRecyclerAdapter.atualizarLista(listaTopLyrics, artistaApi, notify);
                artistaListaMusicasRecyclerAdapter.getFilter().filter(curSearch);
                friendlyIfEmpty();
                break;
            case "ALL":
                txtButtonTopSongs = false;
                txtButtonAllSongs = true;
                txtButtonFavSongs = false;
                buttonTextTopLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextAllLyrics.setTextAppearance(R.style.toggleTextSelected);
                buttonTextFavLyrics.setTextAppearance(R.style.toggleTextOff);
                artistaListaMusicasRecyclerAdapter.atualizarLista(listaLyrics, artistaApi, notify);
                artistaListaMusicasRecyclerAdapter.getFilter().filter(curSearch);
                friendlyIfEmpty();
                break;
            case "FAV":
                txtButtonTopSongs = false;
                txtButtonAllSongs = false;
                txtButtonFavSongs = true;
                buttonTextTopLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextAllLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextFavLyrics.setTextAppearance(R.style.toggleTextSelected);
                artistaListaMusicasRecyclerAdapter.atualizarLista(listaDeMusicasFavoritasDoArtista, artistaApi, notify);
                artistaListaMusicasRecyclerAdapter.getFilter().filter(curSearch);
                friendlyIfEmpty();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        artistasViewModel.getMusicasFavoritasDoBanco();
        artistasViewModel.getMusicasFavoritasDoArtista(artistaBundle.getUrl());
        artistasViewModel.atualizarListadeArtistas();
//        artistasViewModel.atualizarTudo();
    }

    @Override
    public void onListaMusicasSalvasClicado(Musica musicaSalva) {

        Musica tempMusic = new Musica();
        tempMusic.setId(musicaSalva.getId());
        tempMusic.setFavoritarMusica(false);

        for(int y=0; y<listaDeMusicasFavoritasDoBanco.size(); y++){
            Log.i("VAGALUME", " Procurando no banco: "+listaDeMusicasFavoritasDoBanco.get(y).getId()+"\nE comparando com o id Original: "+musicaSalva.getId());
            if(listaDeMusicasFavoritasDoBanco.get(y).getId().equals(musicaSalva.getId())){
                tempMusic.setFavoritarMusica(true);
            }
        }


        Intent intent = new Intent(this, TelaLetrasActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("MUSICA", tempMusic);
        bundle.putBoolean("VOLTAR_ARTISTA", true);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void updateItemCount() {
        friendlyIfEmpty();
    }
}
