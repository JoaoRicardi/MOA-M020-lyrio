package com.example.lyrio.modules.listaArtistaFavorito.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.adapters.ArtistaSalvoAdapter;
import com.example.lyrio.adapters.ListaArtistaSalvoAdapter;
import com.example.lyrio.interfaces.ListaArtistasSalvosListener;
import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.interfaces.ArtistaSalvoListener;
import com.example.lyrio.interfaces.EnviarDeFragmentParaActivity;
import com.example.lyrio.modules.Artista.view.PaginaArtistaActivity;
import com.example.lyrio.modules.configuracoes.view.ConfiguracoesActivity;
import com.example.lyrio.modules.listaArtistaFavorito.viewmodel.ListaArtistasFavoritosViewModel;

import java.util.List;

public class ListaArtistasSalvosActivity
        extends AppCompatActivity
        implements ListaArtistasSalvosListener {

    private ImageButton backButton;
    private ListaArtistaSalvoAdapter listaArtistaSalvoAdapter;
    private List<ApiArtista> listaArtistaSalvo;
    private ListaArtistasFavoritosViewModel listArtFavViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_artistas_salvos);

        listArtFavViewModel = ViewModelProviders.of(this).get(ListaArtistasFavoritosViewModel.class);
        listArtFavViewModel.atualizarListaArtistasFavoritos();

        listArtFavViewModel.gerarListaDeArtistas();
        listArtFavViewModel.getListaArtistasBuscadosNaApi()
                .observe(this, listaArtistas->{
                    listaArtistaSalvo = listaArtistas;
                    listaArtistaSalvoAdapter.atualizarListaDeArtistas(listaArtistaSalvo);
                });

        listaArtistaSalvoAdapter = new ListaArtistaSalvoAdapter(this);
        RecyclerView.LayoutManager listaArtistaManager = new LinearLayoutManager(this);
        RecyclerView recyclerView1 = findViewById(R.id.lista_artistas_salvos_recycler_view_id);
        recyclerView1.setAdapter(listaArtistaSalvoAdapter);
        recyclerView1.setLayoutManager(listaArtistaManager);

        backButton = findViewById(R.id.back_button_meus_artistas_image_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarParaHome();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listArtFavViewModel.atualizarGeral();
    }


    @Override
    public void abrirPaginaDoArtista(ApiArtista artistaSalvo) {
//        Log.i("VAGALUME", " Método abrir página do artista chamado!");
        ApiArtista apiArtista = new ApiArtista();
        apiArtista.setUrl(artistaSalvo.getUrl());
        apiArtista.setFavoritarArtista(true);

        Intent intent = new Intent(this, PaginaArtistaActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("ARTISTA", apiArtista);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void desfavoritarArtista(ApiArtista artistaSalvo) {
//        Log.i("VAGALUME", " Método desfavoritar artista chamado!");
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("PERAÊ!!")
                .setMessage("Deseja realmente remover esta música dos seus favoritos?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listArtFavViewModel.removerArtista(artistaSalvo);
                        listaArtistaSalvoAdapter.removerArtista(artistaSalvo);
                    }
                })
                .setNegativeButton("NÃO", null);
        alert.create();
        alert.show();
    }


    private void voltarParaHome() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

}
