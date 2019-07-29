package com.example.lyrio.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lyrio.api.base_vagalume.ApiArtista;
import com.example.lyrio.R;
import com.example.lyrio.interfaces.ArtistaSalvoListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistaSalvoAdapter extends RecyclerView.Adapter<ArtistaSalvoAdapter.ViewHolder> {
    private List<ApiArtista> listaArtistaSalvo;
    private ArtistaSalvoListener artistaSalvoListener;

    public ArtistaSalvoAdapter(ArtistaSalvoListener artistaSalvoListener) {
        listaArtistaSalvo = new ArrayList<>();
        this.artistaSalvoListener = artistaSalvoListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celula_circle_image_vertical,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ApiArtista artistaSalvo = listaArtistaSalvo.get(i);
        viewHolder.setupArtistaSalvo(artistaSalvo);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artistaSalvoListener.onArtistaClicado(artistaSalvo);
            }
        });
    }

    @Override
    public int getItemCount() {
        int num = 0;

        if(listaArtistaSalvo.size()>8){
            num = 8;
        }else{
            num = listaArtistaSalvo.size();
        }

        return listaArtistaSalvo.size();
    }

    public void adicionarArtista(ApiArtista artistaSalvo){
        listaArtistaSalvo.add(artistaSalvo);
        notifyDataSetChanged();
    }

    public void adicionarListaDeArtistas(List<ApiArtista> listaDeArtistas) {
        listaArtistaSalvo.addAll(listaDeArtistas);
        notifyDataSetChanged();
    }

    public void removerTudo(){
        while(listaArtistaSalvo.size()>0){
            listaArtistaSalvo.remove(0);
        }
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imagemArtistaSalvoCircleImageView;
        private TextView nomeArtistaSalvoTextView;
        private TextView bottomText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemArtistaSalvoCircleImageView = itemView.findViewById(R.id.celula_circle_image_view);
            nomeArtistaSalvoTextView = itemView.findViewById(R.id.celula_circle_campo_topo);
            bottomText = itemView.findViewById(R.id.celula_circle_campo_bottom);
        }
        public void setupArtistaSalvo(ApiArtista artistaSalvo){
            nomeArtistaSalvoTextView.setText(artistaSalvo.getDesc());
            bottomText.setText(artistaSalvo.getQtdMusicas()+" músicas");
            Picasso.get().load(artistaSalvo.getPic_small()).into(imagemArtistaSalvoCircleImageView);
        }
    }
}
