package com.example.lyrio.modules.home.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.adapters.ListaMusicasSalvasAdapter;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.repository.ArtistaRepository;
import com.example.lyrio.repository.BuscaRepository;
import com.example.lyrio.repository.ListaMusicasRepository;
import com.example.lyrio.modules.Artista.model.ApiArtista;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "VAGALUME";

    private MutableLiveData<List<ApiArtista>> listaArtistaLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Musica>> listaMusicaLiveData = new MutableLiveData<>();
    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();

    //Favoritos do Banco
    private ListaMusicasRepository listaMusicasRepository = new ListaMusicasRepository();
    private ArtistaRepository listaArtistaRepository = new ArtistaRepository();

    private CompositeDisposable disposable = new CompositeDisposable();



    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void atualizarTodosOsFavoritos(){
        atualizarListaMusica();
        atualizarListaArtistas();
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

    public void atualizarListaArtistas() {
        disposable.add(
                listaArtistaRepository.getAllArtistas(getApplication())
                        .map(listaArtista -> {
                            List<ApiArtista> listaArtistaGerado = new ArrayList<>();
                            for (ApiArtista apiArt : listaArtista){
                                ApiArtista artApi = listaArtistaRepository.getArtistaPorUrl(apiArt.getUrl())
                                        .blockingFirst();
                                artApi.setQtdMusicas(artApi.getLyrics().getItem().size());
                                listaArtistaGerado.add(artApi);
                            }
                            return listaArtistaGerado;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(listaArtistaGerado->{listaArtistaLiveData.setValue(listaArtistaGerado);
                        },throwable -> throwable.printStackTrace() )
        );
    }


    //Getter e Setters
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
}
