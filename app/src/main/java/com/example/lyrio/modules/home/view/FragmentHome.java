package com.example.lyrio.modules.home.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.lyrio.R;
import com.example.lyrio.adapters.ArtistaSalvoAdapter;
import com.example.lyrio.adapters.MusicaSalvaAdapter;
import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.modules.home.viewModel.HomeViewModel;
import com.example.lyrio.modules.menu.view.MainActivity;
import com.example.lyrio.modules.musica.view.TelaLetrasActivity;
import com.example.lyrio.service.api.VagalumeBuscaApi;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.ApiItem;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.interfaces.ArtistaSalvoListener;
import com.example.lyrio.interfaces.EnviarDeFragmentParaActivity;
import com.example.lyrio.interfaces.MusicaSalvaListener;
import com.example.lyrio.modules.listaArtistaFavorito.view.ListaArtistasSalvosActivity;
import com.example.lyrio.modules.Artista.view.PaginaArtistaActivity;
import com.example.lyrio.modules.listaMusicaFavorito.view.ListaMusicaSalvaActivity;
import com.example.lyrio.modules.configuracoes.view.ConfiguracoesActivity;
import com.example.lyrio.modules.login.view.LoginActivity;
import com.example.lyrio.util.Constantes;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements ArtistaSalvoListener,
        MusicaSalvaListener,
        PopupMenu.OnMenuItemClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LyrioDatabase db;

    public FragmentHome() {}

    private String gotMail;
    private TextView userName;
    private TextView userStatus;
    private CircleImageView ImagemUsuario;
    private TextView verMaisMusica;
    private TextView verMaisArtistas;
    private TextView sairBotao;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GoogleApiClient googleApiClient;

    //Adapters
    private ArtistaSalvoAdapter artistaSalvoAdapter;
    private MusicaSalvaAdapter musicaSalvaAdapter;

    //Listas
    private List<ApiArtista> listaArtistaSalvo;
    private List<Musica> listaMusicaSalva;
    private List<Musica> listaMusicaDoBanco;

    //Integração Api
    private Retrofit retrofit;

    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";

    //Room ETC
    private HomeViewModel homeViewModel;


    //Implantação do LOGIN com Google
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr =  Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result.getSignInAccount());
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result.getSignInAccount());
                }
            });

        }

    }




    // Login com Google
    private void handleSignInResult(GoogleSignInAccount account) {
        if (account != null){
           userName.setText(account.getDisplayName());
            Glide.with(this).load(account.getPhotoUrl()).into(ImagemUsuario);

        }else {
//            goLogInScreen();
        }

    }

    // Login com Google
    private void goLogInScreen() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Logout com Google
    private void logOut(View view){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
            if(status.isSuccess()){
 //              goLogInScreen();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(getContext(),"logOut não foi possível", Toast.LENGTH_LONG).show();
            }
            }
        });

    }

    // Login com Google ///
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_home, container, false);
        db = Room.databaseBuilder(getContext(), LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();


        userName = view.findViewById(R.id.user_name_id);
        userStatus = view.findViewById(R.id.sair_aplicativo_id);

        sairBotao = view.findViewById(R.id.sair_button);
        sairBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(view);
                FirebaseAuth.getInstance().signOut();
            }
        });





        // Login com Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        try {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        } catch(Exception error){
            Log.e("ExceptionGoogle", error.getMessage());
        }





        // receber informações de um Usuario
        setupUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();


        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.atualizarListaMusica();
//        homeViewModel.atualizarArtista();
        homeViewModel.gerarArtistas();

        homeViewModel.getListaMusicaLiveData()
                .observe(this, listaMusicas->{
                    musicaSalvaAdapter.atualizarListaMusicas(listaMusicas);
                });

        homeViewModel.getListaArtistaLiveData()
                .observe(this, listaArtistas->{
                    artistaSalvoAdapter.adicionarListaDeArtistas(listaArtistas);
                });

