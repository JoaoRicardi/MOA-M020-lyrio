package com.example.lyrio;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.lyrio.adapters.MusicaSalvaAdapter;
import com.example.lyrio.api.base_vagalume.VagalumeBusca;
import com.example.lyrio.api.VagalumeBuscaApi;
import com.example.lyrio.data.LyrioDatabase;
import com.example.lyrio.models.Musica;
import com.example.lyrio.util.Constantes;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaLetras extends AppCompatActivity {

    private TextView nomeDoArtista;
    private TextView nomeDaMusica;
    private TextView letraDaMusica;
    private CircleImageView imagemArtista;
    private Retrofit retrofit;
    private Musica musicaSelecionada;
    private ToggleButton favourite_button;


    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";
    private LyrioDatabase lyrioDatabase;
    private MusicaSalvaAdapter musicaSalvaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_letras);

        lyrioDatabase = Room.databaseBuilder(this, LyrioDatabase.class, lyrioDatabase.DATABASE_NAME).build();

        exibirMusica();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Musica musicaSalva = (Musica) bundle.getSerializable("MUSICA");


        // Iniciar retrofit para buscar infos da API
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vagalume.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        musicaSelecionada = new Musica();

        nomeDaMusica = findViewById(R.id.letras_nome_musica_text_view);
        nomeDoArtista = findViewById(R.id.letras_nome_artista_text_view);
        letraDaMusica = findViewById(R.id.letras_letra_musica_text_view);
        imagemArtista = findViewById(R.id.letras_artist_pic);
        favourite_button = findViewById(R.id.letras_favorito_button);

        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favourite_button.isChecked()){
                    Toast.makeText(TelaLetras.this, Constantes.TOAST_MUSICA_FAVORITA_ADICIONAR, Toast.LENGTH_SHORT).show();
                    gravarMusica(musicaSalva);
                }else{
                    Toast.makeText(TelaLetras.this, Constantes.TOAST_MUSICA_FAVORITA_EXCLUIR, Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(musicaSalva.getText()!=null){
            nomeDaMusica.setText(musicaSalva.getName());
            nomeDoArtista.setText(musicaSalva.getArtista().getName());
            letraDaMusica.setText(musicaSalva.getText());
            Picasso.get().load(musicaSalva.getAlbumPic()).into(imagemArtista);
        }else{
            getApiData(musicaSalva.getId());
        }
    }
    private void deletarMusica(Musica musica){
        Completable.fromAction(() -> lyrioDatabase.musicasFavoritasDao().delete(musica))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void exibirMusica () {
        lyrioDatabase.musicasFavoritasDao()
                .getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(listaMusicaFavorita -> musicaSalvaAdapter.exibirMusicaFavorita(listaMusicaFavorita),
                        throwable -> throwable.printStackTrace());

    }

    private void gravarMusica (Musica musica){
        Completable.fromAction(() -> lyrioDatabase.musicasFavoritasDao().inserir(musica))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }


    private void atualizarProduto (Musica musica){
        Completable.fromAction(() -> lyrioDatabase.musicasFavoritasDao().update(musica))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }
    public void favoritarMusica () {
    }

    private void getApiData(String idDaMusica) {

        Date curTime = Calendar.getInstance().getTime();

        idDaMusica = idDaMusica.trim().replace(" ", "-");
        String vagaKey =  Constantes.VAGALUME_KEY + curTime.toString().trim().replace(" ","");
        String buscaFull = "https://api.vagalume.com.br/search.php?apikey="+vagaKey+"&musid="+idDaMusica;

        VagalumeBuscaApi service = retrofit.create(VagalumeBuscaApi.class);
        Call<VagalumeBusca> vagalumeBuscaCall = service.getBuscaResponse(buscaFull);
        vagalumeBuscaCall.enqueue(new Callback<VagalumeBusca>() {
            @Override
            public void onResponse(Call<VagalumeBusca> call, Response<VagalumeBusca> response) {
                if(response.isSuccessful()){
                    VagalumeBusca vagalumeBusca = response.body();
//
//                    ApiArtista apiArtista = vagalumeBusca.getArt();
//                    ApiItem apiMusica = vagalumeBusca.getMus().get(0);

                    //Adicionar aos campos do xml
                    nomeDaMusica.setText(vagalumeBusca.getMus().get(0).getName());
                    nomeDoArtista.setText(vagalumeBusca.getArt().getName());
                    letraDaMusica.setText(vagalumeBusca.getMus().get(0).getText());
                    Picasso.get().load(vagalumeBusca.getArt().getUrl()+"images/profile.jpg").into(imagemArtista);

                }else {Log.e(TAG, " onResponse: "+response.errorBody());}
            }

            @Override
            public void onFailure(Call<VagalumeBusca> call, Throwable t){Log.e(TAG, " onFailure: "+t.getMessage());}
        });
    }
}
