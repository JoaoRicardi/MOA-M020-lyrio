package com.example.lyrio.modules.Artista.viewmodel;

import android.app.Application;

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
    private MutableLiveData<List<ApiArtista>> listaArtistaLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private ArtistaRepository apiArtistaRepository = new ArtistaRepository();
    private ListaMusicasRepository listaDeMusicasRepository = new ListaMusicasRepository();

    public ArtistasViewModel(@NonNull Application application) {
        super(application);
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
    public void atualizarListadeArtistas(){
        disposable.add(
                apiArtistaRepository.getAllArtistas(getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaArtistas -> {
                            listaArtistaLiveData.setValue(listaArtistas);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void getArtistaPorUrl(String urlArtista){
        disposable.add(
                apiArtistaRepository.getArtistaPorUrl(urlArtista)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiArt -> artistaLiveData.setValue(apiArt),
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
