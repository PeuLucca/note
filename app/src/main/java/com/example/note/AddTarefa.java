package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.R;
import com.google.android.material.snackbar.Snackbar;

public class AddTarefa extends AppCompatActivity {

    private EditText titulo, descricao, conteudo;
    private String tituloString, descricaoString, conteudoString;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        titulo = findViewById(R.id.txtTitulo);
        descricao = findViewById(R.id.txtDescricao);
        conteudo = findViewById(R.id.txtConteudo);

        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        if( tarefaAtual!=null ){

            titulo.setText( tarefaAtual.getTitulo() );
            descricao.setText( tarefaAtual.getDescricao() );
            conteudo.setText( tarefaAtual.getConteudo() );
        }

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate( R.menu.menu_add_tarefa, menu );

        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.itemSalvar:

                tituloString = titulo.getText().toString();
                descricaoString = descricao.getText().toString();
                conteudoString = conteudo.getText().toString();

                Dao dao = new Dao(getApplicationContext());

                if( tarefaAtual == null ) { // se for para salvar e nao atualizar
                    if (tituloString.isEmpty() || conteudoString.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Insira os campos obrigatórios", Toast.LENGTH_SHORT).show();
                    } else {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setTitulo(tituloString);
                        tarefa.setDescricao(descricaoString);
                        tarefa.setConteudo(conteudoString);

                        if (dao.salvar(tarefa)) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao salvar tarefa", Toast.LENGTH_LONG).show();
                        }
                    }
                }else if( tarefaAtual!=null ){ // se for para atualizar

                    if (tituloString.isEmpty() || conteudoString.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Insira os campos obrigatórios", Toast.LENGTH_SHORT).show();
                    }else {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setTitulo(tituloString);
                        tarefa.setDescricao(descricaoString);
                        tarefa.setConteudo(conteudoString);
                        tarefa.setId(tarefaAtual.getId());

                        if (dao.atualizar(tarefa)) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Tarefa atualizada com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Erro ao atualizar tarefa", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}