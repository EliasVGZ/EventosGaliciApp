package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Locale;

import dialogs.LoginDialog;

public class Activity_Menu extends AppCompatActivity implements View.OnClickListener{

    private Button btnConciertos, btnFestivales, btnFestPopulares;
    private ImageView opc_login, btn_gallego, btn_espanol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        metodosFind();
        metodosSetOn();
        setGallegoButtonListener();
        setEspanolButtonListener();

        opc_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog loginDialog = new LoginDialog();
                loginDialog.show(getSupportFragmentManager(), "login dialog");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void metodosSetOn() {
        btnConciertos.setOnClickListener( this);
        btnFestivales.setOnClickListener(this);
        btnFestPopulares.setOnClickListener(this);
        opc_login.setOnClickListener(this);
        btn_gallego.setOnClickListener(this);
        btn_espanol.setOnClickListener(this);

    }

    private void metodosFind() {
        btnConciertos = findViewById(R.id.btnConciertos);
        btnFestivales = findViewById(R.id.btnFestivales);
        btnFestPopulares = findViewById(R.id.btnFestPopulares);
        opc_login = findViewById(R.id.opc_login);
        btn_gallego = findViewById(R.id.btn_gallego);
        btn_espanol = findViewById(R.id.btn_espanol);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Activity_Menu.this, Activity_Eventos.class);
        switch (v.getId()) {
            case R.id.btnConciertos:
                intent.putExtra("tipoEvento", "conciertos");
                break;
            case R.id.btnFestivales:
                intent.putExtra("tipoEvento", "festivales");
                break;
            case R.id.btnFestPopulares:
                intent.putExtra("tipoEvento", "fiestas_populares");
                break;
        }
        startActivity(intent);
    }
    private void cambiarIdioma(String idioma) {

        String idiomaActual = getResources().getConfiguration().locale.getLanguage();

        // si el idioma es el mismo no se actualizar
        if (idioma.equals(idiomaActual)) {
            return;
        }

        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        // guardar idioma seleccionado en las preferencias para usarlo la prox vez que se inicie la aapp
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("idioma", idioma);
        editor.apply();

        // Reinicia la actividad para aplicar los cambios
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setGallegoButtonListener() {
        btn_gallego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarIdioma("gl");
            }
        });
    }

    private void setEspanolButtonListener() {
        btn_espanol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarIdioma("es");
            }
        });
    }

}