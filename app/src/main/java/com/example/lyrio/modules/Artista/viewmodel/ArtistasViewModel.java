package com.example.lyrio.modules.Artista.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.repository.ArtistaRepository;
import com.example.lyrio.repository.ListaMusicasRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ArtistasViewModel extends AndroidViewModel {
    private MutableLiveData<List<Musica>> listaDeMusicasFavoritasDoBanco = new MutableLiveData<>();
    private MutableLiveData<List<Musica>> listaDeMusicasFavoritasDoArtista = new MutableLiveData<>();
    private MutableLiveData<List<ApiArtista>> listaArtistaLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFavorito = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private ArtistaRepository apiArtistaRepository = new ArtistaRepository();
    private ListaMusicasRepository listaDeMusicasRepository = new ListaMusicasRepository();

    public ArtistasViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Musica>> getListaDeMusicasFavoritasDoArtista() {
        return listaDeMusicasFavoritasDoArtista;
    }

    public void setListaDeMusicasFavoritasDoArtista(MutableLiveData<List<Musica>> listaDeMusicasFavoritasDoArtista) {
        this.listaDeMusicasFavoritasDoArtista = listaDeMusicasFavoritasDoArtista;
    }

    public MutableLiveData<Boolean> getIsFavorito() {
        return isFavorito;
    }

    public void setIsFavorito(MutableLiveData<Boolean> isFavorito) {
        this.isFavorito = isFavorito;
    }

    public MutableLiveData<List<ApiArtista>> getListaArtistaLiveData(){
        return listaArtistaLiveData;
    }

    public MutableLiveData<ApiArtista> getArtistaLiveData(){
        return artistaLiveData;
    }

    public MutableLiveData<List<Musica>> getListaDeMusicasFavoritasDoBanco() {
        return listaDeMusicasFavoritasDoBanco;
    }

    public void setListaDeMusicasFavoritasDoBanco(MutableLiveData<List<Musica>> listaDeMusicasFavoritasDoBanco) {
        this.listaDeMusicasFavoritasDoBanco = listaDeMusicasFavoritasDoBanco;
    }

//    public void atualizarTudo(){
//        getMusicasFavoritasDoBanco();
//        getMusicasFavoritasDoArtista(artistaLiveData.getValue().getUrl());
//        atualizarListadeArtistas();
//    }

    public void getMusicasFavoritasDoBanco(){
        disposable.add(
                listaDeMusicasRepository.getAllMusicas(getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaFavMus -> {
                            listaDeMusicasFavoritasDoBanco.setValue(listaFavMus);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void getMusicasFavoritasDoArtista(String urlDoArtista){
        disposable.add(
                listaDeMusicasRepository.getAllMusicasDoArtista(getApplication(), urlDoArtista.split("/")[1])
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaMusArt -> {
                            listaDeMusicasFavoritasDoArtista.setValue(listaMusArt);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void atualizarListadeArtistas(){
        disposable.add(
                apiArtistaRepository.getAllArtistas(getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaArtistas -> {
                            listaArtistaLiveData.setValue(listaArtistas);
//                            for(int i=0; i<listaArtistas.size(); i++){
//                                if(listaArtistas.get(i).getUrl().equals(artistaLiveData.getValue().getUrl())){
//                                    isFavorito.setValue(true);
//                                    break;
//                                }
//                            }
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void getArtistaPorUrl(String urlArtista){
        disposable.add(
                apiArtistaRepository.getArtistaPorUrl(urlArtista)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiArt -> {
                            ApiArtista theArt = new ApiArtista();
                            theArt = apiArt;
//                            Log.i("VAGALUME", " valor na lista: "+listaArtistaLiveData.getValue().get(0).getUrl()+
//                                    ", valor api: "+apiArt.getUrl());
                            for(int i=0; i<listaArtistaLiveData.getValue().size(); i++){
                                if(listaArtistaLiveData.getValue().get(i).getUrl().equals(theArt.getUrl().split("/")[1])){
                                    theArt.setFavoritarArtista(true);
                                }
                            }
                            artistaLiveData.setValue(theArt);
                        },
                        throwable -> throwable.printStackTrace())
        );
    }

    public void favoritarArtista(ApiArtista artista){
        disposable.add(
                apiArtistaRepository.favoritarArtista(artista, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListadeArtistas())
        );
    }
    public void removerArtista(ApiArtista artista){
        disposable.add(
                apiArtistaRepository.removerArtistaPorUrl(artista.getUrl().replace("/",""), getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListadeArtistas())
        );
    }

}
