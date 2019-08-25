package com.example.lyrio.interfaces;

import com.example.lyrio.modules.noticiasHotspot.model.Hotspot;

public interface HotspotListener {

    void onHotspotClicado(Hotspot hotspot);
    void onArtistaClicado(String artistaUrl);
}
