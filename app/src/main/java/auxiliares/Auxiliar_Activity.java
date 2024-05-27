package auxiliares;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_eventos.R;

public class Auxiliar_Activity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opciones, menu);
        return super.onCreateOptionsMenu(menu);
    }


/*
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.opc_buscador:
                getBuscadorEditText().setVisibility(View.VISIBLE);
                return true;
            case R.id.opc_filtro:
                getFiltroEditText().setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    //creo clase abstracta para que las clases que hereden de ella tengan que implementar este metodo
    public EditText getBuscadorEditText() {
        return findViewById(R.id.et_buscador);
    }
    public EditText getFiltroEditText() {
        return findViewById(R.id.et_filtro_ciudad);
    }*/

}