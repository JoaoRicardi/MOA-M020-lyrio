package com.example.lyrio.interfaces;
import com.example.lyrio.api.base_vagalume.ApiItem;

public interface ApiBuscaListener {

    void onApiBuscarClicado(ApiItem apiItem);
    void favoritarApiItem(ApiItem apiItem);
    void removerApiItem(ApiItem apiItem);

}
