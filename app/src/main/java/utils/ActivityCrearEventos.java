package utils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_eventos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityCrearEventos extends AppCompatActivity {

    private EditText etCiudad, etCompraEntrada, etFecha, etGenero, etId, etImagen, etImagenUrl, etLugar, etNombreConciertos, etPrecio,  et_nombreId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etCiudad = findViewById(R.id.et_ciudad);
        etCompraEntrada = findViewById(R.id.et_compraEntrada);
        etFecha = findViewById(R.id.et_fecha);
        etGenero = findViewById(R.id.et_genero);
        etId = findViewById(R.id.et_id);
        etImagen = findViewById(R.id.et_imagen);
        etImagenUrl = findViewById(R.id.et_imagenUrl);
        etLugar = findViewById(R.id.et_lugar);
        etNombreConciertos = findViewById(R.id.et_nombreConciertos);
        etPrecio = findViewById(R.id.et_precio);
        et_nombreId = findViewById(R.id.et_nombreId);

        db = FirebaseFirestore.getInstance();

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombreId = et_nombreId.getText().toString();

                db.collection("conciertos").document(nombreId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(ActivityCrearEventos.this, R.string.concerto_existe, Toast.LENGTH_SHORT).show();
                            } else {
                                String ciudad = etCiudad.getText().toString();
                                String compraEntrada = etCompraEntrada.getText().toString();
                                String fecha = etFecha.getText().toString();
                                String genero = etGenero.getText().toString();
                                int id = Integer.parseInt(etId.getText().toString());
                                String imagenUrl = etImagenUrl.getText().toString();
                                String lugar = etLugar.getText().toString();
                                String nombreConciertos = etNombreConciertos.getText().toString();
                                String precio = etPrecio.getText().toString();

                                Map<String, Object> concierto = new HashMap<>();
                                concierto.put("nombreId", nombreId);
                                concierto.put("ciudad", ciudad);
                                concierto.put("compraEntrada", compraEntrada);
                                concierto.put("fecha", fecha);
                                concierto.put("genero", genero);
                                concierto.put("id", id);
                                concierto.put("imagenUrl", imagenUrl);
                                concierto.put("lugar", lugar);
                                concierto.put("nombreConciertos", nombreConciertos);
                                concierto.put("precio", precio);

                                db.collection("conciertos").document(nombreId).set(concierto)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ActivityCrearEventos.this, R.string.concerto_agregado, Toast.LENGTH_SHORT).show();
                                                limpiarCampos();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ActivityCrearEventos.this, R.string.concerto_erro, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.d("Firestore", "Error al consultar documento: ", task.getException());
                        }
                    }
                });
            }
        });
    }
    private void limpiarCampos() {
        et_nombreId.setText("");
        etCiudad.setText("");
        etCompraEntrada.setText("");
        etFecha.setText("");
        etGenero.setText("");
        etId.setText("");
        etImagenUrl.setText("");
        etLugar.setText("");
        etNombreConciertos.setText("");
        etPrecio.setText("");
    }
}