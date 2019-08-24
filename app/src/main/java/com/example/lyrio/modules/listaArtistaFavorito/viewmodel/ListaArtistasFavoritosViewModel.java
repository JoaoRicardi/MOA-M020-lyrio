package com.example.lyrio.modules.listaArtistaFavorito.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.repository.ArtistaRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListaArtistasFavoritosViewModel extends AndroidViewModel {

    //Tag para LogCat
    private static final String TAG = "VAGALUME";

    //LiveDatas
    private MutableLiveData<List<ApiArtista>> listaDeArtistasDoBanco = new MutableLiveData<>();
    private MutableLiveData<List<ApiArtista>> listaArtistasBuscadosNaApi = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();

    //Repository
    private ArtistaRepository listaArtistasRepository = new ArtistaRepository();
    private CompositeDisposable disposable = new CompositeDisposable();

    public ListaArtistasFavoritosViewModel(@NonNull Application application) {
        super(application);
    }

    public void atualizarGeral(){
//        atualizarListaArtistasFavoritos();
        gerarListaDeArtistas();
    }

    //Métodos Ler Banco de dados
    public void gerarListaDeArtistas(){
        disposable.add(
                listaArtistasRepository.getAllArtistas(getApplication())
                        .map(listaArtista -> {
                            List<ApiArtista> listaArtistaGerado = new ArrayList<>();
                            for (ApiArtista apiArt : listaArtista){
                                ApiArtista artApi = listaArtistasRepository.getArtistaPorUrl(apiArt.getUrl())
                                        .blockingFirst();
                                artApi.setQtdMusicas(artApi.getLyrics().getItem().size());
                                listaArtistaGerado.add(artApi);
                            }
                            return listaArtistaGerado;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(listaArtistaGerado->{
                            listaArtistasBuscadosNaApi.setValue(listaArtistaGerado);
                        },throwable -> throwable.printStackTrace() )
        );
    }


    public void atualizarListaArtistasFavoritos(){
        disposable.add(
                listaArtistasRepository.getAllArtistas(getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaArtistas -> {
                            listaDeArtistasDoBanco.setValue(listaArtistas);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    //Métodos Escrever no Banco de dados
    public void favoritarArtista(ApiArtista apiArtista){
        disposable.add(
                listaArtistasRepository.favoritarArtista(apiArtista, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListaArtistasFavoritos())
        );
    }

    public void removerArtista(ApiArtista apiArtista){
        disposable.add(
                listaArtistasRepository.removerArtistaPorUrl(apiArtista.getUrl().replace("/",""), getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListaArtistasFavoritos())
        );
    }

    //Getter e setters

    public MutableLiveData<List<ApiArtista>> getListaArtistasBuscadosNaApi() {
        return listaArtistasBuscadosNaApi;
    }

    public void setListaArtistasBuscadosNaApi(MutableLiveData<List<ApiArtista>> listaArtistasBuscadosNaApi) {
        this.listaArtistasBuscadosNaApi = listaArtistasBuscadosNaApi;
    }

    public MutableLiveData<List<ApiArtista>> getListaDeArtistasDoBanco() {
        return listaDeArtistasDoBanco;
    }

    public void setListaDeArtistasDoBanco(MutableLiveData<List<ApiArtista>> listaDeArtistasDoBanco) {
        this.listaDeArtistasDoBanco = listaDeArtistasDoBanco;
    }

    public MutableLiveData<ApiArtista> getArtistaLiveData() {
        return artistaLiveData;
    }

    public void setArtistaLiveData(MutableLiveData<ApiArtista> artistaLiveData) {
        this.artistaLiveData = artistaLiveData;
    }
}
