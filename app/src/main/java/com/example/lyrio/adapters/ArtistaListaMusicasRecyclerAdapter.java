package com.example.lyrio.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.lyrio.interfaces.Filterable;
import com.example.lyrio.modules.Artista.model.ApiArtista;
import com.example.lyrio.modules.musica.model.Musica;
import com.example.lyrio.R;
import com.example.lyrio.interfaces.ListaMusicasSalvasListener;

import java.util.ArrayList;
import java.util.List;

public class ArtistaListaMusicasRecyclerAdapter extends RecyclerView.Adapter<ArtistaListaMusicasRecyclerAdapter.ViewHolder> implements Filterable {

    private List<Musica> listaMusicaDisplay;
    private ListaMusicasSalvasListener listaMusicasSalvasListener;
    private ApiArtista apiArtista;
    private List<Musica> filteredList;

    public ArtistaListaMusicasRecyclerAdapter(List<Musica> listaDeMusicas, ListaMusicasSalvasListener listaMusicasSalvasListener, ApiArtista apiArtista) {
        this.listaMusicaDisplay = listaDeMusicas;
        this.listaMusicasSalvasListener = listaMusicasSalvasListener;
        this.apiArtista = apiArtista;
        this.filteredList = new ArrayList<>(listaMusicaDisplay);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celula_lista_musica_salva,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Musica musicaSalva = filteredList.get(i);
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
        return filteredList.size();
    }

//    public void adicionarMusica(Musica musicaSalva){
//        listaMusicaDisplay.add(musicaSalva);
//        notifyDataSetChanged();
//    }

    public void restoreList(){
        if(filteredList.size()>0){
            while(filteredList.size()>0){
                filteredList.remove(0);
            }
        }
        filteredList.addAll(listaMusicaDisplay);
        notifyDataSetChanged();
    }

    public void atualizarLista(List<Musica> listaDeMusicas, ApiArtista apiArt, boolean notify){
        if(listaMusicaDisplay.size()>0){
            while(listaMusicaDisplay.size()>0){
                listaMusicaDisplay.remove(0);
            }
        }

        if(filteredList.size()>0){
            while(filteredList.size()>0){
                filteredList.remove(0);
            }
        }

        listaMusicaDisplay.addAll(listaDeMusicas);
        filteredList.addAll(listaMusicaDisplay);
        apiArtista = apiArt;

        if(notify){
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return musicasFilter;
    }

    private Filter musicasFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Musica> toFilter = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                toFilter.addAll(listaMusicaDisplay);

            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Musica musica : listaMusicaDisplay) {
                    if (musica.getDesc().toLowerCase().contains(filterPattern)) {
                        toFilter.add(musica);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = toFilter;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((List) results.values);
            listaMusicasSalvasListener.updateItemCount();
            notifyDataSetChanged();
        }
    };


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
