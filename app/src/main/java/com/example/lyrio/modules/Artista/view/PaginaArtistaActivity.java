package com.example.lyrio.modules.Artista.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.example.lyrio.adapters.ListaMusicasSalvasAdapter;
import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.model.Album;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.interfaces.AlbumListener;
import com.example.lyrio.interfaces.ListaMusicasSalvasListener;
import com.example.lyrio.modules.Album.view.ListaAlbumActivity;
import com.example.lyrio.util.Constantes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaginaArtistaActivity extends AppCompatActivity implements ListaMusicasSalvasListener{

    private CircleImageView imagemArtistaImageView;
    private TextView nomeArtistaTextView;
    private ToggleButton seguirButton;
    private ImageView artistaBg;
    private ListaMusicasSalvasAdapter listaMusicasSalvasAdapter;
    private List<Musica> listaDeMusicasSalvas;

    private List<Musica> listaTopLyrics;
    private List<Musica> listaLyrics;
    private String imgUrlBase = "https://www.vagalume.com";
    private ApiArtista artistaBundle;
    private ApiArtista artistaApi;
    private ArtistasViewModel artistasViewModel;

    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_artista_sem_album);

        //Definir as variaveis
        artistaBg = findViewById(R.id.artista_imagem_bg);
        nomeArtistaTextView = findViewById(R.id.artista_nome_artista_text_view);
        imagemArtistaImageView = findViewById(R.id.artista_profile_image_view);
        seguirButton = findViewById(R.id.letras_favorito_button);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        artistaBundle = (ApiArtista) bundle.getSerializable("ARTISTA");

        if(artistaBundle.isFavoritarArtista()){
            seguirButton.setChecked(true);
        }

        artistasViewModel = ViewModelProviders.of(this).get(ArtistasViewModel.class);
        artistasViewModel.getArtistaPorUrl(artistaBundle.getUrl().split("/")[1]);

        listaTopLyrics = new ArrayList<>();
        listaLyrics = new ArrayList<>();


        listaMusicasSalvasAdapter = new ListaMusicasSalvasAdapter(listaTopLyrics, this, artistaApi);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.pagina_artista_lista_musicas_recycler_view);
        recyclerView.setAdapter(listaMusicasSalvasAdapter);
        recyclerView.setLayoutManager(layoutManager);

        artistasViewModel.getArtistaLiveData()
                .observe(this, apiArtista -> {
                    artistaApi = apiArtista;
                    nomeArtistaTextView.setText(artistaApi.getDesc());
                    Picasso.get().load(imgUrlBase+artistaApi.getPic_small()).into(imagemArtistaImageView);
                    Picasso.get().load(imgUrlBase+artistaApi.getPic_medium()).into(artistaBg);
//                    Log.i(TAG, " GOT TOPLYR 01 EM ARTISTA: "+artistaApi.getToplyrics().getItem().get(0).getDesc());
                    listaTopLyrics = artistaApi.getToplyrics().getItem();
                    listaMusicasSalvasAdapter.atualizarLista(listaTopLyrics, artistaApi);
                });

        seguirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seguirButton.isChecked()){
                    Toast.makeText(PaginaArtistaActivity.this, Constantes.TOAST_ARTISTA_FAVORITO_EXCLUIR, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PaginaArtistaActivity.this, Constantes.TOAST_ARTISTA_FAVORITO_ADICIONAR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onListaMusicasSalvasClicado(Musica musicaSalva) {

        Musica tempMusic = new Musica();
        tempMusic.setId(musicaSalva.getId());

//        for(int y=0; y<listaMusicasFavoritas.size(); y++){
//            if(listaMusicasFavoritas.get(y).getUrl().equals(musicaSalva.getUrl().replace("/",""))) {
//                tempMusic.setFavoritarMusica(true);
//            }
//        }

        Intent intent = new Intent(this, TelaLetrasActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("MUSICA_ID", tempMusic.getId());
        bundle.putBoolean("MUSICA_FAVORITA", tempMusic.isFavoritarMusica());
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
