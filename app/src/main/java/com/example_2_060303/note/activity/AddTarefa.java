package com.example_2_060303.note.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example_2_060303.note.R;
import com.example_2_060303.note.helper.Dao;
import com.example_2_060303.note.model.Tarefa;

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

    public void salvar(){

        tituloString = titulo.getText().toString();
        descricaoString = descricao.getText().toString();
        conteudoString = conteudo.getText().toString();

        Dao dao = new Dao(getApplicationContext());

        if( tarefaAtual == null ) { // se for para salvar e nao atualizar
            if (tituloString.isEmpty() || conteudoString.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Insira os campos obrigatórios", Toast.LENGTH_SHORT).show();
            } else {
                Tarefa tarefa = new Tarefa();
                tarefa.setTitulo(tituloString);
                tarefa.setDescricao(descricaoString);
                tarefa.setConteudo(conteudoString);
                tarefa.setStatus(0L);

                if (dao.salvar(tarefa)) {
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Erro ao salvar tarefa", Toast.LENGTH_LONG).show();
                }
            }
        }else{ // se for para atualizar

            if ( tarefaAtual.getTitulo().isEmpty() || tarefaAtual.getConteudo().isEmpty() ) {
                Toast.makeText(getApplicationContext(),
                        "Insira os campos obrigatórios", Toast.LENGTH_SHORT).show();
            }else {

                Tarefa tarefa = new Tarefa();
                tarefa.setTitulo(tituloString);
                tarefa.setDescricao(descricaoString);
                tarefa.setConteudo(conteudoString);
                tarefa.setId( tarefaAtual.getId() );

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
                salvar();
                break;

            case R.id.itemStatusPositivo:

                if( titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(),
                            "Salve uma tarefa antes de alterar seu status", Toast.LENGTH_SHORT).show();
                }else {

                    Dao d = new Dao(getApplicationContext());

                    // verificar se esta tarefa existe:
                    if ( d.verificarStatus( titulo.getText().toString()) ){

                        Tarefa tarefa = new Tarefa();
                        tarefa.setId( tarefaAtual.getId() );
                        tarefa.setStatus(1L); // tarefa concluida

                        salvar();

                        if( d.atualizarStatus(tarefa) ){
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "Erro ao alterar status da tarefa",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Você precisa salvar sua tarefa, e então mudar seu status",Toast.LENGTH_LONG).show();
                    }
                }

                break;

            case R.id.itemDelete:

                Dao dao2 = new Dao(getApplicationContext());
                if(!titulo.getText().toString().equals("") && !conteudo.getText().toString().equals("")){

                    if( dao2.verificarTarefa(titulo.getText().toString(),conteudo.getText().toString()) ) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder( AddTarefa.this );
                        dialog.setTitle( "Confirmar exclusão\n" );
                        dialog.setMessage( "Deseja excluir esta anotação ?" );

                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if( dao2.deletar( tarefaAtual ) ){

                                    finish();
                                    Toast.makeText(getApplicationContext(),
                                            "Tarefa excluída",Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(getApplicationContext(),
                                            "Erro ao excluir tarefa",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        dialog.setNegativeButton("Não", null);

                        dialog.create().show();

                    }
                    else { // se a tarefa nao existir
                        Toast.makeText(getApplicationContext(),
                                "Para deletar uma tarefa você precisa tê-la salva",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else { // se a tarefa nao existir
                    Toast.makeText(getApplicationContext(),
                            "Para deletar uma tarefa você precisa tê-la salva",
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.itemCompartilhar:

                if(titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(),
                            "É necessário ter anotações salvas para que possam ser compartilhadas",
                            Toast.LENGTH_SHORT).show();

                }else {

                    Intent sendIntent = new Intent ();
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setAction (Intent.ACTION_SEND);
                    sendIntent.putExtra (Intent.EXTRA_TEXT, "*Compartilhado através do Bloco de notas - Note*:\n\n" +
                            "*Título:* " + titulo.getText().toString() +
                            "\n\nDescrição: \n" + descricao.getText().toString() +
                            "\n\n*Conteúdo:* \n" + conteudo.getText().toString() +
                            "\n\n\nBaixe agora https://play.google.com/store/apps/details?id=com.example_2_060303.note");
                    sendIntent.setPackage ("com.whatsapp");
                    sendIntent.setType ("text / plain");
                    startActivity (sendIntent);

                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}