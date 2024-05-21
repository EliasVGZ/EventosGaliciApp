package com.example.proyecto_eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import auxiliares.Auxiliar_Activity;
import controladores.ClaseParaBBDD;
import disenho.AdaptadorPersonalizado_Conciertos;
import modelos.Conciertos;
import utils.RealTimeManager;

public class Activity_Conciertos extends Auxiliar_Activity {

    private RecyclerView rv_conciertos;
    private EditText et_buscador;
    private ImageView opc_buscador;
    private ClaseParaBBDD miClase;
    private static SQLiteDatabase db;
    ArrayList<Conciertos> listaConciertos;
    private AdaptadorPersonalizado_Conciertos adaptador;
    private RealTimeManager realTimeManager;
    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conciertos);
        metodosFind();


        //se muestren los conciertos en dos columnas
        rv_conciertos.setLayoutManager(new GridLayoutManager(this, 2));
        listaConciertos = new ArrayList<>();

        FirebaseApp.initializeApp(this);
        mfirestore = FirebaseFirestore.getInstance();




        //todo miClase = new ClaseParaBBDD(this, "bbdd_aplicacion.db", null, 1);
        //todo db = miClase.getWritableDatabase();
        //todo listaConciertos = (ArrayList<Conciertos>) miClase.lista();
        //todo setupRecyclerView(rv_conciertos, listaConciertos);

        setupRecyclerView(rv_conciertos, new ArrayList<>(listaConciertos));

        /*realTimeManager = new RealTimeManager();
        realTimeManager.getConciertos().observe(this, new Observer<List<Conciertos>>() {
            @Override
            public void onChanged(List<Conciertos> conciertos) {
                // Solo actualizar si los datos iniciales ya se han cargado
                    setupRecyclerView(rv_conciertos, new ArrayList<>(conciertos));

            }
        });*/
        et_buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // puede estar vacio
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // se filtra  la lista de conciertos
                buscarConciertos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No  hacer nada aquí
            }
        });

        //todo buscadro de conciertos
        opc_buscador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_buscador.setVisibility(View.VISIBLE);
            }
        });

        //cargarDatos();



    }
    @Override
    public void onResume() {
        super.onResume();

        cargarDatos();
    }

    private void cargarDatos() {
        mfirestore.collection("conciertos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listaConciertos.clear();
                    //resultado es un QuerySnapshot que contiene los documentos de la colección de Firestore.
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Conciertos concierto = document.toObject(Conciertos.class);
                        listaConciertos.add(concierto);
                    }
                    // actuualiza la interfaz en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (adaptador == null) {
                                adaptador = new AdaptadorPersonalizado_Conciertos(Activity_Conciertos.this, listaConciertos);
                                rv_conciertos.setAdapter(adaptador);
                            } else {
                                adaptador.actualizarDatos(listaConciertos);
                            }
                        }
                    });
                } else {
                    Log.d("Firestore", "Error al consultar documento: ", task.getException());
                }
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
            bundle.putString("imagen", concierto.getImagenUrl());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    //boton buscar
    private void buscarConciertos(String texto) {
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

    private void metodosFind() {
        et_buscador = findViewById(R.id.et_buscador);
        rv_conciertos = findViewById(R.id.rv_conciertos);
        opc_buscador = findViewById(R.id.opc_buscador);
    }

    //ocultar el editext cuando se hace click en cualquier parte de la pantalla
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (et_buscador.isShown()) {
                Rect outRect = new Rect();//se crea un rectangulo
                et_buscador.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    et_buscador.setVisibility(View.GONE);
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}