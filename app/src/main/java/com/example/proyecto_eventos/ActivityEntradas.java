package com.example.proyecto_eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import controladores.ClaseParaBBDD;
import disenho.AdaptadorPersonalizado_Conciertos;
import disenho.ConciertosAdapter;
import modelos.Conciertos;

public class ActivityEntradas extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_artista, tv_fecha, tv_lugar, tv_precioConcierto;
    private ImageView iv_imagen_concierto, opc_home_entradas;
    private Button btn_entradas;
    private ArrayList<Conciertos> listaConciertos = new ArrayList<>();
    private ConciertosAdapter conciertosAdapter;
    private FirebaseFirestore mfirestore;
    private String comprarEntrada, genero;

    private RecyclerView rv_entradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas);
        mfirestore = FirebaseFirestore.getInstance();//se obtiene la instancia de Firestore
        //miClase = new ClaseParaBBDD(this, "bbdd_aplicacion.db", null, 1);
        //db = miClase.getWritableDatabase();
        //listaConciertos = (ArrayList<Conciertos>) miClase.lista();

        metodosFind();
        metodosSetOn();
        recibirDatosBundle();
        volverMenu();


        RecyclerView recyclerViewConciertos = findViewById(R.id.rv_entradas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewConciertos.setLayoutManager(layoutManager);

        conciertosAdapter = new ConciertosAdapter(this, listaConciertos);
        recyclerViewConciertos.setAdapter(conciertosAdapter);




        //para que se vea un concierto a la vez
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewConciertos);

    }

    @Override
    public void onResume() {
        super.onResume();

        cargarDatos();
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
            String precio = bundle.getString("precio");

            genero = bundle.getString("genero");
            comprarEntrada = bundle.getString("comprarEntrada");
            tv_artista.setText("Artista: "+nombreConcierto);
            tv_precioConcierto.setText("Precio: "+precio);
            tv_fecha.setText("Fecha: "+fecha);
            tv_lugar.setText("Lugar: "+lugar+ ", "+ciudad);


        }
    }
    private void cargarDatos() {//se cargan los datos en el recyclerView horizontal
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Muestra los conciertos que tengan el mismo genero que el concierto seleccionado
        db.collection("conciertos")
                .whereEqualTo("genero", genero)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        listaConciertos.clear();
                        String nombreArtistaSuperior = tv_artista.getText().toString().replace("Artista: ", "");
                        for (Conciertos concierto : queryDocumentSnapshots.toObjects(Conciertos.class)) {
                            if (!concierto
                                    .getNombre().equals(nombreArtistaSuperior)) {//Si no es igual se va añadiendo a la lista
                                listaConciertos.add(concierto);
                            }
                        }
                        conciertosAdapter.actualizarDatos(listaConciertos);
                    }
                });
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
        tv_artista = findViewById(R.id.tv_artista);
        tv_fecha = findViewById(R.id.tv_fecha);
        tv_lugar = findViewById(R.id.tv_lugar);
        iv_imagen_concierto = findViewById(R.id.iv_imagen_concierto);
        tv_precioConcierto = findViewById(R.id.tv_precioConcierto);
        opc_home_entradas = findViewById(R.id.opc_home_entradas);
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