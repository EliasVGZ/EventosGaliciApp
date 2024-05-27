package com.example.proyecto_eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import controladores.ClaseParaBBDD;
import disenho.ConciertosAdapter;
import modelos.Conciertos;

public class ActivityEntradas extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_artista, tv_fecha, tv_lugar;
    private ImageView iv_imagen_concierto;
    private Button btn_entradas;
    private ClaseParaBBDD miClase;
    private static SQLiteDatabase db;
    ArrayList<Conciertos> listaConciertos;

    private String comprarEntrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas);
        miClase = new ClaseParaBBDD(this, "bbdd_aplicacion.db", null, 1);
        db = miClase.getWritableDatabase();
        listaConciertos = (ArrayList<Conciertos>) miClase.lista();

        metodosFind();
        metodosSetOn();
        recibirDatosBundle();


        RecyclerView recyclerViewConciertos = findViewById(R.id.rv_entradas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewConciertos.setLayoutManager(layoutManager);

        ConciertosAdapter conciertosAdapter = new ConciertosAdapter(this, listaConciertos);
        recyclerViewConciertos.setAdapter(conciertosAdapter);

        //para que se vea un concierto a la vez
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewConciertos);

    }

    @SuppressLint("SetTextI18n")
    private void recibirDatosBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            /*String imagenId = bundle.getString("imagen"); // Aquí recibo el id de la imagen
            String nombreImagen = "concierto_" + imagenId;
            int resId = getResources().getIdentifier(nombreImagen, "drawable", getPackageName());
            iv_imagen_concierto.setImageResource(resId);*/
            String imagenUrl = bundle.getString("imagen"); // Aquí recibo la URL de la imagen

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imagenUrl);

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ActivityEntradas.this).load(uri.toString()).into(iv_imagen_concierto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });



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
                //esto es para que haga una animacion al pulsar el boton
                Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
                v.startAnimation(scaleAnimation);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(comprarEntrada));
                startActivity(i);
                break;

        }

    }


}