package com.example_2_060303.note.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//import com.example.note.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example_2_060303.note.R;
import com.example_2_060303.note.adapter.Adapter;
import com.example_2_060303.note.adapter.RecycleViewItemClickListener;
import com.example_2_060303.note.databinding.ActivityMainBinding;
import com.example_2_060303.note.helper.Dao;
import com.example_2_060303.note.helper.DbHelper;
import com.example_2_060303.note.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Tarefa> tarefaList = new ArrayList<>();
    private RecyclerView recycler;
    private Tarefa tarefaSelected;
    private int itemSelected;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //DbHelper dbHelper = new DbHelper(getApplicationContext());

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

                        Dao daoDeletar = new Dao( getApplicationContext() );
                        if( daoDeletar.deletar( tarefaSelected ) ){

                            if( itemSelected == 0 ){
                                carregarLista();
                            }else if( itemSelected == 1 ){

                                Dao dao = new Dao(getApplicationContext());
                                tarefaList = dao.listarTitulo();

                                Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recycler.setLayoutManager( layoutManager );
                                recycler.setHasFixedSize(true);
                                recycler.setAdapter( adapter );

                            } else if( itemSelected == 2 ){

                                Dao dao = new Dao(getApplicationContext());
                                tarefaList = dao.listar( "data" ); // carregando lista baseado no status (status,titulo,horario)

                                Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recycler.setLayoutManager( layoutManager );
                                recycler.setHasFixedSize(true);
                                recycler.setAdapter( adapter );
                            } else if( itemSelected == 3 ){

                                Dao dao = new Dao(getApplicationContext());
                                tarefaList = dao.listarSttsConcluido();

                                Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recycler.setLayoutManager( layoutManager );
                                recycler.setHasFixedSize(true);
                                recycler.setAdapter( adapter );
                            } else if( itemSelected == 4 ){

                                Dao dao = new Dao(getApplicationContext());
                                tarefaList = dao.listarSttsNaoConcluido();

                                Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recycler.setLayoutManager( layoutManager );
                                recycler.setHasFixedSize(true);
                                recycler.setAdapter( adapter );
                            } else if( itemSelected == 5 ){

                                Dao dao = new Dao(getApplicationContext());
                                tarefaList = dao.listarSemStts();

                                Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recycler.setLayoutManager( layoutManager );
                                recycler.setHasFixedSize(true);
                                recycler.setAdapter( adapter );
                            }else if( itemSelected == 6 ){

                                Dao dao = new Dao(getApplicationContext());
                                tarefaList = dao.listarFav();

                                Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recycler.setLayoutManager( layoutManager );
                                recycler.setHasFixedSize(true);
                                recycler.setAdapter( adapter );
                            }

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
        tarefaList = dao.listar( "status" ); // carregando lista baseado no status (status,titulo,horario)

        Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recycler.setLayoutManager( layoutManager );
        recycler.setHasFixedSize(true);
        recycler.setAdapter( adapter );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.itemOrdenarPrioridade: // 0

                itemSelected = 0;

                Dao dao0 = new Dao(getApplicationContext());
                tarefaList = dao0.listar( "status" );

                Adapter adapter0 = new Adapter( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager0 = new LinearLayoutManager( getApplicationContext() );
                recycler.setLayoutManager( layoutManager0 );
                recycler.setHasFixedSize(true);
                recycler.setAdapter( adapter0 );

                break;

            case R.id.itemOrdenarA_Z: // 1

                itemSelected = 1;

                // listar as tarefas salvas
                Dao dao1 = new Dao(getApplicationContext());
                tarefaList = dao1.listarTitulo();

                Adapter adapter1 = new Adapter( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager( getApplicationContext() );
                recycler.setLayoutManager( layoutManager1 );
                recycler.setHasFixedSize(true);
                recycler.setAdapter( adapter1 );

                break;

            case R.id.itemOrdenarData: // 2

                itemSelected = 2;

                // listar as tarefas salvas
                Dao dao2 = new Dao(getApplicationContext());
                tarefaList = dao2.listar( "data" ); // carregando lista baseado no status (status,titulo,horario)

                Adapter adapter2 = new Adapter( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager( getApplicationContext() );
                recycler.setLayoutManager( layoutManager2 );
                recycler.setHasFixedSize(true);
                recycler.setAdapter( adapter2 );

                break;


            case R.id.itemOrdenarStatusConcluido: // 3

                itemSelected = 3;

                Dao dao3 = new Dao(getApplicationContext());
                tarefaList = dao3.listarSttsConcluido();

                Adapter adapter3 = new Adapter( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager( getApplicationContext() );
                recycler.setLayoutManager( layoutManager3 );
                recycler.setHasFixedSize(true);
                recycler.setAdapter( adapter3 );
                break;

            case R.id.itemOrdenarStatusNaoConcluido: // 4

                itemSelected = 4;

                Dao dao4 = new Dao(getApplicationContext());
                tarefaList = dao4.listarSttsNaoConcluido();

                Adapter adapter4 = new Adapter( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager( getApplicationContext() );
                recycler.setLayoutManager( layoutManager4 );
                recycler.setHasFixedSize(true);
                recycler.setAdapter( adapter4 );
                break;

            case R.id.itemOrdenarSemStatus: // 5

                itemSelected = 5;

                Dao dao5 = new Dao(getApplicationContext());
                tarefaList = dao5.listarSemStts();

                Adapter adapter5 = new Adapter( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager( getApplicationContext() );
                recycler.setLayoutManager( layoutManager5 );
                recycler.setHasFixedSize(true);
                recycler.setAdapter( adapter5 );
                break;

            case R.id.itemExibirFav:

                itemSelected = 6;

                Dao dao6 = new Dao(getApplicationContext());
                tarefaList = dao6.listarFav();

                Adapter adapter6 = new Adapter( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager( getApplicationContext() );
                recycler.setLayoutManager( layoutManager6 );
                recycler.setHasFixedSize(true);
                recycler.setAdapter( adapter6 );

                break;
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