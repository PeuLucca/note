package com.example_2_060303.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "db_note";
    public static String TABELA_TAREFA = "tarefa";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
        // context.deleteDatabase(NOME_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // apagar tabela se já existir

        String sqlTarefa = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFA +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "status INTEGER," +
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

    }
}
