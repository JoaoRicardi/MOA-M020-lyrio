package com.example.lyrio.modules.home.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.adapters.MusicaAdapter;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.repository.ArtistaRepository;
import com.example.lyrio.repository.ListaMusicasRepository;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.ApiItem;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {


    private MutableLiveData<List<ApiArtista>> listaArtistaLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Musica>> listaMusicaLiveData = new MutableLiveData<>();
    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private ListaMusicasRepository listaMusicasRepository = new ListaMusicasRepository();
    private ArtistaRepository listaArtistaRepository = new ArtistaRepository();

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(listaMusica->listaMusicaLiveData.setValue(listaMusica) )
        );
    }
    public void getMusicaPorId(String stringId){
        disposable.add(
                listaMusicasRepository.getMusicaPorId(getApplication(), stringId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(musica -> {
                            musicaLiveData.setValue(musica);
                        },throwable -> throwable.printStackTrace())
        );
    }
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
}
