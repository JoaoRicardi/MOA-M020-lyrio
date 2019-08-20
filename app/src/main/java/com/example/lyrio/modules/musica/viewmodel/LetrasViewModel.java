package com.example.lyrio.modules.musica.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.database.models.Musica;
import com.example.lyrio.repository.ListaMusicasRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LetrasViewModel extends AndroidViewModel {

    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private ListaMusicasRepository listaMusicasRepository = new ListaMusicasRepository();

    public LetrasViewModel(@NonNull Application application) {
        super(application);
    }

    public  MutableLiveData<Musica> getMusicaLiveData(){
        return musicaLiveData;
    }

    public void getMusicaPorId(String musicaId){
        disposable.add(
                listaMusicasRepository.getMusicaPorIdApi(musicaId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(musica -> {
                            musicaLiveData.setValue(musica);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void favoritarMusica(Musica musica){
        disposable.add(
                listaMusicasRepository.favoritarMusica(musica, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }

    public void removerMusicaPorId(String musicaId){
        disposable.add(
                listaMusicasRepository.removerMusicaPorId(musicaId, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }
}
