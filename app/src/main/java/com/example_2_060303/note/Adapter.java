package com.example_2_060303.note;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Tarefa> listaTarefa;
    private final String textoStatus = "Status: ";
    private final String textoDescricao = "Descricao: ";

    public Adapter(List<Tarefa> lista) {
        this.listaTarefa = lista;
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
        holder.descricao.setText(textoDescricao + tarefa.getDescricao() );

        if(tarefa.getStatus()!=1){
            holder.status.setTextColor( Color.parseColor("#E43232") );
            holder.status.setText(textoStatus + "Não concluído");
        }else{
            holder.status.setTextColor( Color.parseColor("#55AB48") );
            holder.status.setText(textoStatus + "Concluído");
        }
    }

    @Override
    public int getItemCount() {
        return this.listaTarefa.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView titulo,descricao,status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.txtTituloAdapter);
            descricao = itemView.findViewById(R.id.txtDescricaoAdapter);
            status = itemView.findViewById(R.id.txtStatusAdapter);
        }
    }
}
