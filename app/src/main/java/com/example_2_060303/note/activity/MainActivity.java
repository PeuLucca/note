package com.example_2_060303.note.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

//import com.example.note.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example_2_060303.note.R;
import com.example_2_060303.note.adapter.Adapter;
import com.example_2_060303.note.adapter.RecycleViewItemClickListener;
import com.example_2_060303.note.databinding.ActivityMainBinding;
import com.example_2_060303.note.helper.Dao;
import com.example_2_060303.note.helper.DbHelper;
import com.example_2_060303.note.model.Alarm;
import com.example_2_060303.note.model.Tarefa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity{

    private List<Tarefa> tarefaList = new ArrayList<>();
    private RecyclerView recycler;
    private Tarefa tarefaSelected;
    private int itemSelected;
    private FloatingActionButton fab;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rateUs(); // para avaliação na play store:

        //DbHelper dbHelper = new DbHelper(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fab = findViewById(R.id.fab);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int time = 604800; // 15 dias = 1296000 e 7 dias = 604800 e 9 dias = 777.600
        Intent i = new Intent(MainActivity.this, Alarm.class);

        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+time*1000,pi);
    }

    public void rateUs(){
        AppRate.with(this)
                .setInstallDays(5) // quantos dias depois que instalou o app para aparecer este dialogo
                .setLaunchTimes(7) // quantas vezes abrir o app para aparecer este dialogo
                .setRemindInterval(1) // quantos dias para aparecer de novo depois do usuario
                // clicar em "Talves mais tarde"
                .setTitle("Está curtindo o app?")
                .setMessage("Ajude-nos e avalie enviando seu feedback para Play Store! ⭐⭐⭐⭐⭐")
                .setTextNever("Nunca")
                .setTextRateNow("Avaliar agora")
                .setTextLater("Talves mais tarde")
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @SuppressLint("ResourceAsColor")
    public void carregarLista(){

        // listar as tarefas salvas
        Dao dao = new Dao(getApplicationContext());
        tarefaList = dao.listar( "status" ); // carregando lista baseado no status (status,titulo,horario)

        Adapter adapter = new Adapter( tarefaList,getApplicationContext() );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recycler.setLayoutManager( layoutManager );
        recycler.setHasFixedSize(true);
        recycler.setAdapter( adapter );

        if(tarefaList.size() > 5){
            fab.setAlpha(0.60f);
        }else {
            fab.setAlpha(1f);
        }
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


            case R.id.pesquisar_por_nome:
                List<Tarefa> tarefa = tarefaList;
                Intent intent = new Intent( MainActivity.this, PesquisarTarefa.class );
                intent.putExtra( "tarefa" , (Serializable) tarefa);
                startActivity( intent );

                break;

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