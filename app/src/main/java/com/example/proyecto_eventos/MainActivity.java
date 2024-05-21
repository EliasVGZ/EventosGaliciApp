package com.example.proyecto_eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnConciertos, btnFestivales, btnFestPopulares;
    private disenho.CircularImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metodosFind();
        metodosSetOn();


        Animation slideFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);
        Animation slideFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        btnConciertos.startAnimation(slideFromRight);
        btnFestivales.startAnimation(slideFromLeft);
        btnFestPopulares.startAnimation(slideFromRight);

        final Animation moveAndScale = AnimationUtils.loadAnimation(this, R.anim.move_and_scale);

        imgLogo.startAnimation(moveAndScale);

        imgLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnConciertos.setVisibility(View.VISIBLE);
                btnConciertos.startAnimation(slideFromRight);
                btnFestivales.setVisibility(View.VISIBLE);
                btnFestivales.startAnimation(slideFromLeft);
                btnFestPopulares.setVisibility(View.VISIBLE);
                btnFestPopulares.startAnimation(slideFromRight);
            }
        }, 2000);


    }

    private void metodosSetOn() {
        btnConciertos.setOnClickListener(this);
        btnFestivales.setOnClickListener(this);
        btnFestPopulares.setOnClickListener(this);

    }

    private void metodosFind() {
        btnConciertos = findViewById(R.id.btnConciertos);
        btnFestivales = findViewById(R.id.btnFestivales);
        btnFestPopulares = findViewById(R.id.btnFestPopulares);
        imgLogo = findViewById(R.id.imgLogo);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnConciertos:
                Intent intent = new Intent(MainActivity.this, Activity_Conciertos.class);
                startActivity(intent);

            break;
            case R.id.btnFestivales:
                //llamar a la actividad de festivales
            break;
            case R.id.btnFestPopulares:
                //llamar a la actividad de festivales populares
                break;
        }

    }
}