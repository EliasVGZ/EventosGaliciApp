package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import dialogs.LoginDialog;

public class Activity_Menu extends AppCompatActivity implements View.OnClickListener{

    private Button btnConciertos, btnFestivales, btnFestPopulares;
    private ImageView opc_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        metodosFind();
        metodosSetOn();


        opc_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog loginDialog = new LoginDialog();
                loginDialog.show(getSupportFragmentManager(), "login dialog");
            }
        });
    }

    private void metodosSetOn() {
        btnConciertos.setOnClickListener( this);
        btnFestivales.setOnClickListener(this);
        btnFestPopulares.setOnClickListener(this);
        opc_login.setOnClickListener(this);

    }

    private void metodosFind() {
        btnConciertos = findViewById(R.id.btnConciertos);
        btnFestivales = findViewById(R.id.btnFestivales);
        btnFestPopulares = findViewById(R.id.btnFestPopulares);
        opc_login = findViewById(R.id.opc_login);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConciertos:
                Intent intent = new Intent(Activity_Menu.this, Activity_Conciertos.class);
                startActivity(intent);
                break;
            case R.id.btnFestivales:
            case R.id.btnFestPopulares:
                // todo
                break;
        }
    }
}