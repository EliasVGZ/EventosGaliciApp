package com.example.proyecto_eventos;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Locale;

import controladores.FirebaseController;
import dialogs.LoginDialog;
import utils.ActivityCrearEventos;

public class Activity_Menu extends AppCompatActivity implements View.OnClickListener{

    private Button btnConciertos, btnFestivales, btnFestPopulares;
    private ImageView opc_login, btn_gallego, btn_espanol, opc_puntos, btn_ingles;
    private FirebaseController fbcontroller = new FirebaseController();
    private FirebaseUser user ;
    private String tipoEventoSeleccionado;
    private String rolUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        user = FirebaseAuth.getInstance().getCurrentUser();
        rolUsuario = getIntent().getStringExtra("rolUsuario");

        metodosFind();
        metodosSetOn();
        setGallegoButtonListener();
        setEspanolButtonListener();
        setEnglishButtonListener();
        verificarInicioSesion();
        mostrarMenuOpciones();

        if(rolUsuario != null){
            if(rolUsuario.equals("administrador")){
                opc_login.setImageDrawable(getDrawable(R.drawable.administrador));
            }else if(rolUsuario.equals("organizador")){
                opc_login.setImageDrawable(getDrawable(R.drawable.organizador));
            }else{
                opc_login.setImageDrawable(getDrawable(R.drawable.usuario));
            }
        }


    }

    public void confirmarCierreSesion() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.cerrar_sesion)
                .setMessage(R.string.seguro)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        fbcontroller.cerrarSesion();
                        //cierra sesion y vuelve el logo original
                        opc_login.setImageDrawable(getDrawable(R.drawable.icono_inicio_sesion));
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void verificarInicioSesion() {
        opc_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) { //el usuario ha iniciado sesion
                    if(rolUsuario != null){
                        String rolFormatted = rolUsuario.substring(0, 1).toUpperCase() + rolUsuario.substring(1).toLowerCase();
                        // MUESTRO EL menu con el rol del usuario
                        PopupMenu popup = new PopupMenu(Activity_Menu.this, opc_login);
                        popup.getMenuInflater().inflate(R.menu.menu_opciones, popup.getMenu());
                        MenuItem crearEventoItem = popup.getMenu().findItem(R.id.opcionRol);
                        crearEventoItem.setTitle(rolFormatted);


                    //Dentro de popup se crea un menu con las opciones, al clicar la opcionsalir se cierra sesion
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.opcionSalir) {
                                confirmarCierreSesion();
                            }
                            return true;
                        }
                    });
                    popup.show();
                    }
                } else {
                    // El usuario no ha iniciado sesión
                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.show(getSupportFragmentManager(), "login dialog");
                }
            }
        });
    }

    private void mostrarMenuOpciones() {
        opc_puntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    PopupMenu popup = new PopupMenu(Activity_Menu.this, opc_puntos);
                    popup.getMenuInflater().inflate(R.menu.menu_usuario, popup.getMenu());
                    popup.show();
                }
                if (user != null) {
                    fbcontroller.obtenerRolUsuario(user.getUid(), new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String rol = document.getString("rol");
                                    PopupMenu popup = new PopupMenu(Activity_Menu.this, opc_puntos);
                                    if (rol.equals("usuario")) {
                                        popup.getMenuInflater().inflate(R.menu.menu_usuario, popup.getMenu());
                                    } else if (rol.equals("administrador")) {
                                        popup.getMenuInflater().inflate(R.menu.menu_administrador, popup.getMenu());
                                    }else if(rol.equals("organizador")){
                                        popup.getMenuInflater().inflate(R.menu.menu_organizador, popup.getMenu());
                                    }
                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        public boolean onMenuItemClick(MenuItem item) {
                                            if (item.getItemId() == R.id.opcionCrearEvento) {
                                                mostrarDialogoCrearEvento();
                                            }
                                            return true;
                                        }
                                    });
                                    popup.show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void mostrarDialogoCrearEvento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selecciona_tipo_evento);

        String[] tiposEventos = new String[]{
                getString(R.string.concertos),
                getString(R.string.festivais),
                getString(R.string.festPopulares)
        };
        int checkedItem = 0; //0 por defecto
        //por defecto el tipo de evento seleccionado es conciertos, sino no no me selecciona ninguno
        tipoEventoSeleccionado = "conciertos";

        builder.setSingleChoiceItems(tiposEventos, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        tipoEventoSeleccionado = "conciertos";
                        break;
                    case 1:
                        tipoEventoSeleccionado = "festivales";
                        break;
                    case 2:
                        tipoEventoSeleccionado = "fiestas_populares";
                        break;
                }
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Comprueba si el usuario ha seleccionado un tipo de evento
                if (tipoEventoSeleccionado == null) {
                    Toast.makeText(Activity_Menu.this, "Por favor, selecciona un tipo de evento para continuar", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Activity_Menu.this, ActivityCrearEventos.class);
                    intent.putExtra("tipoEvento", tipoEventoSeleccionado);
                    startActivity(intent);
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
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
        opc_puntos.setOnClickListener(this);
        btn_ingles.setOnClickListener(this);

    }

    private void metodosFind() {
        btnConciertos = findViewById(R.id.btnConciertos);
        btnFestivales = findViewById(R.id.btnFestivales);
        btnFestPopulares = findViewById(R.id.btnFestPopulares);
        opc_login = findViewById(R.id.opc_login);
        btn_gallego = findViewById(R.id.btn_gallego);
        btn_espanol = findViewById(R.id.btn_espanol);
        opc_puntos = findViewById(R.id.opc_puntos);
        btn_ingles = findViewById(R.id.btn_ingles);
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

        String idiomaActual = getResources().getConfiguration().locale.toString();

        // si el idioma es el mismo no se actualizar
        if (idioma.equals(idiomaActual)) {
            return;
        }

        Locale locale = new Locale(idioma);// idioma seleccionado
        Locale.setDefault(locale);// establecer idioma seleccionado como predeterminado
        Configuration config = new Configuration();// configuracion de la aplicacion
        config.locale = locale;// establecer idioma seleccionado
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());// actualizar configuracion de la aplicacion

        // guardar idioma seleccionado en las preferencias para usarlo la prox vez que se inicie la aapp
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("idioma", idioma);
        editor.apply();

        // se necesita reiniciar la actividad para aplicar los cambios, seguro habra otra forma mas dinamica de hacerlo, pero es la unica que pud encontrar
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
    private void setEnglishButtonListener() {
        btn_ingles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarIdioma("en");
            }
        });
    }

}