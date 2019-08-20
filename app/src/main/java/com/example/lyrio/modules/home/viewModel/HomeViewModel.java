package com.example.lyrio.modules.home.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.adapters.ListaMusicasSalvasAdapter;
import com.example.lyrio.adapters.MusicaAdapter;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.repository.ArtistaRepository;
import com.example.lyrio.repository.BuscaRepository;
import com.example.lyrio.repository.ListaMusicasRepository;
import com.example.lyrio.service.api.VagalumeHomeApi;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.ApiItem;
import com.example.lyrio.service.model.VagalumeBusca;
import com.example.lyrio.util.Constantes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeViewModel extends AndroidViewModel {


    private MutableLiveData<List<ApiArtista>> listaArtistaLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Musica>> listaMusicaLiveData = new MutableLiveData<>();
    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private ListaMusicasRepository listaMusicasRepository = new ListaMusicasRepository();
    private ArtistaRepository listaArtistaRepository = new ArtistaRepository();
    private BuscaRepository buscaRepository = new BuscaRepository();
    private ListaMusicasSalvasAdapter musicaSalvaAdapter;

    public MutableLiveData<ApiArtista> getArtistaLiveData() {
        return artistaLiveData;
    }

    public MutableLiveData<Musica> getMusicaLiveData() {
        return musicaLiveData;
    }

    public MutableLiveData<List<ApiArtista>> getListaArtistaLiveData() {
        return listaArtistaLiveData;
    }

    public MutableLiveData<List<Musica>> getListaMusicaLiveData() {
        return listaMusicaLiveData;
    }

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void atualizarListaMusica(){
        disposable.add(
                listaMusicasRepository.getAllMusicas(getApplication())
                .map(listaMusica -> {
                    List<Musica> listaMusicaApi = new ArrayList<>();
                    for (Musica musica : listaMusica){
                        Musica musicaApi = listaMusicasRepository.getMusicaPorIdApi(musica.getId())
                                .blockingFirst();
                        listaMusicaApi.add(musicaApi);
                    }
                    return listaMusicaApi;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(listaMusica->{listaMusicaLiveData.setValue(listaMusica);
                },throwable -> throwable.printStackTrace() )
        );
    }
//    public void removerMusica(String musicaId){
//        disposable.delete(
//                listaMusicasRepository.removerMusica(musicaId,getApplication())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(listaMusica->{
//                    listaMusicaLiveData.setValue(listaMusica);
//                })
//        )
//    }

    public void atualizarArtista(){
        disposable.add(
                listaArtistaRepository.getAllArtistas(getApplication())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(listaArtista-> listaArtistaLiveData.setValue(listaArtista))
        );
    }
    public void getArtistaPorId(String stringId){
        disposable.add(
                listaArtistaRepository.getArtistaPorId(getApplication(), stringId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(artista -> {
                    artistaLiveData.setValue(artista);
                },throwable -> throwable.printStackTrace())
        );
    }


//    private void gerarListaDeArtistas(String[] nomesDosArtistas) {
//
//        // Iterar nomes de cada artista e buscar cada um na Api do Vagalume
//        for (int i = 0; i < nomesDosArtistas.length; i++) {
//
////            Log.i(TAG, " NOME RECEBIDO: "+nomesDosArtistas[i]);
//            getApiData(nomesDosArtistas[i], "artista");
//
//        }
//    }


    // Integração com API
//    private void getApiData(String oQueBuscar, String artistaOuMusica) {
//
//        Date curTime = Calendar.getInstance().getTime();
//
//        oQueBuscar = oQueBuscar.trim().replace(" ", "-");
//        String buscaFull = "";
//
//        String vagaKey = Constantes.VAGALUME_KEY + curTime.toString().trim().replace(" ","");
//
//        switch (artistaOuMusica) {
//            case "artista":
//                buscaFull = "https://www.vagalume.com.br/" + oQueBuscar + "/index.js";
//                break;
//            case "musica":
//                buscaFull = "https://api.vagalume.com.br/search.php?apikey=" + vagaKey + "&musid=" + oQueBuscar;
//                break;
//        }
//
//        VagalumeHomeApi service = retrofit.create(VagalumeHomeApi.class);
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
//
//
//
//
//    }
}
