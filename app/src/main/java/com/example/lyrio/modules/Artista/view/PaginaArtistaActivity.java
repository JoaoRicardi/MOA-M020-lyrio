package com.example.lyrio.modules.Artista.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.adapters.ArtistaSalvoAdapter;
import com.example.lyrio.modules.Artista.viewmodel.ArtistasViewModel;
import com.example.lyrio.modules.musica.view.TelaLetrasActivity;
import com.example.lyrio.adapters.ListaMusicasSalvasAdapter;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.model.Album;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.interfaces.AlbumListener;
import com.example.lyrio.interfaces.ListaMusicasSalvasListener;
import com.example.lyrio.modules.Album.view.ListaAlbumActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class PaginaArtistaActivity extends AppCompatActivity implements ListaMusicasSalvasListener, AlbumListener {

    private CircleImageView imagemArtistaImageView;
    private TextView nomeArtistaTextView;
    private ToggleButton seguirButton;
    private ImageView artistaBg;
    private ListaMusicasSalvasAdapter listaMusicasSalvasAdapter;
    private List<Musica> listaDeMusicasSalvas;

    private List<Musica> listaTopLyrics;
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
//            backButton = findViewById(R.id.back_button_pagina_artista_image_button);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        artistaBundle = (ApiArtista) bundle.getSerializable("ARTISTA");

        artistasViewModel = ViewModelProviders.of(this).get(ArtistasViewModel.class);
        artistasViewModel.getArtistaPorUrl(artistaBundle.getUrl().split("/")[1]);

        listaTopLyrics = new ArrayList<>();

//        Recycler com a lista de músicas que veio no Bundle
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



//        if(artistaSalvo.getDesc()==null){
//            listaDeMusicasSalvas = new ArrayList<>();
//            String[] artSplit = artistaSalvo.getUrl().split("/");
//            getApiData(artSplit[1]);
//        }else{
//            //Set variaveis
//            nomeArtistaTextView.setText(artistaSalvo.getDesc());
//            Picasso.get().load(artistaSalvo.getPic_small()).into(imagemArtistaImageView);
//            Picasso.get().load(artistaSalvo.getPic_medium()).into(artistaBg);
//            listaDeMusicasSalvas = artistaSalvo.getMusicasSalvas();
//        }
//
//        seguirButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(seguirButton.isChecked()){
//                    Toast.makeText(PaginaArtistaActivity.this, Constantes.TOAST_ARTISTA_FAVORITO_EXCLUIR, Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(PaginaArtistaActivity.this, Constantes.TOAST_ARTISTA_FAVORITO_ADICIONAR, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



        //ja estava baixo

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                voltarParaUltimaPagina();
//            }
//        });

        //ja estava cima

    }

//    private void voltarParaUltimaPagina() {
//        if (getFragmentManager().getBackStackEntryCount() == 0) {
//            this.finish();
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }

    @Override
    public void onAlbumClicado(Album album) {
        Intent intent = new Intent(this, ListaAlbumActivity.class);
        startActivity(intent);
    }

    @Override
    public void onListaMusicasSalvasClicado(Musica musicaSalva) {

        Intent intent = new Intent(this, TelaLetrasActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("MUSICA_ID", musicaSalva.getId());
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
