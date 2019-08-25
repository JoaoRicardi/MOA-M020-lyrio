package com.example.lyrio.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.R;
import com.example.lyrio.interfaces.MusicaSalvaListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MusicaSalvaAdapter extends RecyclerView.Adapter<MusicaSalvaAdapter.ViewHolder> {
    private static final String TAG = "VAGALUME";

    private List<Musica> listaMusicaSalva;
    private MusicaSalvaListener musicaSalvaListener;

    public MusicaSalvaAdapter(MusicaSalvaListener musicaSalvaListener) {
        listaMusicaSalva = new ArrayList<>();
        this.musicaSalvaListener = musicaSalvaListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celula_circle_image_vertical,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Musica musicaSalva = listaMusicaSalva.get(i);
        viewHolder.setupMusicaSalva(musicaSalva);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicaSalvaListener.onMusicaSalvaClicado(musicaSalva);
            }
        });
    }

    @Override
    public int getItemCount() {
        int num = 0;

        if(listaMusicaSalva.size()>8){
            num = 8;
        }else{
            num = listaMusicaSalva.size();
        }

        return num;
    }

    public void atualizarListaDeMusicas(List<Musica> listaDeMusicas) {
//        Log.i("VAGALUME", " - Lista de musicas da home ANTES com "+listaMusicaSalva.size()+" itens");
        removerTudo();
        listaMusicaSalva.addAll(listaDeMusicas);
//        Log.i("VAGALUME", " - Atualizando lista de músicas do recycler na home");
//        Log.i("VAGALUME", " - Lista de musicas da home DEPOIS com "+listaMusicaSalva.size()+" itens");
        notifyDataSetChanged();
    }

    public void removerTudo(){
        while(listaMusicaSalva.size()>0){
            listaMusicaSalva.remove(0);
        }
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagemMusicaSalvaImageView;
        private TextView nomeMusicaSalvaTextView;
        private TextView campoBottomText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeMusicaSalvaTextView = itemView.findViewById(R.id.celula_circle_campo_topo);
            imagemMusicaSalvaImageView = itemView.findViewById(R.id.celula_circle_image_view);
            campoBottomText = itemView.findViewById(R.id.celula_circle_campo_bottom);

        }
        public void setupMusicaSalva(Musica musicaSalva){
            nomeMusicaSalvaTextView.setText(musicaSalva.getName());
            campoBottomText.setText(musicaSalva.getArtista().getName());
            Picasso.get()
                    .load(musicaSalva.getArtista().getUrl())
                    .placeholder(R.drawable.placeholder_logo)
                    .into(imagemMusicaSalvaImageView);
        }
    }
}
