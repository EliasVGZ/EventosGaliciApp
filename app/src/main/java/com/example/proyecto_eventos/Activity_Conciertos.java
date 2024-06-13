package com.example.proyecto_eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import adapters.AdaptadorPersonalizado_Conciertos;
import controladores.FirebaseController;
import models.Conciertos;
import utils.RealTimeManager;

public class Activity_Conciertos extends AppCompatActivity  {

    private RecyclerView rv_conciertos;
    private EditText et_buscador, et_filtro_ciudad;
    private ImageView opc_buscador, opc_filtro, opc_home, opc_preguntas;
    private LinearLayout ll_filtro_ciudad;
    private static SQLiteDatabase db;
    ArrayList<Conciertos> listaConciertos;
    private AdaptadorPersonalizado_Conciertos adaptador;
    private RealTimeManager realTimeManager;

    private ProgressBar loader;
    private FirebaseController firebaseController;
    private CheckBox cb_acoruna, cb_lugo, cb_ourense, cb_pontevedra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conciertos);
        metodosFind();


        //se muestren los conciertos en dos columnas
        rv_conciertos.setLayoutManager(new GridLayoutManager(this, 2));
        listaConciertos = new ArrayList<>();

        FirebaseApp.initializeApp(this);// se inicializa Firebase
        firebaseController = new FirebaseController();//se creo una instancia de FirebaseController


        setupRecyclerView(rv_conciertos, new ArrayList<>(listaConciertos));

        buscadorConciertos();
        filtradorProvincia();
        listenerBuscador();
        listenerFiltroCiudad();
        volverMenu();
    }

    private void volverMenu() {
        opc_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Conciertos.this, Activity_Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//para que no se pueda volver atras
                startActivity(intent);
            }
        });
    }


    //se ejecuta cuando la actividad se vuelve visible, si los metiera en oncreate solo se cargarian una vez
    @Override
    public void onResume() {
        super.onResume();

        cargarDatos();
    }

    private void cargarDatos() {
        loader.setVisibility(View.VISIBLE);

        firebaseController.cargarDatosConciertos(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listaConciertos.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Conciertos concierto = document.toObject(Conciertos.class);
                        listaConciertos.add(concierto);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (adaptador == null) {
                                adaptador = new AdaptadorPersonalizado_Conciertos(Activity_Conciertos.this, listaConciertos);
                                rv_conciertos.setAdapter(adaptador);
                            } else {
                                adaptador.actualizarDatos(listaConciertos);
                            }
                            loader.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Log.d("Firestore", "Error al consultar documento: ", task.getException());
                    loader.setVisibility(View.GONE);
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
            bundle.putString("nombreConcierto", concierto.getNombre());
            bundle.putString("fecha", concierto.getFecha());
            bundle.putString("lugar", concierto.getLugar());
            bundle.putString("ciudad", concierto.getCiudad());
            bundle.putString("comprarEntrada", concierto.getCompraEntrada());
            bundle.putString("imagen", concierto.getImagenUrl());
            bundle.putString("genero", concierto.getGenero());
            bundle.putString("precio", concierto.getPrecio());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    //boton buscar
    private void buscarConciertos(String texto) {
        ArrayList<Conciertos> listaFiltrada = new ArrayList<>();

        for (Conciertos concierto : listaConciertos) {
            if (concierto.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(concierto);
            }
        }

        //se actualiza la lista con lo buscado
        setupRecyclerView(rv_conciertos, listaFiltrada);
    }

    private void metodosFind() {
        et_buscador = findViewById(R.id.et_buscador);
        rv_conciertos = findViewById(R.id.rv_conciertos);
        opc_buscador = findViewById(R.id.opc_buscador);
        opc_filtro = findViewById(R.id.opc_filtro);
        cb_acoruna = findViewById(R.id.cb_acoruna);
        cb_lugo = findViewById(R.id.cb_lugo);
        cb_ourense = findViewById(R.id.cb_ourense);
        cb_pontevedra = findViewById(R.id.cb_pontevedra);
        ll_filtro_ciudad = findViewById(R.id.ll_filtro_ciudad);
        opc_home = findViewById(R.id.opc_home);
        loader = findViewById(R.id.loader);
    }

    //ocultar el editext cuando se hace click en cualquier parte de la pantalla
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect outRectBuscador = new Rect();
            Rect outRectFiltro = new Rect();
            Rect outRectConciertos = new Rect();
            et_buscador.getGlobalVisibleRect(outRectBuscador);
            ll_filtro_ciudad.getGlobalVisibleRect(outRectFiltro);
            rv_conciertos.getGlobalVisibleRect(outRectConciertos);
            if (et_buscador.isShown() && !outRectBuscador.contains((int)event.getRawX(), (int)event.getRawY()) && !outRectConciertos.contains((int)event.getRawX(), (int)event.getRawY())) {
                et_buscador.setText("");
                et_buscador.setVisibility(View.GONE);
                return true;
            }
            if (ll_filtro_ciudad.isShown() && !outRectFiltro.contains((int)event.getRawX(), (int)event.getRawY()) && !outRectConciertos.contains((int)event.getRawX(), (int)event.getRawY())) {
                ll_filtro_ciudad.setVisibility(View.GONE);
                return true;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void listenerFiltroCiudad() {
        opc_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_buscador.setVisibility(View.GONE);
                ll_filtro_ciudad.setVisibility(View.VISIBLE);
            }
        });
    }

    private void listenerBuscador() {
        opc_buscador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_filtro_ciudad.setVisibility(View.GONE);
                et_buscador.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filtradorProvincia() {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(cb_acoruna);
        checkBoxes.add(cb_lugo);
        checkBoxes.add(cb_ourense);
        checkBoxes.add(cb_pontevedra);

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (CheckBox otherCheckBox : checkBoxes) {
                            if (otherCheckBox != checkBox) {
                                otherCheckBox.setChecked(false);
                            }
                        }
                        firebaseController.cargarConciertosPorCiudad(checkBox, new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    listaConciertos.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Conciertos concierto = document.toObject(Conciertos.class);
                                        listaConciertos.add(concierto);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (adaptador == null) {
                                                adaptador = new AdaptadorPersonalizado_Conciertos(Activity_Conciertos.this, listaConciertos);
                                                rv_conciertos.setAdapter(adaptador);
                                            } else {
                                                adaptador.actualizarDatos(listaConciertos);
                                            }
                                            loader.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    Log.d("Firestore", "Error al consultar documento: ", task.getException());
                                    loader.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void buscadorConciertos() {
        //Buscar por concierto
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
    }


}