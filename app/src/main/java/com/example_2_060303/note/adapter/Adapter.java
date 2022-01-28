package com.example_2_060303.note.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example_2_060303.note.R;
import com.example_2_060303.note.model.Tarefa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    private List<Tarefa> listaTarefa;
    private List<Tarefa> listaTarefaAll;
    private final String textoStatus = "Status: ";
    private final String textoDescricao = "Descricao: ";
    private Context context;

    public Adapter(List<Tarefa> lista, Context context) {
        this.listaTarefa = lista;
        this.context = context;

        this.listaTarefaAll = new ArrayList<>(listaTarefa);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_adapter ,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tarefa tarefa = listaTarefa.get( position );
        holder.titulo.setText( tarefa.getTitulo() );
        holder.descricao.setText(tarefa.getDescricao() );

        if( tarefa.getStatus().equals(100L) ){ // tarefa sem status
            holder.status.setText( "Sem Status" );
        }else {
            if(tarefa.getStatus().equals(0L)){
                holder.status.setTextColor( Color.parseColor("#E43232") );
                holder.status.setText(textoStatus + "Não concluído");
            }else if( tarefa.getStatus().equals(1L) ){
                holder.status.setTextColor( Color.parseColor("#55AB48") );
                holder.status.setText(textoStatus + "Concluído");
            }
        }

        holder.data.setText( tarefa.getData() );
        holder.hra.setText( tarefa.getHorario() );

        if( !tarefa.getFavorito().equals(0L) ){
            holder.imgFav.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.listaTarefa.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Tarefa> listaFiltrada = new ArrayList<>();
            if( charSequence.toString().isEmpty() ){
                listaFiltrada.addAll(listaTarefaAll);
            }else {
                for(Tarefa tarefa: listaTarefaAll){
                    if( tarefa.getTitulo().toLowerCase().contains(charSequence.toString().toLowerCase()) ){
                        listaFiltrada.add(tarefa);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = listaFiltrada;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            listaTarefa.clear();
            listaTarefa.addAll((Collection<? extends Tarefa>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView titulo,descricao,status,data,hra;
        private TextView imgFav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.txtTituloAdapter);
            descricao = itemView.findViewById(R.id.txtDescricaoAdapter);
            status = itemView.findViewById(R.id.txtStatusAdapter);
            data = itemView.findViewById(R.id.txtData);
            hra = itemView.findViewById(R.id.txtHra);

            imgFav = itemView.findViewById(R.id.imgFav);
        }
    }
}
