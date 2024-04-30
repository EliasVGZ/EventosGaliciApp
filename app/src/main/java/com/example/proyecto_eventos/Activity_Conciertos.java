package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import auxiliares.Auxiliar_Activity;
import controladores.ClaseParaBBDD;
import disenho.AdaptadorPersonalizado_Conciertos;
import modelos.Conciertos;

public class Activity_Conciertos extends Auxiliar_Activity {

    private ListView lv_conciertos;
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
                // No necesitamos hacer nada aquí
            }
        });

    }


    public void setupListView(ListView listView, ArrayList<Conciertos> dataList) {
        adaptador = new AdaptadorPersonalizado_Conciertos(
                this, R.layout.layout_personalizado_conciertos, dataList
        );
        listView.setAdapter(adaptador);


    }

    private void filtrarConciertos(String texto) {
        ArrayList<Conciertos> listaFiltrada = new ArrayList<>();

        for (Conciertos concierto : listaConciertos) {
            if (concierto.getNombreConciertos().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(concierto);
            }
        }

        //se actualiza la lista con lo buscado
        adaptador = new AdaptadorPersonalizado_Conciertos(
                this, R.layout.layout_personalizado_conciertos, listaFiltrada
        );
        lv_conciertos.setAdapter(adaptador);
    }

    public EditText getBuscadorEditText() {
        return et_buscador;
    }
}