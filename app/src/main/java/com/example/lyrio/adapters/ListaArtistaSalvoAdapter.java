package com.example.lyrio.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.interfaces.ListaArtistasSalvosListener;
import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListaArtistaSalvoAdapter extends RecyclerView.Adapter<ListaArtistaSalvoAdapter.ViewHolder> {
    private static final String TAG = "VAGALUME";

    private List<ApiArtista> listaArtistaSalvo;
    private ListaArtistasSalvosListener artistaSalvoListener;

    public ListaArtistaSalvoAdapter(ListaArtistasSalvosListener artistaSalvoListener) {
        listaArtistaSalvo = new ArrayList<>();
        this.artistaSalvoListener = artistaSalvoListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celula_lista_ver_mais_artistas,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ApiArtista artistaSalvo = listaArtistaSalvo.get(i);
        viewHolder.setupArtistaSalvo(artistaSalvo);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artistaSalvoListener.abrirPaginaDoArtista(artistaSalvo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaArtistaSalvo.size();
    }

//    public void adicionarArtista(ApiArtista artistaSalvo){
//        listaArtistaSalvo.add(artistaSalvo);
//        notifyDataSetChanged();
//    }

    public void atualizarListaDeArtistas(List<ApiArtista> listaDeArtistas) {
        removerTudo();
        listaArtistaSalvo.addAll(listaDeArtistas);
        notifyDataSetChanged();
    }

    public void removerTudo(){
        while(listaArtistaSalvo.size()>0){
            listaArtistaSalvo.remove(0);
        }
        notifyDataSetChanged();
    }

    public void removerArtista(ApiArtista apiArt){
        listaArtistaSalvo.remove(apiArt);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profilePic;
        private TextView nomeArtista;
        private ToggleButton toggleButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.lista_ver_mais_artistas_img_artista);
            nomeArtista = itemView.findViewById(R.id.lista_ver_mais_artistas_nome_artista);
            toggleButton = itemView.findViewById(R.id.lista_ver_mais_artistas_favorito_button);
        }
        public void setupArtistaSalvo(ApiArtista artistaSalvo){
            toggleButton.setChecked(true);
            nomeArtista.setText(artistaSalvo.getDesc());
            Picasso.get().load("https://www.vagalume.com"+artistaSalvo.getPic_small()).into(profilePic);
//            Log.i(TAG, " ArtistaSalvoAdapter picSmall: "+artistaSalvo.getPic_small());

            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleButton.setChecked(true);
                    artistaSalvoListener.desfavoritarArtista(artistaSalvo);
                }
            });
        }
    }
}
