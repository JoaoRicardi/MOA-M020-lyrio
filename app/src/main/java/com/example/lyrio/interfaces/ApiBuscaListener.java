package com.example.lyrio.interfaces;
import com.example.lyrio.service.model.ApiItem;

public interface ApiBuscaListener {

    void onApiBuscarClicado(ApiItem apiItem);
    void favoritarApiItem(ApiItem apiItem);
    void removerApiItem(ApiItem apiItem);

}
