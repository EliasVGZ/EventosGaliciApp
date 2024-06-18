package controladores;

import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseController {
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    public FirebaseController() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void cargarDatosEventos(String tipoEvento, OnCompleteListener<QuerySnapshot> listener) {
        db.collection(tipoEvento).orderBy("fecha").get().addOnCompleteListener(listener);
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
    public void obtenerRolUsuario(String uid, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("usuarios").document(uid).get().addOnCompleteListener(listener);
    }

    public void cerrarSesion() {
        mAuth.signOut();
    }
}
