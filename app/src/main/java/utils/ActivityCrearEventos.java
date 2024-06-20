package utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import com.example.proyecto_eventos.Activity_Menu;
import com.example.proyecto_eventos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import controladores.FirebaseController;

public class ActivityCrearEventos extends AppCompatActivity {

    private EditText et_provincia, etCompraEntrada, etFecha, etGenero, etImagenUrl, etLugar, etNombreConciertos, etPrecio,  et_nombreId;
    private Spinner sp_provincia;
    private FirebaseFirestore db;
    private FirebaseController fbcontroller = new FirebaseController();
    private String tipoEvento;
    private ImageView iv_insertarImagen;
    private Button btnSubmit, btn_subirImagen, btn_seleccionarImagen;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private StorageReference miStorageRef;
    private Uri imageUri;//se almacena la imagen seleccionada
    private ProgressBar pb_esperaSubirImagen;
    private LinearLayout ll_rellenarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
        tipoEvento = getIntent().getStringExtra("tipoEvento");
        sp_provincia = findViewById(R.id.sp_provincia);
        etCompraEntrada = findViewById(R.id.et_compraEntrada);
        etFecha = findViewById(R.id.et_fecha);
        etGenero = findViewById(R.id.et_genero);
        etImagenUrl = findViewById(R.id.et_imagenUrl);
        etLugar = findViewById(R.id.et_lugar);
        etNombreConciertos = findViewById(R.id.et_nombreConciertos);
        etPrecio = findViewById(R.id.et_precio);
        et_nombreId = findViewById(R.id.et_nombreId);
        btnSubmit = findViewById(R.id.btn_submit);
        btn_subirImagen = findViewById(R.id.btn_subirImagen);
        btn_seleccionarImagen = findViewById(R.id.btn_seleccionarImagen);
        iv_insertarImagen = findViewById(R.id.iv_insertarImagen);
        pb_esperaSubirImagen = findViewById(R.id.pb_esperaSubirImagen);
        ll_rellenarEvento = findViewById(R.id.ll_rellenarEvento);

        miStorageRef = FirebaseStorage.getInstance().getReference();

        //esto verifica si la app tiene permiso para acceder a la galeria, sino la tiene se lo pide a la persona
        //mediante el metodo onRequestPermissionsResult
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }



        String[] provincias = new String[]{"","A Coruña", "Pontevedra", "Ourense", "Lugo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provincias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_provincia.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombreId = et_nombreId.getText().toString();
                String ciudad = sp_provincia.getSelectedItem().toString();
                String compraEntrada = etCompraEntrada.getText().toString();
                String fecha = etFecha.getText().toString();
                String genero = etGenero.getText().toString();
                String imagenUrl = etImagenUrl.getText().toString();
                String lugar = etLugar.getText().toString();
                String nombre = etNombreConciertos.getText().toString();
                String precio = etPrecio.getText().toString();

                if (nombreId.isEmpty() || ciudad.isEmpty() || compraEntrada.isEmpty() || fecha.isEmpty() || genero.isEmpty() || imagenUrl.isEmpty() || lugar.isEmpty() || nombre.isEmpty() || precio.isEmpty()) {
                    Toast.makeText(ActivityCrearEventos.this, "Todos os campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> evento = new HashMap<>();
                evento.put("nombreId", nombreId);
                evento.put("ciudad", ciudad);
                evento.put("compraEntrada", compraEntrada);
                evento.put("fecha", fecha);
                evento.put("genero", genero);
                evento.put("imagenUrl", imagenUrl);
                evento.put("lugar", lugar);
                evento.put("nombre", nombre);
                evento.put("precio", precio);

                fbcontroller.crearEvento(tipoEvento, nombreId, evento)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                new AlertDialog.Builder(ActivityCrearEventos.this)
                                        .setTitle(R.string.concerto_agregado)
                                        .setMessage(R.string.otro_evento)
                                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // El usuario quiere agregar otro evento, limpiar los campos
                                                limpiarCampos();
                                            }
                                        })
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(ActivityCrearEventos.this, Activity_Menu.class);
                                                intent.putExtra("verificarRolUsuario", true);
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActivityCrearEventos.this, R.string.concerto_erro, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



        //Seleccionar una iamgen dentro del movil
        btn_seleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);//100 es el codigo de respuesta
            }
        });

        btn_subirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    pb_esperaSubirImagen.setVisibility(View.VISIBLE);
                    StorageReference fileReference = miStorageRef.child(tipoEvento + "/" + System.currentTimeMillis() + ".jpg");//nombre de la imagen
                    fileReference.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pb_esperaSubirImagen.setVisibility(View.GONE);
                                            ll_rellenarEvento.setVisibility(View.VISIBLE);
                                            etImagenUrl.setText(uri.toString());
                                            Toast.makeText(ActivityCrearEventos.this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ActivityCrearEventos.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(ActivityCrearEventos.this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    //insertar la iamgen dentro de iv_insertarImagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data.getData();
            iv_insertarImagen.setImageURI(imageUri);
        }
    }
    private void limpiarCampos() {
        et_nombreId.setText("");
        sp_provincia.setSelection(0);
        etCompraEntrada.setText("");
        etFecha.setText("");
        etGenero.setText("");
        etImagenUrl.setText("");
        etLugar.setText("");
        etNombreConciertos.setText("");
        etPrecio.setText("");
        iv_insertarImagen.setImageDrawable(null);
    }

    //permiso para entrar en la galeria
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }
}