//
//
//
//        listaMusicasViewModel = ViewModelProviders.of(this).get(ListaMusicasViewModel.class);
//        listaMusicasViewModel.atualizarLista();
//
//        //Gerar lista de musicas a partir do Banco
//        listaMusicasViewModel.getListaMusicasLiveData()
//                .observe(this, listaMusicas -> {
//                    gerarListaDeMusicasPeloBanco(listaMusicas);
//                });

        musicaSalvaAdapter = new MusicaSalvaAdapter(this);
        GridLayoutManager gridMusicas = new GridLayoutManager(view.getContext(), 4);
        RecyclerView recyclerView = view.findViewById(R.id.musica_salva_recycler_view);
        recyclerView.setAdapter(musicaSalvaAdapter);
        recyclerView.setLayoutManager(gridMusicas);

        artistaSalvoAdapter = new ArtistaSalvoAdapter(this);
        GridLayoutManager gridArtistas = new GridLayoutManager(view.getContext(), 4);
        RecyclerView recyclerView1 = view.findViewById(R.id.artistas_salvos_recycler_view);
        recyclerView1.setAdapter(artistaSalvoAdapter);
        recyclerView1.setLayoutManager(gridArtistas);


        ImagemUsuario = view.findViewById(R.id.home_user_icon_image_button);
        ImagemUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), ImagemUsuario);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(FragmentHome.this);
            }
        });




        verMaisArtistas = view.findViewById(R.id.ver_mais_artistas_salvos_text_view);
        verMaisArtistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaMeusArtistas();
            }
        });

        verMaisMusica = view.findViewById(R.id.ver_mais_musica_text_view);
        verMaisMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaMinhasMusicas();
            }
        });



        userName = view.findViewById(R.id.user_name_id);
        userStatus = view.findViewById(R.id.sair_aplicativo_id);

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaLogin();
            }
        });


        try {
            gotMail = getActivity().getIntent().getExtras().getString("EMAIL");
        } catch (Exception e) {
            gotMail = null;
        }

        if (gotMail != null) {

//            userStatus.setText("Notificações ativas");
        } else {
            userName.setText("Faça seu login");
//            userStatus.setText("Sem notificações");
        }

        swipeRefreshLayout = view.findViewById(R.id.home_swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            homeViewModel.atualizarListaMusica();

//                Toast.makeText(getActivity(), "bla", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupUser();
    }

    private void setupUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            userName.setText(email);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void irParaLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

