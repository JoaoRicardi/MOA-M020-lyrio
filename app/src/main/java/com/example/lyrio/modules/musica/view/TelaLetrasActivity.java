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
import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.modules.Artista.view.PaginaArtistaActivity;
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
    private String urlDoArtista;

    private String letraOriginal;
    private String letraTraduzida;

    private View divTraducao;
    private TextView buttonTextVerTradução;
    private TextView buttonTextVerOriginal;
    private boolean txtButtonTrad;
    private boolean txtButtonOrig;


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
//        traduzirButton = findViewById(R.id.button_traduzir_letra);
        divTraducao = findViewById(R.id.div_traducao);
        buttonTextVerOriginal = findViewById(R.id.tela_letras_ver_original);
        buttonTextVerTradução = findViewById(R.id.tela_letras_ver_traducao);

        buttonTextVerOriginal.setVisibility(View.GONE);
        buttonTextVerTradução.setVisibility(View.GONE);
        divTraducao.setVisibility(View.GONE);

        buttonTextVerOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TelaLetrasActivity.this, "VER ORIGINAL", Toast.LENGTH_SHORT).show();
                if(!txtButtonOrig){
                    txtButtonOrig = true;
                    txtButtonTrad = false;
                    letraDaMusica.setText(letraOriginal);
                    buttonTextVerOriginal.setTextAppearance(R.style.toggleTextSelected);
                    buttonTextVerTradução.setTextAppearance(R.style.toggleTextOff);
                }
            }
        });

        buttonTextVerTradução.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(TelaLetrasActivity.this, "VER TRADUCAO", Toast.LENGTH_SHORT).show();
                if(!txtButtonTrad){
                    txtButtonOrig = false;
                    txtButtonTrad = true;
                    letraDaMusica.setText(letraTraduzida);
                    buttonTextVerOriginal.setTextAppearance(R.style.toggleTextOff);
                    buttonTextVerTradução.setTextAppearance(R.style.toggleTextSelected);
                }
            }
        });



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

                    musicaApi.setDesc(musica.getName());
                    letraOriginal = "\n"+musicaApi.getText();
                    urlDoArtista = "/"+musicaApi.getArtista().getUrl().split("/")[3]+"/";
//                    Log.i(TAG, " GOT URL PARA ARTISTA => "+urlDoArtista.split("/")[3]);
//                    Log.i(TAG, " CHECAR TRADUCAO:");
                    try{
                        hasTranslation = musicaApi.getTranslate().get(0).getText()!=null;
                        if(hasTranslation){
                            buttonTextVerOriginal.setVisibility(View.VISIBLE);
                            buttonTextVerTradução.setVisibility(View.VISIBLE);
                            divTraducao.setVisibility(View.VISIBLE);
                            txtButtonTrad = false;
                            txtButtonOrig = true;
                            buttonTextVerOriginal.setTextAppearance(R.style.toggleTextSelected);

                            letraTraduzida = "\n"+musicaApi.getTranslate().get(0).getText();
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

        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourite_button.isChecked()) {
                    Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_ADICIONAR, Toast.LENGTH_SHORT).show();

                    Musica musica = new Musica();
                    musica.setId(musicaApi.getId());
                    musica.setDesc(musicaApi.getDesc());
                    musica.setUrlArtista(urlDoArtista.split("/")[1]);
                    Log.i(TAG, musica.getUrlArtista());

                    letrasViewModel.favoritarMusica(musica);
                    letrasViewModel.atualizarListadeMusicas();
                } else {
                    Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_EXCLUIR, Toast.LENGTH_SHORT).show();

                    Musica delMusic = new Musica();
                    delMusic.setId(musicaApi.getId());


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

        nomeDoArtista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaPaginaDoArtista();
            }
        });
        imagemArtista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaPaginaDoArtista();
            }
        });

    }

    private void irParaPaginaDoArtista(){

        ApiArtista apiArtista = new ApiArtista();
        apiArtista.setUrl(urlDoArtista);
//        apiArtista.setFavoritarArtista(true);

        Intent intent = new Intent(this, PaginaArtistaActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("ARTISTA", apiArtista);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    private void compartilharMusica(Musica musica) {
        String chamadaLyrio = "Dá uma olhada na música que eu encontrei no Lyrio:\n\n";

        //Definir se o compartilhamento sera feito com a letra traduzida ou original
        String letra;
        if(txtButtonOrig){
            letra = letraOriginal;
        }else{
            letra = letraTraduzida;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, musicaApi.getArtista().getName() +" - "+ musicaApi.getDesc());
        intent.putExtra(Intent.EXTRA_TEXT, chamadaLyrio+"*** " + musicaApi.getArtista().getName() +" - "+ musicaApi.getDesc() +" ***" + "\n"+letra);

        startActivity(Intent.createChooser(intent,  "Compartilhar"));
    }

}
