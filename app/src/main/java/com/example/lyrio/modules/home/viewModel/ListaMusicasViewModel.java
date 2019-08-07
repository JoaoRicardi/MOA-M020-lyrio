package com.example.lyrio.modules.home.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.database.models.Musica;
import com.example.lyrio.repository.ListaMusicasRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListaMusicasViewModel extends AndroidViewModel {

    private MutableLiveData<List<Musica>> listaMusicasLiveData = new MutableLiveData<>();
    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();
//    private String stringId;

    private CompositeDisposable disposable = new CompositeDisposable();

    private ListaMusicasRepository listaMusicasRepository = new ListaMusicasRepository();

    public ListaMusicasViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Musica>> getListaMusicasLiveData(){
        return listaMusicasLiveData;
    }

    public  MutableLiveData<Musica> getMusicaLiveData(){;
        return musicaLiveData;
    }

    private Musica tempMusica;

    public void getMusicaPorId(String stringId){
        disposable.add(
                listaMusicasRepository.getMusicaPorId(getApplication(), stringId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(musica -> {
                            musicaLiveData.setValue(musica);
                            tempMusica = musica;
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void atualizarLista(){
        disposable.add(
                listaMusicasRepository.getAllMusicas(getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaMusicasList -> {
                            listaMusicasLiveData.setValue(listaMusicasList);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void favoritarMusica(Musica musica){
        disposable.add(
                listaMusicasRepository.favoritarMusica(musica, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarLista())
        );
    }

    public void removerMusica(Musica musica){
        disposable.add(
                listaMusicasRepository.removerMusica(musica, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarLista())
        );
    }
    public void removerMusicaPorId(String musicaId){
        disposable.add(
                listaMusicasRepository.removerMusicaPorId(musicaId, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarLista())
        );
    }
}
