package com.example.lyrio.interfaces;

import com.example.lyrio.modules.musica.model.Musica;

public interface ListaMusicasSalvasListener {
    void onListaMusicasSalvasClicado(Musica musicaSalva);
    void updateItemCount();
}
