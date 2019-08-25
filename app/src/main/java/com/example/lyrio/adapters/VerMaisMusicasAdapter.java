package com.example.lyrio.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lyrio.R;
import com.example.lyrio.interfaces.VerMaisMusicaListener;
import com.example.lyrio.modules.musica.model.Musica;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerMaisMusicasAdapter extends RecyclerView.Adapter<VerMaisMusicasAdapter.ViewHolder> {
    private List<Musica> listaMusica;
    private VerMaisMusicaListener musicaListener;

    public VerMaisMusicasAdapter(VerMaisMusicaListener musicaListener){
        this.listaMusica = new ArrayList<>();
        this.musicaListener = musicaListener;
    }

    public void atualizarLista(List<Musica> listaMusica){
        this.listaMusica = listaMusica;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_ver_mais_musica, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Musica musicaSalva = listaMusica.get(position);
        holder.setupListaMusica(musicaSalva);

    }

    @Override
    public int getItemCount() {
        return listaMusica.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nomeMusica;
        private CircleImageView imagemArtista;
        private ToggleButton favoritarButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeMusica = itemView.findViewById(R.id.nome_musica_ver_mais_text_view);
            imagemArtista = itemView.findViewById(R.id.ver_mais_musica_imagem_artista_image_view);
            favoritarButton = itemView.findViewById(R.id.lista_ver_mais_musica_favorita_button);
        }

        public void setupListaMusica(Musica musicaSalva) {
            nomeMusica.setText(musicaSalva.getName());
            Picasso.get().load(musicaSalva.getArtista().getUrl()).placeholder(R.drawable.placeholder_logo).into(imagemArtista);
            favoritarButton.setChecked(true);
            favoritarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favoritarButton.setChecked(true);
                    musicaListener.desfavoritarMusica(musicaSalva);
                }
            });

        }
    }
}
