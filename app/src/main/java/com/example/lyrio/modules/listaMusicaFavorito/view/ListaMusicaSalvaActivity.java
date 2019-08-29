package com.example.lyrio.modules.listaMusicaFavorito.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.adapters.VerMaisMusicasAdapter;
import com.example.lyrio.interfaces.VerMaisMusicaListener;
import com.example.lyrio.modules.listaMusicaFavorito.viewmodel.ListaMusicaFavoritaViewModel;
import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.modules.musica.view.TelaLetrasActivity;

import java.util.List;

public class ListaMusicaSalvaActivity
        extends AppCompatActivity
        implements VerMaisMusicaListener {

    private ImageButton voltarButton;
    private List<Musica> listaMusicaSalva;
    private VerMaisMusicasAdapter musicaSalvaAdapter;
    private ListaMusicaFavoritaViewModel listaMusicaViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_musica_salva);

        listaMusicaViewModel = ViewModelProviders.of(this).get(ListaMusicaFavoritaViewModel.class);

        listaMusicaViewModel.atualizarListaDeMusicaFavoritos();
        listaMusicaViewModel.gerarListaMusicasFavoritas();

        listaMusicaViewModel.getListaMusicaApi()
                .observe(this, listamusica->{
                    listaMusicaSalva = listamusica;
                    musicaSalvaAdapter.atualizarLista(listaMusicaSalva);
                });

        RecyclerView recyclerView = findViewById(R.id.minhas_musicas_salvas_recycler_view_id);
        musicaSalvaAdapter = new VerMaisMusicasAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(musicaSalvaAdapter);

        voltarButton = findViewById(R.id.back_button_minhas_musicas_image_button);
        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarParaHome();
            }
        });

    }

    private void voltarParaHome() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        listaMusicaViewModel.atualizarListaMusicaGeral();
    }

    @Override
    public void abrirLetrasMusica(Musica musicaSalva) {
        Musica musica = new Musica();
        musica.setId(musicaSalva.getId());
        musica.setFavoritarMusica(true);

        Intent intent = new Intent(this, TelaLetrasActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("MUSICA", musica);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void desfavoritarMusica(Musica musicaSalva) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("PERAÊ!!")
                .setMessage("Deseja realmente remover esta música dos seus favoritos?")
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listaMusicaViewModel.removerMusica(musicaSalva);
                        musicaSalvaAdapter.removerMusica(musicaSalva);
                    }
                })
                .setNegativeButton("NÃO", null);
        alert.create();
        alert.show();
    }
}
