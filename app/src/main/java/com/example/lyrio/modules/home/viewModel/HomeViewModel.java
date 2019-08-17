package com.example.lyrio.modules.home.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.adapters.MusicaAdapter;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.repository.ArtistaRepository;
import com.example.lyrio.repository.BuscaRepository;
import com.example.lyrio.repository.ListaMusicasRepository;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.ApiItem;
import com.example.lyrio.service.model.VagalumeBusca;

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
    private BuscaRepository buscaRepository = new BuscaRepository();

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
                .subscribe(listaMusica->{listaMusicaLiveData.setValue(listaMusica);
                },throwable -> throwable.printStackTrace() )
        );
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
    public void receberBusca(VagalumeBusca vagalumeBusca, String termo){
        disposable.add(
                buscaRepository.buscar(termo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(apiItemList->{


                    if (vagalumeBusca.getArt() != null) {
                        ApiArtista apiArtista = vagalumeBusca.getArt();
                        ApiItem apiMusica = vagalumeBusca.getMus().get(0);

                        ApiArtista artistaRecebido = new ApiArtista();
                        artistaRecebido.setId(apiArtista.getId());
                        artistaRecebido.setName(apiArtista.getName());
                        artistaRecebido.setUrl(apiArtista.getUrl());
                        artistaRecebido.setPic_small(apiArtista.getUrl() + "images/profile.jpg");

                        // Logcat com tag VAGALUME
//                        Log.i(TAG, " RETROFIT url imagem: "+artistaRecebido.getPic_small());

                        Musica musicaRecebida = new Musica();
                        musicaRecebida.setId(apiMusica.getId());
                        musicaRecebida.setName(apiMusica.getName());
                        musicaRecebida.setUrl(apiMusica.getUrl());
                        musicaRecebida.setLang(apiMusica.getLang());
                        musicaRecebida.setText(apiMusica.getText());
                        musicaRecebida.setAlbumPic(artistaRecebido.getPic_small());
                        musicaRecebida.setArtista(apiArtista);

                    } else {
                        ApiArtista apiArtist = vagalumeBusca.getArtist();

                        ApiArtista artistaRecebido = new ApiArtista();
                        artistaRecebido.setDesc(apiArtist.getDesc());
                        artistaRecebido.setPic_small("https://www.vagalume.com.br" + apiArtist.getPic_small());
                        artistaRecebido.setPic_medium("https://www.vagalume.com.br" + apiArtist.getPic_medium());
                        artistaRecebido.setQtdMusicas(apiArtist.getLyrics().getItem().size());
                        artistaRecebido.setToplyrics(apiArtist.getToplyrics());
                    }

        },throwable -> throwable.printStackTrace())
        );

    }
}
