package com.example_2_060303.note;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Dao implements IDao {

    private SQLiteDatabase escreve,le;

    public Dao(Context context) {
            // toda vez ao salvar, deletar, atualizar ou listar
            DbHelper dbHelper = new DbHelper( context );
            escreve = dbHelper.getWritableDatabase();
            le = dbHelper.getReadableDatabase();
        }

    @Override
    public boolean salvar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "status" , tarefa.getStatus() );
        cv.put( "titulo" , tarefa.getTitulo() );
        cv.put( "descricao" , tarefa.getDescricao() );
        cv.put( "conteudo" , tarefa.getConteudo() );

        try{
            escreve.insert( DbHelper.TABELA_TAREFA , null, cv );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {

        String[] args = { tarefa.getId().toString() };

        try {
            escreve.delete( DbHelper.TABELA_TAREFA,"id=?",args);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "status" , tarefa.getStatus() );
        cv.put( "titulo" , tarefa.getTitulo() );
        cv.put( "descricao" , tarefa.getDescricao() );
        cv.put( "conteudo" , tarefa.getConteudo() );

        try{
            String[] args = { tarefa.getId().toString() };
            escreve.update( DbHelper.TABELA_TAREFA, cv, "id=?" , args );

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizarStatus(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "status" , tarefa.getStatus() );

        try{
            String[] args = { tarefa.getId().toString() };
            escreve.update( DbHelper.TABELA_TAREFA, cv, "id=?" , args );

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @SuppressLint("Range")
    @Override
    public List<Tarefa> listar() {

        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " ORDER BY status;";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") Long status = cursor.getLong( cursor.getColumnIndex( "status" ) );
            @SuppressLint("Range") String titulo = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String descricao = cursor.getString( cursor.getColumnIndex( "descricao" ) );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );

            tarefa.setId( id );
            tarefa.setStatus( status );
            tarefa.setTitulo( titulo );
            tarefa.setDescricao( descricao );
            tarefa.setConteudo( conteudo );
            tarefaList.add( tarefa );
        }

        return tarefaList;
    }

    @Override
    public boolean verificarTarefa(String titulo, String conteudo){

        int resposta = 1;

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA +
                " WHERE titulo = '" + titulo + "' AND conteudo = '" + conteudo + "';";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            @SuppressLint("Range") String titulo2 = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String conteudo2 = cursor.getString( cursor.getColumnIndex( "conteudo" ) );

            if( titulo2.isEmpty() || conteudo2.isEmpty() ){
                resposta = resposta - 1;
            }
        }

        if( resposta == 1 ){
            return true;
        }

        return false;
    }
}
