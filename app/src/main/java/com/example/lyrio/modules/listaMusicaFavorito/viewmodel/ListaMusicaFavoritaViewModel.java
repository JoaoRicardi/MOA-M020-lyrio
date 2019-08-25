package com.example.lyrio.modules.listaMusicaFavorito.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.repository.ListaMusicasRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListaMusicaFavoritaViewModel extends AndroidViewModel {

    private MutableLiveData<List<Musica>> listaMusicaLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Musica>> listaMusicaApi = new MutableLiveData<>();
    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    private ListaMusicasRepository musicasRepository = new ListaMusicasRepository();

    public MutableLiveData<List<Musica>> getListaMusicaLiveData() {
        return listaMusicaLiveData;
    }

    public MutableLiveData<List<Musica>> getListaMusicaApi() {
        return listaMusicaApi;
    }

    public ListaMusicaFavoritaViewModel(@NonNull Application application) {
        super(application);
    }

    public void atualizarListaMusicaGeral(){
        gerarListaMusicasFavoritas();
    }

    public void atualizarListaDeMusicaFavoritos() {
        disposable.add(
                musicasRepository.getAllMusicas(getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(musica -> {
                            listaMusicaLiveData.setValue(musica);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void gerarListaMusicasFavoritas() {
        disposable.add(
                musicasRepository.getAllMusicas(getApplication())
                        .map(listaMusica -> {
                            List<Musica> listaMusicaApi = new ArrayList<>();
                            for (Musica musica : listaMusica){
                                String theCurNum =  UUID.randomUUID()+"";
                                Musica musicaApi = musicasRepository.getMusicaPorIdApiV2(musica.getId(), theCurNum)
                                        .blockingFirst();
                                listaMusicaApi.add(musicaApi);
                            }
                            return listaMusicaApi;
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaMusicasList -> {
                            listaMusicaApi.setValue(listaMusicasList);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void favoritarMusica(Musica musica) {
        disposable.add(
                musicasRepository.favoritarMusica(musica, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListaDeMusicaFavoritos())
        );
    }

    public void removerMusica(Musica musica) {
        disposable.add(
                musicasRepository.removerMusicaPorId(musica.getId(), getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListaDeMusicaFavoritos())
        );
    }
}
