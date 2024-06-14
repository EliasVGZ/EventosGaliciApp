package controladores;

import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseController {

    private FirebaseFirestore db;

    public FirebaseController() {
        db = FirebaseFirestore.getInstance();
    }

    public void cargarDatosEventos(String tipoEvento, OnCompleteListener<QuerySnapshot> listener) {
        db.collection(tipoEvento).get().addOnCompleteListener(listener);
    }

    public void cargarEventosRelacionados(String tipoEvento, String genero, OnCompleteListener<QuerySnapshot> listener) {
        db.collection(tipoEvento).whereEqualTo("genero", genero).get().addOnCompleteListener(listener);
    }
    public void cargarEventosPorProvincia(String tipoEvento, CheckBox checkBox, OnCompleteListener<QuerySnapshot> listener) {
        if (checkBox.isChecked()) {
            String ciudad = checkBox.getText().toString();
            db.collection(tipoEvento).whereEqualTo("ciudad", ciudad).get().addOnCompleteListener(listener);
        }
    }
}
