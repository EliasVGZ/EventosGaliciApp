package com.example.proyecto_eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import controladores.FirebaseController;

public class MainActivity extends AppCompatActivity {

    private FirebaseController fbcontroller = new FirebaseController();
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //handler sirve para que la pantalla de inicio dure 3 segundos
        handler = new Handler();
        runnable = new Runnable(){//Runnable es una interfaz que se utiliza para ejecutar un bloque de código en un hilo separado
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Activity_Menu.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable, 2000);

        comprobarRolUsuario();
    }

    //Necesito comprobar si el usuario ya ha iniciado sesion para poder actualizar el icono de usuario
    private void comprobarRolUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            fbcontroller.obtenerRolUsuario(user.getUid(), new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String rolUsuario = document.getString("rol");

                            Intent intent = new Intent(MainActivity.this, Activity_Menu.class);
                            intent.putExtra("rolUsuario", rolUsuario);
                            Log.d("ROL", rolUsuario);
                            startActivity(intent);
                            handler.removeCallbacks(runnable);//Para que no se ejecute el intent de arriba
                            finish();

                        }
                    }
                }
            });
        }
    }
}