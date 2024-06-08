package com.example.proyecto_eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import modelos.Administrador;
import modelos.Evento;

public class ActivitySubirImagen extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button button_choose_image;
    private Button button_upload;
    private ImageView image_view;
    private Uri mImageUri;

    private Administrador administrador = new Administrador("nombre_administrador");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_imagen);

        button_choose_image = findViewById(R.id.button_choose_image);
        button_upload = findViewById(R.id.button_upload);
        image_view = findViewById(R.id.image_view);

        button_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aqui depende de que evento elija le paso el nombre del evento
                Evento evento = new Evento();
                evento.setNombre("nombre_del_evento");
                administrador.subirImagenEvento(evento, mImageUri);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this).load(mImageUri).into(image_view);
        }
    }


}