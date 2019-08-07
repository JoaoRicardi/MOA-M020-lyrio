package com.example.lyrio.interfaces;

import com.example.lyrio.service.model.ApiArtista;

import java.util.List;

public interface EnviarDeFragmentParaActivity {

    void enviarListaDeArtistas(List<ApiArtista> listaDeArtistas);

}
