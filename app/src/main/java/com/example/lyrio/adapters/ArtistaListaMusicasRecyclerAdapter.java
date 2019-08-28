package com.example.lyrio.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.R;
import com.example.lyrio.interfaces.ListaMusicasSalvasListener;

import java.util.List;

public class ArtistaListaMusicasRecyclerAdapter extends RecyclerView.Adapter<ArtistaListaMusicasRecyclerAdapter.ViewHolder> {

    private List<Musica> listaMusicaSalva;
    private ListaMusicasSalvasListener listaMusicasSalvasListener;
    private ApiArtista apiArtista;

    public ArtistaListaMusicasRecyclerAdapter(List<Musica> listaMusicaSalva, ListaMusicasSalvasListener listaMusicasSalvasListener, ApiArtista apiArtista) {
        this.listaMusicaSalva = listaMusicaSalva;
        this.listaMusicasSalvasListener = listaMusicasSalvasListener;
        this.apiArtista = apiArtista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celula_lista_musica_salva,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Musica musicaSalva = listaMusicaSalva.get(i);
        viewHolder.setupListaMusicaSalva(musicaSalva, i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaMusicasSalvasListener.onListaMusicasSalvasClicado(musicaSalva);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaMusicaSalva.size();
    }

    public void adicionarMusica(Musica musicaSalva){
        listaMusicaSalva.add(musicaSalva);
        notifyDataSetChanged();
    }

    public void atualizarLista(List<Musica> listaDeMusicas, ApiArtista apiArt){
        if(listaMusicaSalva.size()>0){
            while(listaMusicaSalva.size()>0){
                listaMusicaSalva.remove(0);
            }
        }

        listaMusicaSalva.addAll(listaDeMusicas);
        apiArtista = apiArt;

        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
//        private ImageView imagemMusicaSalvaImageView;
        private TextView nomeMusicaSalvaTextView;
        private TextView numeroMusicaSalvaTextView;
        private TextView buscaCampoBottom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imagemMusicaSalvaImageView = itemView.findViewById(R.id.busca_img_artista);
            nomeMusicaSalvaTextView=itemView.findViewById(R.id.busca_campo_top);
            numeroMusicaSalvaTextView=itemView.findViewById(R.id.busca_campo_num);
//            buscaCampoBottom=itemView.findViewById(R.id.busca_campo_bottom);

        }
        public void setupListaMusicaSalva(Musica musicaSalva, int num){
//            imagemMusicaSalvaImageView.setVisibility(View.GONE);
//            String imgUrl = "https://www.vagalume.com"+apiArtista.getUrl()+"images/profile.jpg";
//            buscaCampoBottom.setText(apiArtista.getDesc());
            int fixedNum = num+1;
            numeroMusicaSalvaTextView.setText(fixedNum+".");
            nomeMusicaSalvaTextView.setText(musicaSalva.getDesc());
//            Picasso.get().load(imgUrl).into(imagemMusicaSalvaImageView);
        }
    }
}
