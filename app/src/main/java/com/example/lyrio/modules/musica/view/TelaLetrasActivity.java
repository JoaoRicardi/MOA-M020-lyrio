package com.example.lyrio.modules.musica.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.lyrio.R;
import com.example.lyrio.modules.musica.viewmodel.LetrasViewModel;
import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.util.Constantes;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TelaLetrasActivity extends AppCompatActivity {

    private TextView nomeDoArtista;
    private TextView nomeDaMusica;
    private TextView letraDaMusica;
    private CircleImageView imagemArtista;
    private ToggleButton favourite_button;
    private LetrasViewModel letrasViewModel;
    private boolean hasTranslation;
    private boolean curTranslation;
    private Button traduzirButton;
    private ImageView shareMusica;

//    private String musicaSalvaId;
    private boolean isFavourite;
    private Musica musicaBundle;
    private Musica musicaApi;

    private String letraOriginal;
    private String letraTraduzida;

    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_letras);

        nomeDaMusica = findViewById(R.id.letras_nome_musica_text_view);
        nomeDoArtista = findViewById(R.id.letras_nome_artista_text_view);
        letraDaMusica = findViewById(R.id.letras_letra_musica_text_view);
        imagemArtista = findViewById(R.id.letras_artist_pic);
        favourite_button = findViewById(R.id.letras_favorito_button);
        shareMusica = findViewById(R.id.share_musica_button);
        traduzirButton = findViewById(R.id.button_traduzir_letra);
        traduzirButton.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        musicaBundle = (Musica) bundle.getSerializable("MUSICA");

        try{
            musicaBundle.isFavoritarMusica();
            Log.i("VAGALUME", " MUSICA_FAVORITA: "+musicaBundle.isFavoritarMusica());
        }catch(Exception e){
            Log.e("VAGALUME", " BUNDLE SEM OPÇÃO DE MUSICA_FAVORITA");
        }

        if(musicaBundle.isFavoritarMusica()){
            favourite_button.setChecked(true);
        }


        letrasViewModel = ViewModelProviders.of(this).get(LetrasViewModel.class);
        letrasViewModel.getMusicaPorId(musicaBundle.getId());
        letrasViewModel.getMusicaLiveData()
                .observe(this, musica -> {
                    musicaApi = musica;

                    letraOriginal = musicaApi.getText();

//                    Log.i(TAG, " CHECAR TRADUCAO:");
                    try{
                        hasTranslation = musicaApi.getTranslate().get(0).getText()!=null;
                        if(hasTranslation){
                            traduzirButton.setVisibility(View.VISIBLE);
                            letraTraduzida = "\n\n"+musicaApi.getTranslate().get(0).getText();
                            letraOriginal = "\n\n"+letraOriginal;
//                            Log.i(TAG, " MÚSICA COM TRADUÇÃO");
                        }
                    }catch(Exception e){
//                        Log.i(TAG, " MÚSICA SEM TRADUÇÃO");
                    }

                    letraDaMusica.setText(letraOriginal);
                    favourite_button.setSelected(musicaApi != null);
                    nomeDaMusica.setText(musicaApi.getName());
                    nomeDoArtista.setText(musicaApi.getArtista().getName());
                    Picasso.get()
                            .load(musicaApi.getArtista().getUrl())
                            .placeholder(R.drawable.placeholder_logo)
                            .into(imagemArtista);
                });



        curTranslation = false;
        traduzirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curTranslation = !curTranslation;
                if(curTranslation){
                    letraDaMusica.setText(letraTraduzida);
                    traduzirButton.setText("Ver original");
                }else{
                    letraDaMusica.setText(letraOriginal);
                    traduzirButton.setText("Ver tradução");
                }
            }
        });


        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourite_button.isChecked()) {
                    Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_ADICIONAR, Toast.LENGTH_SHORT).show();

                    Musica musica = new Musica();
                    musica.setId(musicaApi.getId());

                    letrasViewModel.favoritarMusica(musica);
                    letrasViewModel.atualizarListadeMusicas();
                } else {
                    Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_EXCLUIR, Toast.LENGTH_SHORT).show();

                    Musica delMusic = new Musica();
                    delMusic.setId("l"+musicaApi.getId());

                    letrasViewModel.removerMusica(delMusic);
                    letrasViewModel.atualizarListadeMusicas();
                }
            }
        });

        shareMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letrasViewModel.getMusicaLiveData()
                        .observe(TelaLetrasActivity.this, musica -> {
                            compartilharMusica(musica);
                        });
            }
        });

    }

    private void compartilharMusica(Musica musica) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, musica.getName());

        intent.putExtra(Intent.EXTRA_TEXT, "***" + musica.getName() +"***" + "\n"+musica.getText());

        startActivity(Intent.createChooser(intent,  "Compartilhar"));
    }

}
