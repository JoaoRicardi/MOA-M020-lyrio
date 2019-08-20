package com.example.lyrio.modules.Artista.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.adapters.ArtistaSalvoAdapter;
import com.example.lyrio.modules.musica.view.TelaLetrasActivity;
import com.example.lyrio.adapters.ListaMusicasSalvasAdapter;
import com.example.lyrio.service.api.VagalumeBuscaApi;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.model.Album;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.interfaces.AlbumListener;
import com.example.lyrio.interfaces.ListaMusicasSalvasListener;
import com.example.lyrio.modules.Album.view.ListaAlbumActivity;
import com.example.lyrio.util.Constantes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class PaginaArtistaActivity extends AppCompatActivity implements ListaMusicasSalvasListener, AlbumListener {

    private CircleImageView imagemArtistaImageView;
    private TextView nomeArtistaTextView;
    private ToggleButton seguirButton;
    private ImageButton backButton;
    private ImageView artistaBg;
    private Retrofit retrofit;
    private ApiArtista artistaSalvo;
    private ListaMusicasSalvasAdapter listaMusicasSalvasAdapter;
    private ArtistaSalvoAdapter artistaSalvoAdapter;
    private List<Musica> listaDeMusicasSalvas;

    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_artista);

        // Iniciar retrofit para buscar infos da API
//        ArtistasViewModel artistasViewModel = ViewModelProviders.of(this).get(ArtistasViewModel.class);
//        artistasViewModel.atualizarArtista();

        //Definir as variaveis
        artistaBg = findViewById(R.id.artista_imagem_bg);
        nomeArtistaTextView = findViewById(R.id.artista_nome_artista_text_view);
        imagemArtistaImageView = findViewById(R.id.artista_profile_image_view);
        seguirButton = findViewById(R.id.letras_favorito_button);
//            backButton = findViewById(R.id.back_button_pagina_artista_image_button);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ApiArtista artistaSalvo = (ApiArtista) bundle.getSerializable("ARTISTA");

        if(artistaSalvo.getDesc()==null){
            listaDeMusicasSalvas = new ArrayList<>();
            String[] artSplit = artistaSalvo.getUrl().split("/");
            getApiData(artSplit[1]);
        }else{
            //Set variaveis
            nomeArtistaTextView.setText(artistaSalvo.getDesc());
            Picasso.get().load(artistaSalvo.getPic_small()).into(imagemArtistaImageView);
            Picasso.get().load(artistaSalvo.getPic_medium()).into(artistaBg);
            listaDeMusicasSalvas = artistaSalvo.getMusicasSalvas();
        }

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

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                voltarParaUltimaPagina();
//            }
//        });

        //Recycler com a lista de músicas que veio no Bundle
        listaMusicasSalvasAdapter = new ListaMusicasSalvasAdapter(listaDeMusicasSalvas, this, artistaSalvo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.pagina_artista_lista_musicas_recycler_view);
        recyclerView.setAdapter(listaMusicasSalvasAdapter);
        recyclerView.setLayoutManager(layoutManager);


//        List<Album> listaAlbum = new ArrayList<>();
//        Album album1 = new Album(R.drawable.paula_fernandes);
//        listaAlbum.add(album1);
//        Album album2 = new Album(R.drawable.u2);
//        listaAlbum.add(album2);
//        Album album3 = new Album(R.drawable.paula_fernandes);
//        listaAlbum.add(album3);
//        Album album4 = new Album(R.drawable.u2);
//        listaAlbum.add(album4);
//
//        AlbumAdapter albumAdapter = new AlbumAdapter(listaAlbum, this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
//        RecyclerView recyclerView1 = findViewById(R.id.pagina_artista_lista_albuns_recyler_view);
//        recyclerView1.setAdapter(albumAdapter);
//        recyclerView1.setLayoutManager(linearLayoutManager);


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

//        Log.i(TAG, " LETRA: "+musicaSalva.getText());

        Intent intent = new Intent(this, TelaLetrasActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("MUSICA", musicaSalva);
        intent.putExtras(bundle);

        startActivity(intent);
    }


    private void getApiData(String oQueBuscar) {

        Date curTime = Calendar.getInstance().getTime();
        oQueBuscar = oQueBuscar.trim().replace(" ", "-");
        String vagaKey = Constantes.VAGALUME_KEY + curTime.toString().trim().replace(" ","");
        String buscaFull = "https://www.vagalume.com.br/"+oQueBuscar+"/index.js";

//        VagalumeBuscaApi service = retrofit.create(VagalumeBuscaApi.class);
//        Call<VagalumeBusca> vagalumeBuscaCall = service.getBuscaResponse(buscaFull);
//        vagalumeBuscaCall.enqueue(new Callback<VagalumeBusca>() {
//            @Override
//            public void onResponse(Call<VagalumeBusca> call, Response<VagalumeBusca> response) {
//                if(response.isSuccessful()){
//                    VagalumeBusca vagalumeBusca = response.body();
//                        ApiArtista apiArtist = vagalumeBusca.getArtist();
//
//                        nomeArtistaTextView.setText(apiArtist.getDesc());
//                        Picasso.get().load("https://www.vagalume.com.br"+apiArtist.getPic_small()).into(imagemArtistaImageView);
//                        Picasso.get().load("https://www.vagalume.com.br"+apiArtist.getPic_medium()).into(artistaBg);
//
//                        Log.e(TAG, " NOME: "+apiArtist.getDesc());
//
//                        for(int i=0; i<apiArtist.getToplyrics().getItem().size(); i++){
//
//                            ApiItem curApi = apiArtist.getToplyrics().getItem().get(i);
//                            String url = "https://www.vagalume.com.br"+curApi.getUrl();
//
//                            //Conferir se temos problemas ------------------------------------------------------------------------------------
//                            Musica musicaTemp = new Musica(curApi.getId(),curApi.getDesc(),url);
//
////                            //Musica musicaTemp = new Musica(curApi.getId(),curApi.getDesc(),url);
////                            Musica musicaTemp = new Musica();
//
//                            musicaTemp.setAlbumPic("https://www.vagalume.com.br"+apiArtist.getPic_small());
//
//                            listaMusicasSalvasAdapter.adicionarMusica(musicaTemp);
//                        }
//
//                }else {
//                    Log.e(TAG, " onResponse: "+response.errorBody());}
//            }
//            @Override
//            public void onFailure(Call<VagalumeBusca> call, Throwable t){Log.e(TAG, " onFailure: "+t.getMessage());}
//        });
    }

}
