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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import adapters.AdaptadorPersonalizado_Eventos;
import controladores.FirebaseController;
import models.Conciertos;
import models.Evento;
import utils.RealTimeManager;

public class Activity_Eventos extends AppCompatActivity  {

    private RecyclerView rv_eventos;
    private EditText et_buscador;
    private ImageView opc_buscador, opc_filtro, opc_home;
    private LinearLayout ll_filtro_ciudad;
    ArrayList<Evento> listaEventos;
    private AdaptadorPersonalizado_Eventos adaptador;

    private ProgressBar loader;
    private FirebaseController firebaseController;
    private CheckBox cb_acoruna, cb_lugo, cb_ourense, cb_pontevedra;
    private String tipoEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        metodosFind();


        //se muestren los conciertos en dos columnas
        rv_eventos.setLayoutManager(new GridLayoutManager(this, 2));
        listaEventos = new ArrayList<>();

        FirebaseApp.initializeApp(this);// se inicializa Firebase
        firebaseController = new FirebaseController();//se creo una instancia de FirebaseController
        tipoEvento = getIntent().getStringExtra("tipoEvento");


        setupRecyclerView(rv_eventos, new ArrayList<>(listaEventos));

        buscadorEventos();
        filtradorProvincia();
        listenerBuscador();
        listenerFiltroCiudad();
        volverMenu();
        cargarDatos();
    }

    private void volverMenu() {
        opc_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Eventos.this, Activity_Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//para que no se pueda volver atras
                startActivity(intent);
            }
        });
    }


    //se ejecuta cuando la actividad se vuelve visible, si los metiera en oncreate solo se cargarian una vez
    @Override
    public void onResume() {
        super.onResume();


    }

    private void cargarDatos() {
        loader.setVisibility(View.VISIBLE);

        if (tipoEvento != null) {
            firebaseController.cargarDatosEventos(tipoEvento, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        listaEventos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Evento evento = document.toObject(Evento.class);
                            listaEventos.add(evento);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adaptador == null) {
                                    adaptador = new AdaptadorPersonalizado_Eventos(Activity_Eventos.this, listaEventos);
                                    rv_eventos.setAdapter(adaptador);
                                } else {
                                    adaptador.actualizarDatos(listaEventos);
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



    public void setupRecyclerView(RecyclerView recyclerView, ArrayList<Evento> listaEventos) {
        adaptador = new AdaptadorPersonalizado_Eventos(this, listaEventos);
        recyclerView.setAdapter(adaptador);
        //lambda, se le pasa un evento y se ejecuta el metodo onItemClick
        adaptador.setOnItemClickListener(evento -> {

            Intent intent = new Intent(Activity_Eventos.this, ActivityEntradas.class);
            Bundle bundle = new Bundle();


            bundle.putString("nombreEvento", evento.getNombre());
            bundle.putString("fecha", evento.getFecha());
            bundle.putString("lugar", evento.getLugar());
            bundle.putString("ciudad", evento.getCiudad());
            bundle.putString("comprarEntrada", evento.getCompraEntrada());
            bundle.putString("imagen", evento.getImagenUrl());
            bundle.putString("genero", evento.getGenero());
            bundle.putString("precio", evento.getPrecio());
            bundle.putString("tipoEvento", tipoEvento);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    //boton buscar
    private void buscarConciertos(String texto) {
        ArrayList<Evento> listaFiltrada = new ArrayList<>();

        for (Evento ev : listaEventos) {
            if (ev.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(ev);
            }
        }

        //se actualiza la lista con lo buscado
        setupRecyclerView(rv_eventos, listaFiltrada);
    }

    private void metodosFind() {
        et_buscador = findViewById(R.id.et_buscador);
        rv_eventos = findViewById(R.id.rv_eventos);
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
            rv_eventos.getGlobalVisibleRect(outRectConciertos);
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

                if (tipoEvento != null) {
                    switch (tipoEvento) {
                        case "conciertos":
                            et_buscador.setHint(R.string.atopa_o_teu_concerto_favorito);
                            break;
                        case "festivales":
                            et_buscador.setHint(R.string.atopa_o_teu_festival_favorito);
                            break;
                        case "fiestas_populares":
                            et_buscador.setHint(R.string.atopa_a_tua_festa_popular_favorita);
                            break;
                    }
                }
            }
        });
    }

    private void filtradorProvincia() {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(cb_acoruna);
        checkBoxes.add(cb_lugo);
        checkBoxes.add(cb_ourense);
        checkBoxes.add(cb_pontevedra);

        if (tipoEvento != null) {
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
                            firebaseController.cargarEventosPorProvincia(tipoEvento, checkBox, new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        listaEventos.clear();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Conciertos concierto = document.toObject(Conciertos.class);
                                            listaEventos.add(concierto);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (adaptador == null) {
                                                    adaptador = new AdaptadorPersonalizado_Eventos(Activity_Eventos.this, listaEventos);
                                                    rv_eventos.setAdapter(adaptador);
                                                } else {
                                                    adaptador.actualizarDatos(listaEventos);
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
                        }else{
                            cargarDatos();

                        }
                    }
                });
            }
        }
    }

    private void buscadorEventos() {
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