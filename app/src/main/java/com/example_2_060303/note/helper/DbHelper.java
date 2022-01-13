package com.example_2_060303.note.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION_DB = 2;
    public static String NOME_DB = "db_note";
    public static String TABELA_TAREFA = "tarefa";
    private static final String sqlDeletarTBLA = "DROP TABLE IF EXISTS " + TABELA_TAREFA + ";";
    private static final String sqlCriarTarefa =
            "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFA +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "status INTEGER," +
            "titulo TEXT NOT NULL, " +
            "descricao TEXT, " +
            "conteudo TEXT NOT NULL);";
    private static final String sqlAddData = "ALTER TABLE " + TABELA_TAREFA + " ADD COLUMN data TEXT;";
    private static final String sqlAddHra = "ALTER TABLE " + TABELA_TAREFA + " ADD COLUMN horario TEXT;";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION_DB);
        // context.deleteDatabase(NOME_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL( sqlDeletarTBLA );
            db.execSQL( sqlCriarTarefa );

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {

            if( oldVersion < 2 ){
                db.execSQL( sqlAddData );
                db.execSQL( sqlAddHra );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
}
