package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import controladores.ClaseParaBBDD;
import modelos.Conciertos;

public class Activity_Conciertos extends AppCompatActivity {

    private ListView lv_conciertos;
    private ClaseParaBBDD miClase;
    private static SQLiteDatabase db;
    ArrayList<Conciertos> listaConciertos;
    private AdaptadorPersonalizado_Conciertos adaptador;
    private Conciertos conciertos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conciertos);

        lv_conciertos = findViewById(R.id.lv_conciertos);
        miClase = new ClaseParaBBDD(this, "bbdd_aplicacion.db", null, 1);
        db = miClase.getWritableDatabase();

        listaConciertos = (ArrayList<Conciertos>) miClase.lista();

        setupListView(lv_conciertos, listaConciertos);

        lv_conciertos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conciertos concierto = listaConciertos.get(position);

                Bundle bundle = new Bundle();
                bundle.putInt("imagen", concierto.getImagen());

                bundle.putString("nombreConcierto", concierto.getNombreConciertos());
                bundle.putString("fecha", concierto.getFecha());
                bundle.putString("lugar", concierto.getLugar());
                bundle.putString("ciudad", concierto.getCiudad());
                bundle.putString("comprarEntrada", concierto.getCompraEntrada());


                Intent intent = new Intent(Activity_Conciertos.this, ActivityEntradas.class);
                intent.putExtras(bundle);


                startActivity(intent);
            }
        });

    }


    public void setupListView(ListView listView, ArrayList<Conciertos> dataList) {
        adaptador = new AdaptadorPersonalizado_Conciertos(
                this, R.layout.layout_personalizado_conciertos, dataList
        );
        listView.setAdapter(adaptador);


    }
}