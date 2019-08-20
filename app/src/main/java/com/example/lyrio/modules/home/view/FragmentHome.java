package com.example.lyrio.modules.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lyrio.R;
import com.example.lyrio.adapters.ArtistaSalvoAdapter;
import com.example.lyrio.adapters.MusicaSalvaAdapter;
import com.example.lyrio.adapters.NoticiaSalvaAdapter;
import com.example.lyrio.database.LyrioDatabase;
import com.example.lyrio.modules.home.viewModel.HomeViewModel;
import com.example.lyrio.modules.musica.view.TelaLetrasActivity;
import com.example.lyrio.service.api.VagalumeBuscaApi;
import com.example.lyrio.service.api.VagalumeHomeApi;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.ApiItem;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.database.models.NoticiaSalva;
import com.example.lyrio.interfaces.ArtistaSalvoListener;
import com.example.lyrio.interfaces.EnviarDeFragmentParaActivity;
import com.example.lyrio.interfaces.MusicaSalvaListener;
import com.example.lyrio.interfaces.NoticiaSalvaListener;
import com.example.lyrio.modules.listaArtistaFavorito.view.ListaArtistasSalvosActivity;
import com.example.lyrio.modules.Artista.view.PaginaArtistaActivity;
import com.example.lyrio.modules.listaMusicaFavorito.view.ListaMusicaSalvaActivity;
import com.example.lyrio.modules.listaNoticiaFavorito.view.ListaNoticiaSalvaActivity;
import com.example.lyrio.modules.noticia.view.NoticiaActivity;
import com.example.lyrio.modules.configuracoes.view.ConfiguracoesActivity;
import com.example.lyrio.modules.login.view.LoginActivity;


import java.util.ArrayList;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements ArtistaSalvoListener,
        MusicaSalvaListener,
        NoticiaSalvaListener,
        PopupMenu.OnMenuItemClickListener {

    public FragmentHome() {
        // Required empty public constructor
    }

    private String gotMail;
    private TextView userName;
    private TextView userStatus;
    private ImageButton opcoesUsuario;
    private TextView verMaisMusica;
    private TextView verMaisArtistas;
    private TextView verMaisNoticias;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LyrioDatabase db;

    //Interfaces
    private EnviarDeFragmentParaActivity enviarDeFragmentParaActivity;

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
//    private ArtistasViewModel artistasViewModel;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_home, container, false);
        db = Room.databaseBuilder(getContext(), LyrioDatabase.class, LyrioDatabase.DATABASE_NAME).build();

        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();



//        googleApiClient = new GoogleApiClient.Builder(FragmentHome.this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
//                .build();



        // Iniciar retrofit para buscar infos da API
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vagalume.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        artistasViewModel = ViewModelProviders.of(this).get(ArtistasViewModel.class);
//        artistasViewModel.atualizarArtista();


        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.atualizarListaMusica();
        homeViewModel.atualizarArtista();

        homeViewModel.getListaMusicaLiveData()
                .observe(this, listaMusicas->{
                    musicaSalvaAdapter.atualizarListaMusicas(listaMusicas);
                });

//        homeViewModel.getListaArtistaLiveData()
//                .observe(this,listarArtista->{
//                    gerarListaDeArtistas(listarArtista);
//                });

//        artistasViewModel = ViewModelProviders.of(this).get(ArtistasViewModel.class);
//        artistasViewModel.atualizarArtista();
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


        //Conteudo lista noticias
        List<NoticiaSalva> listaNoticiasSalvas = new ArrayList<>();
        NoticiaSalva noticiaSalva = new NoticiaSalva();
        noticiaSalva.setTituloNoticiaSalva("Dia do Rock!");
        noticiaSalva.setImagemNoticiaSalva("https://caisdamemoria.files.wordpress.com/2018/07/dia-mundial-do-rock.jpg?w=620");
        listaNoticiasSalvas.add(noticiaSalva);
        listaNoticiasSalvas.add(noticiaSalva);
        listaNoticiasSalvas.add(noticiaSalva);

        //Recycler noticias
        NoticiaSalvaAdapter noticiaSalvaAdapter = new NoticiaSalvaAdapter(listaNoticiasSalvas, this);
        GridLayoutManager gridNoticias = new GridLayoutManager(view.getContext(), 3);
        RecyclerView recyclerView2 = view.findViewById(R.id.noticias_salvas_recycler_view);
        recyclerView2.setAdapter(noticiaSalvaAdapter);
        recyclerView2.setLayoutManager(gridNoticias);


        opcoesUsuario = view.findViewById(R.id.home_user_icon_image_button);
        opcoesUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), opcoesUsuario);
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

        verMaisNoticias = view.findViewById(R.id.ver_mais_noticias_salvas_text_view);
        verMaisNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaMinhasNoticias();
            }
        });


        userName = view.findViewById(R.id.user_name_id);
        userStatus = view.findViewById(R.id.txtUserStatus);

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
            userName.setText(gotMail);
            userStatus.setText("Notificações ativas");
        } else {
            userName.setText("Faça seu login");
            userStatus.setText("Sem notificações");
        }

        swipeRefreshLayout = view.findViewById(R.id.home_swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            homeViewModel.atualizarListaMusica();

//                Toast.makeText(getActivity(), "bla", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
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


    private void irParaMinhasNoticias() {
        Intent intent = new Intent(getContext(), ListaNoticiaSalvaActivity.class);
        startActivity(intent);
    }

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
        for (int i = 0; i < apiArtista.getToplyrics().getItem().size(); i++) {

            ApiItem curApi = apiArtista.getToplyrics().getItem().get(i);
            String url = "https://www.vagalume.com.br" + curApi.getUrl();
            Musica musicaTemp = new Musica(curApi.getId(), curApi.getDesc(), url);
            musicaTemp.setAlbumPic(apiArtista.getPic_small());

            musicasSalvas.add(musicaTemp);

        }
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
    public void onNoticiaSalvaClicado(NoticiaSalva noticiaSalva) {
        Intent intent = new Intent(getContext(), NoticiaActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch ((menuItem.getItemId())) {
            case R.id.item_sair:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.item_editar_perfil:
                Intent intent02 = new Intent(getContext(), ConfiguracoesActivity.class);
                startActivity(intent02);
                return true;
            default:
                return false;
        }
    }


}
