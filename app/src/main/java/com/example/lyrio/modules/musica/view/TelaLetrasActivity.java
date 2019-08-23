package com.example.lyrio.modules.musica.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.lyrio.R;
import com.example.lyrio.modules.musica.viewmodel.LetrasViewModel;
import com.example.lyrio.service.api.VagalumeBuscaApi;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.util.Constantes;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private String letraOriginal;
    private String letraTraduzida;


    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_letras);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String musicaSalvaId = bundle.getString("MUSICA_ID");

        letrasViewModel = ViewModelProviders.of(this).get(LetrasViewModel.class);

        traduzirButton = findViewById(R.id.button_traduzir_letra);
        nomeDaMusica = findViewById(R.id.letras_nome_musica_text_view);
        nomeDoArtista = findViewById(R.id.letras_nome_artista_text_view);
        letraDaMusica = findViewById(R.id.letras_letra_musica_text_view);
        imagemArtista = findViewById(R.id.letras_artist_pic);
        favourite_button = findViewById(R.id.letras_favorito_button);

        traduzirButton.setVisibility(View.GONE);
        letrasViewModel.getMusicaPorId(musicaSalvaId);
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


        letrasViewModel.getMusicaLiveData()
                .observe(this, musica -> {

                    letraOriginal = musica.getText();

                    Log.i(TAG, " CHECAR TRADUCAO:");
                    try{
                        hasTranslation = musica.getTranslate().get(0).getText()!=null;
                        if(hasTranslation){
                            traduzirButton.setVisibility(View.VISIBLE);
                            letraTraduzida = "\n\n"+musica.getTranslate().get(0).getText();
                            letraOriginal = "\n\n"+letraOriginal;
                        }
                        Log.i(TAG, " TRADUCAO: "+hasTranslation);
                    }catch(Exception e){
                        Log.i(TAG, " ERRO NA RESPOSTA");
                    }

                    letraDaMusica.setText(letraOriginal);
                    favourite_button.setSelected(musica != null);
                    nomeDaMusica.setText(musica.getName());
                    nomeDoArtista.setText(musica.getArtista().getName());
                    Picasso.get().load(musica.getArtista().getUrl()).into(imagemArtista);

                });

        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourite_button.isChecked()) {
                    Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_ADICIONAR, Toast.LENGTH_SHORT).show();
                    Musica musica = new Musica();
                    musica.setId(musicaSalvaId);
                    letrasViewModel.favoritarMusica(musica);

                } else {
                    Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_EXCLUIR, Toast.LENGTH_SHORT).show();

                    letrasViewModel.removerMusicaPorId(musicaSalvaId);
                }
            }
        });

    }

}
