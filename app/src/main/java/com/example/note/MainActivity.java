package com.example.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.note.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Tarefa> tarefaList = new ArrayList<>();
    private RecyclerView recycler;
    private Tarefa tarefaSelected;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recycler = findViewById(R.id.recyclerView);

        setSupportActionBar(binding.toolbar);

        recycler.addOnItemTouchListener(new RecycleViewItemClickListener(getApplicationContext(), recycler, new RecycleViewItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) { // para editar

                Tarefa tarefaSelecionada = tarefaList.get( position );
                Intent intent = new Intent( MainActivity.this, AddTarefa.class );
                intent.putExtra( "tarefaSelecionada" , tarefaSelecionada );
                startActivity( intent );
            }

            @Override
            public void onLongItemClick(View view, int position) { // para deletar

                tarefaSelected = tarefaList.get( position );

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle( "Confirmar exclusão\n" );
                dialog.setMessage( "Deseja excluir esta anotação: " + tarefaSelected.getTitulo() + "?" );

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Dao dao = new Dao( getApplicationContext() );
                        if( dao.deletar( tarefaSelected ) ){

                            carregarLista();

                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Erro ao excluir tarefa",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.setNegativeButton("Não", null);

                dialog.create().show();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getApplicationContext(), AddTarefa.class ) );
            }
        });
    }

    @Override
    protected void onStart() {
        carregarLista();
        super.onStart();
    }

    public void carregarLista(){

        // listar as tarefas salvas
        Dao dao = new Dao(getApplicationContext());
        tarefaList = dao.listar();

        Adapter adapter = new Adapter( tarefaList );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recycler.setLayoutManager( layoutManager );
        recycler.setHasFixedSize(true);
        recycler.addItemDecoration( new DividerItemDecoration( getApplicationContext(), LinearLayoutManager.VERTICAL ));
        recycler.setAdapter( adapter );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();*/
        return true;
    }
}