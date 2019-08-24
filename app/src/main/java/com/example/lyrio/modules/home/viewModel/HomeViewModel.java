package com.example.lyrio.modules.home.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lyrio.adapters.ListaMusicasSalvasAdapter;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.repository.ArtistaRepository;
import com.example.lyrio.repository.BuscaRepository;
import com.example.lyrio.repository.ListaMusicasRepository;
import com.example.lyrio.modules.Artista.model.ApiArtista;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "VAGALUME";


    private MutableLiveData<List<ApiArtista>> listaArtistaLiveData = new MutableLiveData<>();
    private MutableLiveData<ApiArtista> artistaLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Musica>> listaMusicaLiveData = new MutableLiveData<>();
    private MutableLiveData<Musica> musicaLiveData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private ListaMusicasRepository listaMusicasRepository = new ListaMusicasRepository();
    private ArtistaRepository listaArtistaRepository = new ArtistaRepository();
    private BuscaRepository buscaRepository = new BuscaRepository();
    private ListaMusicasSalvasAdapter musicaSalvaAdapter;

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
                .map(listaMusica -> {
                    List<Musica> listaMusicaApi = new ArrayList<>();
                    for (Musica musica : listaMusica){
                        Musica musicaApi = listaMusicasRepository.getMusicaPorIdApi(musica.getId())
                                .blockingFirst();
                        listaMusicaApi.add(musicaApi);
                    }
                    return listaMusicaApi;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(listaMusica->{listaMusicaLiveData.setValue(listaMusica);
                },throwable -> throwable.printStackTrace() )
        );
    }


    public void atualizarListaArtistas() {
        disposable.add(
                listaArtistaRepository.getAllArtistas(getApplication())
                        .map(listaArtista -> {
                            List<ApiArtista> listaArtistaGerado = new ArrayList<>();
                            for (ApiArtista apiArt : listaArtista){
                                ApiArtista artApi = listaArtistaRepository.getArtistaPorUrl(apiArt.getUrl())
                                        .blockingFirst();
                                artApi.setQtdMusicas(artApi.getLyrics().getItem().size());
                                listaArtistaGerado.add(artApi);
                            }
                            return listaArtistaGerado;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(listaArtistaGerado->{listaArtistaLiveData.setValue(listaArtistaGerado);
                        },throwable -> throwable.printStackTrace() )
        );
    }

//    public void atualizarListaArtistas() {
//        List<ApiArtista> listaGerada = new ArrayList<>();
//
//        String listaString = "skank,matisyahu";
//        String[] listaHard = listaString.split(",");
//
//        // Iterar nomes de cada artista e buscar cada um na Api do Vagalume
//        for (int i = 0; i < listaHard.length; i++) {
//            disposable.add(
//                    listaArtistaRepository.getArtistaPorUrl(listaHard[i])
//                            .subscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(apiArt -> {
//                                artistaLiveData.setValue(apiArt);
//                                        listaGerada.add(apiArt);
//                                        Log.i(TAG, " GERANDO ARTISTA: "+apiArt.getDesc());
//                                        listaArtistaLiveData.setValue(listaGerada);
//
//
//                                    },
//                            throwable -> throwable.printStackTrace())
//            );
//        }
//
//        listaArtistaLiveData.setValue(listaGerada);
//        Log.i(TAG, " LISTA ARTISTA SIZE: "+listaGerada.size());
//    }




//    public void removerMusica(String musicaId){
//        disposable.delete(
//                listaMusicasRepository.removerMusica(musicaId,getApplication())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(listaMusica->{
//                    listaMusicaLiveData.setValue(listaMusica);
//                })
//        )
//    }

    public void atualizarArtista(){
        disposable.add(
                listaArtistaRepository.getAllArtistas(getApplication())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(listaArtista-> listaArtistaLiveData.setValue(listaArtista))
        );
    }
    public void getArtistaPorUrl(String stringUrl){
        disposable.add(
                listaArtistaRepository.getArtistaPorUrl(stringUrl)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiArt -> artistaLiveData.setValue(apiArt),
                                throwable -> throwable.printStackTrace())
        );
    }
}