//    private void atualizarTudo() {
//        homeViewModel.getListaMusicaLiveData()
//                .observe(this, listaMusicas -> {
//                    gerarListaDeMusicasPeloBanco(listaMusicas);
//                });
//        homeViewModel.getListaArtistaLiveData()
//                .observe(this,listaArtista->{
//                    artistaSalvoAdapter.adicionarListaDeArtistas(listaArtista);
//                });
//    }
//
//    private void gerarListaDeMusicasPeloBanco(List<Musica> musicList) {
//
//        if(musicList!=null){
//            for (int i = 0; i < musicList.size(); i++) {
//                getApiData(musicList.get(i).getId(), "musica");
//            }
//        }
//    }

    private void irParaMinhasMusicas() {
        Intent intent = new Intent(getContext(), ListaMusicaSalvaActivity.class);

        startActivity(intent);
    }


    private void irParaMeusArtistas() {

        Intent intent = new Intent(getContext(), ListaArtistasSalvosActivity.class);
        startActivity(intent);
    }


    @Override
    public void onArtistaClicado(ApiArtista artistaSalvo) {

        ApiArtista apiArtista = new ApiArtista();
        apiArtista.setDesc(artistaSalvo.getDesc());
        apiArtista.setPic_small(artistaSalvo.getPic_small());
        apiArtista.setPic_medium(artistaSalvo.getPic_medium());
        apiArtista.setUrl(artistaSalvo.getUrl());


        //Gerar lista para enviar ao bundle
        apiArtista.setMusicasSalvas(gerarListaDeMusicas(artistaSalvo));

        Intent intent = new Intent(getContext(), PaginaArtistaActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("ARTISTA", apiArtista);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private List<Musica> gerarListaDeMusicas(ApiArtista apiArtista) {

        //Gerar lista de musicas para enviar ao bundle
        List<Musica> musicasSalvas = new ArrayList<>();
//        for (int i = 0; i < apiArtista.getToplyrics().getItem().size(); i++) {
//
//            ApiItem curApi = apiArtista.getToplyrics().getItem().get(i);
//            String url = "https://www.vagalume.com.br" + curApi.getUrl();
//            Musica musicaTemp = new Musica(curApi.getId(), curApi.getDesc(), url);
//            musicaTemp.setAlbumPic(apiArtista.getPic_small());
//
//            musicasSalvas.add(musicaTemp);
//
//        }
        return musicasSalvas;
    }

    @Override
    public void onMusicaSalvaClicado(Musica musicaSalva) {

        Intent intent = new Intent(getContext(), TelaLetrasActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("MUSICA_ID", musicaSalva.getId());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch ((menuItem.getItemId())) {
            case R.id.item_sair:
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            case R.id.item_editar_perfil:
                Intent intent02 = new Intent(getContext(), ConfiguracoesActivity.class);
                startActivity(intent02);
                return true;
            default:
                return false;
        }
    }


    private void gerarListaDeMusicasPeloBanco(List<Musica> musicList) {

        if(musicList!=null){
            for (int i = 0; i < musicList.size(); i++) {
                getApiData(musicList.get(i).getId(), "musica");
            }
        }
    }


//    private List<Musica> gerarListaDeMusicas(ApiArtista apiArtista) {
//
//        //Gerar lista de musicas para enviar ao bundle
//        List<Musica> musicasSalvas = new ArrayList<>();
//        for (int i = 0; i < apiArtista.getToplyrics().getItem().size(); i++) {
//
//            ApiItem curApi = apiArtista.getToplyrics().getItem().get(i);
//            String url = "https://www.vagalume.com.br" + curApi.getUrl();
//            Musica musicaTemp = new Musica(curApi.getId(), curApi.getDesc(), url);
//            musicaTemp.setAlbumPic(apiArtista.getPic_small());
//
//            musicasSalvas.add(musicaTemp);
//
//        }
//        return musicasSalvas;
//    }



    // Integração com API
    private void getApiData(String oQueBuscar, String artistaOuMusica) {

        Date curTime = Calendar.getInstance().getTime();

        oQueBuscar = oQueBuscar.trim().replace(" ", "-");
        String buscaFull = "";

        String vagaKey = Constantes.VAGALUME_KEY + curTime.toString().trim().replace(" ","");

        switch (artistaOuMusica) {
            case "artista":
                buscaFull = "https://www.vagalume.com.br/" + oQueBuscar + "/index.js";
                break;
            case "musica":
                buscaFull = "https://api.vagalume.com.br/search.php?apikey=" + vagaKey + "&musid=" + oQueBuscar;
                break;
        }

        VagalumeBuscaApi service = retrofit.create(VagalumeBuscaApi.class);
//        Call<VagalumeBusca> vagalumeBuscaCall = service.getBuscaResponse(buscaFull);
//        vagalumeBuscaCall.enqueue(new Callback<VagalumeBusca>() {
//            @Override
//            public void onResponse(Call<VagalumeBusca> call, Response<VagalumeBusca> response) {
//                if (response.isSuccessful()) {
//                    VagalumeBusca vagalumeBusca = response.body();
//
//                    if (vagalumeBusca.getArt() != null) {
//                        ApiArtista apiArtista = vagalumeBusca.getArt();
//                        ApiItem apiMusica = vagalumeBusca.getMus().get(0);
//
//                        ApiArtista artistaRecebido = new ApiArtista();
//                        artistaRecebido.setId(apiArtista.getId());
//                        artistaRecebido.setName(apiArtista.getName());
//                        artistaRecebido.setUrl(apiArtista.getUrl());
//                        artistaRecebido.setPic_small(apiArtista.getUrl() + "images/profile.jpg");
//
//                        // Logcat com tag VAGALUME
////                        Log.i(TAG, " RETROFIT url imagem: "+artistaRecebido.getPic_small());
//
//                        Musica musicaRecebida = new Musica();
//                        musicaRecebida.setId(apiMusica.getId());
//                        musicaRecebida.setName(apiMusica.getName());
//                        musicaRecebida.setUrl(apiMusica.getUrl());
//                        musicaRecebida.setLang(apiMusica.getLang());
//                        musicaRecebida.setText(apiMusica.getText());
//                        musicaRecebida.setAlbumPic(artistaRecebido.getPic_small());
//                        musicaRecebida.setArtista(apiArtista);
//
//                        //Adicionar a lista de Musicas
////                        listaMusicaSalva.add(musicaRecebida);
//
//                        //Adicionar ao Adapter do RecyclerView
//                        musicaSalvaAdapter.adicionarMusica(musicaRecebida);
//
//
//                    } else {
//                        ApiArtista apiArtist = vagalumeBusca.getArtist();
//
//                        ApiArtista artistaRecebido = new ApiArtista();
//                        artistaRecebido.setDesc(apiArtist.getDesc());
//                        artistaRecebido.setPic_small("https://www.vagalume.com.br" + apiArtist.getPic_small());
//                        artistaRecebido.setPic_medium("https://www.vagalume.com.br" + apiArtist.getPic_medium());
//                        artistaRecebido.setQtdMusicas(apiArtist.getLyrics().getItem().size());
//                        artistaRecebido.setToplyrics(apiArtist.getToplyrics());
//
//                        //Adicionar a lista de Artistas
//                        listaArtistaSalvo.add(artistaRecebido);
//
//                        //Adicionar ao Adapter do RecyclerView
//                        artistaSalvoAdapter.adicionarArtista(artistaRecebido);
//                    }
//
//                } else {
//                    Log.e(TAG, " onResponse: " + response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<VagalumeBusca> call, Throwable t) {
//                Log.e(TAG, " onFailure: " + t.getMessage());
//            }
//        });




    }


}
