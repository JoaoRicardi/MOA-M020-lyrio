package com.example.lyrio.modules.buscar.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.database.models.Musica;
import com.example.lyrio.model.BuscaLayout;
import com.example.lyrio.repository.BuscaRepository;
import com.example.lyrio.repository.ListaMusicasRepository;
import com.example.lyrio.service.model.ApiArtista;
import com.example.lyrio.service.model.ApiItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BuscarViewModel extends AndroidViewModel {

    private static final String TAG = "BuscarViewModel";

    private MutableLiveData<List<ApiArtista>> listaArtistasFavoritosLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();

    private MutableLiveData<List<Musica>> listaMusicasFavoritoLiveData = new MutableLiveData<>();
    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();
//    private String stringId;

    private MutableLiveData<List<ApiItem>> listaMusicasBuscadaLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ApiItem>> listaArtistaBuscadaLiveData = new MutableLiveData<>();
    private MutableLiveData<BuscaLayout> buscaLayoutLiveData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private ListaMusicasRepository listaMusicasRepository = new ListaMusicasRepository();
    private BuscaRepository buscaRepository = new BuscaRepository();

    public BuscarViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Musica>> getListaMusicasFavoritoLiveData(){
        return listaMusicasFavoritoLiveData;
    }

    public  MutableLiveData<Musica> getMusicaLiveData(){;
        return musicaLiveData;
    }

    public MutableLiveData<List<ApiItem>> getListaMusicasBuscadaLiveData() {
        return listaMusicasBuscadaLiveData;
    }


    public MutableLiveData<List<ApiArtista>> getListaArtistasFavoritosLiveData() {
        return listaArtistasFavoritosLiveData;
    }

    public MutableLiveData<ApiArtista> getArtistaLiveData() {
        return artistaLiveData;
    }


    public MutableLiveData<List<ApiItem>> getListaArtistaBuscadaLiveData() {
        return listaArtistaBuscadaLiveData;
    }

    public MutableLiveData<BuscaLayout> getBuscaLayoutLiveData() {
        return buscaLayoutLiveData;
    }

    public void getMusicaPorId(String stringId){
        disposable.add(
                listaMusicasRepository.getMusicaPorId(getApplication(), stringId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(musica -> {
                            musicaLiveData.setValue(musica);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void atualizarListaFavoritos(){
        disposable.add(
                listaMusicasRepository.getAllMusicas(getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listaMusicasList -> {
                            listaMusicasFavoritoLiveData.setValue(listaMusicasList);
                        }, throwable -> throwable.printStackTrace())
        );
    }

    public void favoritarMusica(Musica musica){
        disposable.add(
                listaMusicasRepository.favoritarMusica(musica, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListaFavoritos())
        );
    }

    public void removerMusica(Musica musica){
        disposable.add(
                listaMusicasRepository.removerMusica(musica, getApplication())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> atualizarListaFavoritos())
        );
    }

    public void fazerBusca(String termo) {
        disposable.add(
                buscaRepository.buscar(termo)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiItemList -> {
                            String gotResult = "";

                            ArrayList<ApiItem> listaDeArtistas = new ArrayList<>();
                            ArrayList<ApiItem> listaDeMusicas = new ArrayList<>();
                            
                            for(ApiItem apiItem: apiItemList){
                                String bandName = apiItem.getBand();
                                String songTitle = apiItem.getTitle();
                                String pgUrl = apiItem.getUrl();
                                String theId = apiItem.getId();
                                ApiItem curApiItem = new ApiItem();

                                if(bandName != null && songTitle != null){

                                    // Logar em txt
                                    gotResult = gotResult+"MÚSICA - "+bandName+" - "+songTitle+"\n";

                                    // Adicionar ao Recycler
                                    curApiItem.setBand(bandName);
                                    curApiItem.setCampoTop(songTitle);
                                    curApiItem.setCampoBottom(bandName);
                                    curApiItem.setUrl(pgUrl);
                                    curApiItem.setId(theId);
                                    listaDeMusicas.add(curApiItem);
                                    Log.i(TAG, " Musica API: " +curApiItem.getCampoTop());


                                } else if(songTitle == null){

                                    // Printar txt
                                    gotResult = gotResult+"ARTISTA - "+bandName+"\n";

                                    // Adicionar ao Recycler
                                    curApiItem.setBand(bandName);
                                    curApiItem.setCampoTop(bandName);
                                    curApiItem.setCampoBottom("Ver músicas");
                                    curApiItem.setUrl(pgUrl);
                                    listaDeArtistas.add(curApiItem);
                                    Log.i(TAG, " Artista_old API: " +curApiItem.getCampoTop());
                                }
                            }

                            // atualizar pelo LiveData
                            listaArtistaBuscadaLiveData.setValue(listaDeArtistas);
                            listaMusicasBuscadaLiveData.setValue(listaDeMusicas);

                            BuscaLayout buscaLayout = new BuscaLayout(listaDeArtistas.size() > 0, listaDeMusicas.size() > 0);
                            buscaLayoutLiveData.setValue(buscaLayout);

                        }, throwable -> throwable.printStackTrace())
        );
    }

}
