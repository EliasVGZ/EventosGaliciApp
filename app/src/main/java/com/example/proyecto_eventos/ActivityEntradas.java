package com.example.proyecto_eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import adapters.EventoAdapter;
import controladores.FirebaseController;
import models.Conciertos;
import models.Evento;

public class ActivityEntradas extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_evento, tv_fecha, tv_lugar, tv_precioEvento;
    private ImageView iv_imagen_evento, opc_home_entradas, iv_corazonRojo, iv_corazonNegro;
    private Button btn_entradas;
    private ArrayList<Evento> listaEventos = new ArrayList<>();
    private EventoAdapter eventosAdapter;
    private FirebaseFirestore mfirestore;
    private String comprarEntrada, genero, tipoEvento;
    private FirebaseController firebaseController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas);
        firebaseController = new FirebaseController();

        metodosFind();
        metodosSetOn();
        recibirDatosBundle();
        volverMenu();
        meGusta();


        RecyclerView recyclerViewEventos = findViewById(R.id.rv_entradas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEventos.setLayoutManager(layoutManager);

        eventosAdapter = new EventoAdapter(this, listaEventos);
        recyclerViewEventos.setAdapter(eventosAdapter);




        //para que se vea un concierto a la vez
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewEventos);

    }

    private void meGusta() {
        iv_corazonNegro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_corazonNegro.setImageResource(R.drawable.corazon);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        cargarEventoRelacionado();
    }



    @SuppressLint("SetTextI18n")
    private void recibirDatosBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String imagenUrl = bundle.getString("imagen"); // Aquí recibo la URL de la imagen
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imagenUrl);

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ActivityEntradas.this).load(uri.toString()).into(iv_imagen_evento);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });



            String nombreEvento = bundle.getString("nombreEvento");
            String fecha = bundle.getString("fecha");
            String lugar = bundle.getString("lugar");
            String ciudad = bundle.getString("ciudad");
            String precio = bundle.getString("precio");
            genero = bundle.getString("genero");
            comprarEntrada = bundle.getString("comprarEntrada");
            tipoEvento = bundle.getString("tipoEvento");

            tv_evento.setText(nombreEvento);
            String prezoString = getString(R.string.prezo);
            tv_precioEvento.setText(prezoString + precio);
            tv_fecha.setText("Fecha: "+fecha);
            tv_lugar.setText("Lugar: "+lugar+ ", "+ciudad);


        }
    }
    private void cargarEventoRelacionado() {
        if (tipoEvento != null) {
            firebaseController.cargarEventosRelacionados(tipoEvento, genero, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        listaEventos.clear();
                        String nombreEventoSuperior = tv_evento.getText().toString().replace("Evento: ", "");
                        for (Evento evento : task.getResult().toObjects(Evento.class)) {//convertir los objetos de la base de datos a objetos de la clase Evento
                            if (!evento.getNombre().equals(nombreEventoSuperior)) {
                                listaEventos.add(evento);
                            }
                        }
                        eventosAdapter.actualizarDatos(listaEventos);
                    }
                }
            });
        }
    }

    private void volverMenu() {
        opc_home_entradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityEntradas.this, Activity_Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//para que no se pueda volver atras
                startActivity(intent);
            }
        });
    }

    private void metodosSetOn() {
        btn_entradas.setOnClickListener(this);
    }

    private void metodosFind() {
        btn_entradas = findViewById(R.id.btn_entradas);
        tv_evento = findViewById(R.id.tv_evento);
        tv_fecha = findViewById(R.id.tv_fecha);
        tv_lugar = findViewById(R.id.tv_lugar);
        iv_imagen_evento = findViewById(R.id.iv_imagen_evento);
        tv_precioEvento = findViewById(R.id.tv_precioEvento);
        opc_home_entradas = findViewById(R.id.opc_home_entradas);
        iv_corazonNegro = findViewById(R.id.iv_corazonNegro);
        iv_corazonRojo = findViewById(R.id.iv_corazonRojo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_entradas:
                //esto es para que haga una animacion al pulsar el boton
                Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
                v.startAnimation(scaleAnimation);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(comprarEntrada));
                startActivity(i);
                break;

        }

    }


}