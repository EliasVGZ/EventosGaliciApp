package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import auxiliares.Auxiliar_Activity;
import controladores.ClaseParaBBDD;
import disenho.AdaptadorPersonalizado_Conciertos;
import modelos.Conciertos;

public class Activity_Conciertos extends Auxiliar_Activity {

    private RecyclerView rv_conciertos;
    private EditText et_buscador;
    private ClaseParaBBDD miClase;
    private static SQLiteDatabase db;
    ArrayList<Conciertos> listaConciertos;
    private AdaptadorPersonalizado_Conciertos adaptador;
    private Conciertos conciertos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conciertos);
        et_buscador = findViewById(R.id.et_buscador);
        rv_conciertos = findViewById(R.id.rv_conciertos);
        //se muestren los conciertos en dos columnas
        rv_conciertos.setLayoutManager(new GridLayoutManager(this, 2));

        miClase = new ClaseParaBBDD(this, "bbdd_aplicacion.db", null, 1);
        db = miClase.getWritableDatabase();
        listaConciertos = (ArrayList<Conciertos>) miClase.lista();
        setupRecyclerView(rv_conciertos, listaConciertos);
        et_buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // puede estar vacio
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // se filtra  la lista de conciertos
                filtrarConciertos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No  hacer nada aquí
            }
        });



    }


    public void setupRecyclerView(RecyclerView recyclerView, ArrayList<Conciertos> listaConciertos) {
        adaptador = new AdaptadorPersonalizado_Conciertos(this, listaConciertos);
        recyclerView.setAdapter(adaptador);
        //lambda, se le pasa un concierto y se ejecuta el metodo onItemClick
        adaptador.setOnItemClickListener(concierto -> {

            Intent intent = new Intent(Activity_Conciertos.this, ActivityEntradas.class);
            Bundle bundle = new Bundle();
            bundle.putString("nombreConcierto", concierto.getNombreConciertos());
            bundle.putString("fecha", concierto.getFecha());
            bundle.putString("lugar", concierto.getLugar());
            bundle.putString("ciudad", concierto.getCiudad());
            bundle.putString("comprarEntrada", concierto.getCompraEntrada());
            bundle.putInt("imagen", concierto.getImagen());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
    //boton buscar
    private void filtrarConciertos(String texto) {
        ArrayList<Conciertos> listaFiltrada = new ArrayList<>();

        for (Conciertos concierto : listaConciertos) {
            if (concierto.getNombreConciertos().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(concierto);
            }
        }

        //se actualiza la lista con lo buscado
        adaptador = new AdaptadorPersonalizado_Conciertos(
                this, listaFiltrada
        );
        rv_conciertos.setAdapter(adaptador);
    }

    public EditText getBuscadorEditText() {
        return et_buscador;
    }
}