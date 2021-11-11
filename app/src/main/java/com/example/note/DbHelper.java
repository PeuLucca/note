package com.example.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "db_note";
    public static String TABELA_TAREFA = "tarefa";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlTarefa = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFA +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "titulo TEXT NOT NULL, " +
                "descricao TEXT, " +
                "conteudo TEXT NOT NULL);";

        try {

            db.execSQL( sqlTarefa );

        }catch (Exception e){

            e.printStackTrace();

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sqlDrop = "DROP TABLE IF EXISTS " + TABELA_TAREFA + ";";

        try{

            db.execSQL(sqlDrop);

        }catch (Exception e){

            e.printStackTrace();

        }

    }
}
