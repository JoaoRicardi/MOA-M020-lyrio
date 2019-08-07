package com.example.lyrio.modules.listaMusicaFavorito.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lyrio.R;
import com.example.lyrio.adapters.MusicaSalvaAdapter;
import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.interfaces.ListaMusicasSalvasListener;

import java.util.List;

public class ListaMusicaSalvaActivity
        extends AppCompatActivity
        implements ListaMusicasSalvasListener {

    private ImageButton voltarButton;
    private List<Musica> listaMusicaSalva;
    private LyrioDatabase lyrioDatabase;
    private Musica musica;
    private MusicaSalvaAdapter musicaSalvaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_musica_salva);

//        lyrioDatabase = Room.databaseBuilder(this, LyrioDatabase.class, lyrioDatabase.DATABASE_NAME).build();

//        ListaMusicasSalvasAdapter listaMusicasSalvasAdapter = new ListaMusicasSalvasAdapter(listaMusicaSalva, this,musica.getArtista());
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        RecyclerView recyclerView = findViewById(R.id.minhas_musicas_salvas_recycler_view_id);
//        recyclerView.setAdapter(listaMusicasSalvasAdapter);
//        recyclerView.setLayoutManager(layoutManager);

        voltarButton = findViewById(R.id.back_button_minhas_musicas_image_button);
        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarParaHome();
            }
        });

//        exibirMusica();


    }
    private void exibirMusica () {
//        lyrioDatabase.musicasFavoritasDao()
//                .getAll()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(listaMusicaFavorita -> musicaSalvaAdapter.exibirMusicaFavorita(listaMusicaFavorita),
//                        throwable -> throwable.printStackTrace());

    }

    private void voltarParaHome() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onListaMusicasSalvasClicado(Musica musicaSalva) {
//        Intent intent = new Intent(this, TelaLetrasActivity.class);
//        startActivity(intent);
    }
}
