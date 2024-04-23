package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnConciertos, btnFestivales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metodosFind();
        metodosSetOn();


    }

    private void metodosSetOn() {
        btnConciertos.setOnClickListener(this);
        btnFestivales.setOnClickListener(this);
    }

    private void metodosFind() {
        btnConciertos = findViewById(R.id.btnConciertos);
        btnFestivales = findViewById(R.id.btnFestivales);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnConciertos:
                Intent intent = new Intent(MainActivity.this, Activity_Conciertos.class);
                startActivity(intent);

            break;
            case R.id.btnFestivales:
            break;
        }

    }
}