package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import controladores.ClaseParaBBDD;
import modelos.Conciertos;

public class ActivityEntradas extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_artista, tv_fecha, tv_lugar;
    private ImageView iv_imagen_concierto;
    private Button btn_entradas;
    private ClaseParaBBDD miClase;
    private static SQLiteDatabase db;
    ArrayList<Conciertos> listaConciertos;

    private Conciertos conciertos;
    private String comprarEntrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas);
        miClase = new ClaseParaBBDD(this, "bbdd_aplicacion.db", null, 1);
        db = miClase.getWritableDatabase();

        metodosFind();
        metodosSetOn();
        recibirDatosBundle();



    }

    @SuppressLint("SetTextI18n")
    private void recibirDatosBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int imagen = bundle.getInt("imagen");
            String nombreConcierto = bundle.getString("nombreConcierto");
            String fecha = bundle.getString("fecha");
            String lugar = bundle.getString("lugar");
            String ciudad = bundle.getString("ciudad");
            comprarEntrada = bundle.getString("comprarEntrada");


            tv_artista.setText("Artista: "+nombreConcierto);
            tv_fecha.setText("Fecha: "+fecha);
            tv_lugar.setText("Lugar: "+lugar+ ", "+ciudad);


        }
    }

    private void metodosSetOn() {
        btn_entradas.setOnClickListener(this);
    }

    private void metodosFind() {
        btn_entradas = findViewById(R.id.btn_entradas);
        tv_artista = findViewById(R.id.tv_artista);
        tv_fecha = findViewById(R.id.tv_fecha);
        tv_lugar = findViewById(R.id.tv_lugar);
        iv_imagen_concierto = findViewById(R.id.iv_imagen_concierto);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_entradas:
                String url = comprarEntrada;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);



                break;

        }

    }
}