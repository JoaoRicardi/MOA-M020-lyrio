package com.example.lyrio.modules.Artista.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
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

public class PaginaArtistaActivity extends AppCompatActivity implements ListaMusicasSalvasListener{

    //    private ImageView artistaBg;
    private ArtistaListaMusicasRecyclerAdapter artistaListaMusicasRecyclerAdapter;
    private CircleImageView imagemArtistaImageView;
    private TextView nomeArtistaTextView;
    private ToggleButton seguirButton;
    private List<Musica> listaDeMusicasSalvas;

    private RecyclerView recyclerView;
    private TextView userFriendlyText;

    private List<Musica> listaDeMusicasFavoritasDoBanco;
    private List<Musica> listaDeMusicasFavoritasDoArtista;
    private List<ApiArtista> listaDeArtistasFavoritasDoBanco;
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

    private String txtFriendlyTop = "OPS!\n\nEste artista ainda\nnão tem Top Músicas\n\n:(";
    private String txtFriendlyFavs = "OPS!\n\nVocê ainda não favoritou\nmúsicas deste artista\n\n:(";

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
//        artistaBg = findViewById(R.id.artista_imagem_bg);
        nomeArtistaTextView = findViewById(R.id.artista_nome_artista_text_view);
        imagemArtistaImageView = findViewById(R.id.artista_profile_image_view);
        seguirButton = findViewById(R.id.letras_favorito_button);
        buttonTextTopLyrics = findViewById(R.id.artista_txt_button_top_lyrics);
        buttonTextAllLyrics = findViewById(R.id.artista_txt_button_all_lyrics);
        buttonTextFavLyrics = findViewById(R.id.artista_txt_button_fav_lyrics);

        recyclerView = findViewById(R.id.pagina_artista_lista_musicas_recycler_view);
        userFriendlyText = findViewById(R.id.txt_friendly_top_musicas);

        userFriendlyText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        artistaBundle = (ApiArtista) bundle.getSerializable("ARTISTA");

        if(artistaBundle.isFavoritarArtista()){
            seguirButton.setChecked(true);
        }

        artistasViewModel = ViewModelProviders.of(this).get(ArtistasViewModel.class);
        listaDeMusicasFavoritasDoBanco = new ArrayList<>();
        artistasViewModel.getMusicasFavoritasDoBanco();
        artistasViewModel.atualizarListadeArtistas();
        artistasViewModel.getArtistaPorUrl(artistaBundle.getUrl().split("/")[1]);
        artistasViewModel.getMusicasFavoritasDoArtista(artistaBundle.getUrl());


        listaTopLyrics = new ArrayList<>();
        listaLyrics = new ArrayList<>();

        artistasViewModel.getListaDeMusicasFavoritasDoBanco()
                .observe(this, listaMusicas->{
                    listaDeMusicasFavoritasDoBanco = listaMusicas;
                });

        artistasViewModel.getListaDeMusicasFavoritasDoArtista()
                .observe(this, listMusArt->{
                    listaDeMusicasFavoritasDoArtista = listMusArt;
                    if(txtButtonFavSongs){
                        swichListas("FAV");
                        artistaListaMusicasRecyclerAdapter.atualizarLista(listaDeMusicasFavoritasDoArtista, artistaApi);
                    }
                });


        artistasViewModel.getIsFavorito()
                .observe(this, isFav->{
                    isFavoritado = isFav;
                    if(isFavoritado){
                        seguirButton.setChecked(true);
                    }else{
                        seguirButton.setChecked(false);
                    }
                });

        artistaListaMusicasRecyclerAdapter = new ArtistaListaMusicasRecyclerAdapter(listaTopLyrics, this, artistaApi);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(artistaListaMusicasRecyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);

        artistasViewModel.getArtistaLiveData()
                .observe(this, apiArtista -> {
                    artistaApi = apiArtista;
                    if(artistaApi.isFavoritarArtista()){
                        seguirButton.setChecked(true);
                    }

                    nomeArtistaTextView.setText(artistaApi.getDesc());
                    Picasso.get()
                            .load(imgUrlBase+artistaApi.getPic_small())
                            .placeholder(R.drawable.placeholder_logo)
                            .into(imagemArtistaImageView);
//                    Picasso.get().load(imgUrlBase+artistaApi.getPic_medium()).into(artistaBg);
//                    Log.i(TAG, " GOT TOPLYR 01 EM ARTISTA: "+artistaApi.getToplyrics().getItem().get(0).getDesc());
                    listaTopLyrics = artistaApi.getToplyrics().getItem();
                    listaLyrics = artistaApi.getLyrics().getItem();

                    if(listaTopLyrics.size()==0 || listaTopLyrics==null){
                        swichListas("ALL");
                        artistaListaMusicasRecyclerAdapter.atualizarLista(listaLyrics, artistaApi);
                    }else{
                        swichListas("TOP");
                        artistaListaMusicasRecyclerAdapter.atualizarLista(listaTopLyrics, artistaApi);
                    }
                });


        buttonTextAllLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtButtonAllSongs){
                    swichListas("ALL");
                }
            }
        });

        buttonTextTopLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtButtonTopSongs) {
                    swichListas("TOP");
                }
            }
        });

        buttonTextFavLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtButtonFavSongs){
                    swichListas("FAV");
                }
            }
        });

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

    private void swichListas(String whatList){
        switch (whatList){
            case "TOP":
                txtButtonTopSongs = true;
                txtButtonAllSongs = false;
                txtButtonFavSongs = false;
                buttonTextTopLyrics.setTextAppearance(R.style.toggleTextSelected);
                buttonTextAllLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextFavLyrics.setTextAppearance(R.style.toggleTextOff);
                artistaListaMusicasRecyclerAdapter.atualizarLista(listaTopLyrics, artistaApi);
                if (listaTopLyrics.size() > 0) {
                    userFriendlyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    userFriendlyText.setText(txtFriendlyTop);
                    userFriendlyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                break;
            case "ALL":
                txtButtonTopSongs = false;
                txtButtonAllSongs = true;
                txtButtonFavSongs = false;
                buttonTextTopLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextAllLyrics.setTextAppearance(R.style.toggleTextSelected);
                buttonTextFavLyrics.setTextAppearance(R.style.toggleTextOff);
                artistaListaMusicasRecyclerAdapter.atualizarLista(listaLyrics, artistaApi);
                userFriendlyText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case "FAV":
                txtButtonTopSongs = false;
                txtButtonAllSongs = false;
                txtButtonFavSongs = true;
                buttonTextTopLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextAllLyrics.setTextAppearance(R.style.toggleTextOff);
                buttonTextFavLyrics.setTextAppearance(R.style.toggleTextSelected);
                artistaListaMusicasRecyclerAdapter.atualizarLista(listaDeMusicasFavoritasDoArtista, artistaApi);
                if (listaDeMusicasFavoritasDoBanco != null && listaDeMusicasFavoritasDoBanco.size() > 0) {
                    userFriendlyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    userFriendlyText.setText(txtFriendlyFavs);
                    userFriendlyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
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
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
