package com.example.lyrio.modules.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.api.base_vagalume.ApiArtista;
import com.example.lyrio.modules.repository.ArtistaRepository;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ArtistasViewModel extends AndroidViewModel {
    private MutableLiveData<List<ApiArtista>> listaArtistaLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista>  artistaLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private ArtistaRepository apiArtistaRepository = new ArtistaRepository();

    public ArtistasViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<ApiArtista>> getListaArtistaLiveData(){
        return listaArtistaLiveData;
    }

    public MutableLiveData<ApiArtista> getArtistaLiveData(){
        return artistaLiveData;
    }

    private ApiArtista tempArtista;

    private void getArtistaPorId(String stringId){
        disposable.add(
                apiArtistaRepository.getArtistaPorId(getApplication(),stringId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(artista -> {
                    artistaLiveData.setValue(artista);
                    tempArtista = artista;
                },throwable -> throwable.printStackTrace())
        );
    }

    public void atualizarArtista(){
        disposable.add(
                apiArtistaRepository.getArtistasList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(artistasList -> listaArtistaLiveData.setValue(artistasList),
                                throwable -> throwable.printStackTrace()
                        )
        );
    }
    public void favoritarArtista(ApiArtista artista){
        disposable.add(
                apiArtistaRepository.favoritarArtista(artista, getApplication())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(()->atualizarArtista())
        );
    }
    public void removerArtista(ApiArtista artista){
        disposable.add(
                apiArtistaRepository.removerArtista(artista, getApplication())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(()->atualizarArtista())
        );
    }
    public void removerArtistaPorId(String artistaId){
        disposable.add(
                apiArtistaRepository.removerArtistaPorId(artistaId,getApplication())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(()->atualizarArtista())
        );
    }
}
