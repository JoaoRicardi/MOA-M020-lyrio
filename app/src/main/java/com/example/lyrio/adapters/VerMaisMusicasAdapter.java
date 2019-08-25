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

public class VerMaisMusicasAdapter extends RecyclerView.Adapter<VerMaisMusicasAdapter.ViewHolder>{
    private List<Musica> listaMusica;
    private VerMaisMusicaListener musicaSalvaListener;

    public VerMaisMusicasAdapter(VerMaisMusicaListener musicaSalvaListener){
        listaMusica= new ArrayList<>();
        this.musicaSalvaListener = musicaSalvaListener;
    }
    public void atualizarVerMaisMusicas(List<Musica> listaVerMusica) {
        listaMusica.addAll(listaVerMusica);
        removerTudo();
        notifyDataSetChanged();
    }
    public void removerTudo(){
        while(listaMusica.size()>0){
            listaMusica.remove(0);
        }
        notifyDataSetChanged();
    }
    public void removerMusicaSalva(Musica musica){
        listaMusica.remove(musica);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_ver_mais_musica,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Musica musica = listaMusica.get(i);
        holder.setupMusica(musica);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicaSalvaListener.abrirLetrasMusica(musica);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listaMusica.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView artistaImageView;
        private TextView nomeMusicaTextView;
        private ToggleButton toggleButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artistaImageView = itemView.findViewById(R.id.ver_mais_imagem_artista_image_view);
            nomeMusicaTextView = itemView.findViewById(R.id.nome_musica_ver_mais_text_view);
            toggleButton = itemView.findViewById(R.id.lista_ver_mais_musica_favorita_button);
        }

        public void setupMusica(Musica musica) {
            toggleButton.setChecked(true);
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    musicaSalvaListener.desfavoritarMusica(musica);
                }
            });
            nomeMusicaTextView.setText(musica.getName());
            Picasso.get().load(musica.getArtista().getUrl()).into(artistaImageView);
        }
    }
}
