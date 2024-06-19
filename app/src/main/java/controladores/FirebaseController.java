package controladores;

import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

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
    public Task<Void> crearEvento(String tipoEvento, String nombreId, Map<String, Object> evento) {
        return db.collection(tipoEvento).document(nombreId).set(evento);
    }

    public void cerrarSesion() {
        mAuth.signOut();
    }

    /**
     * DocumentSnapshot: Esta clase representa un snapshot de un solo documento en Firestore.
     * Contiene los datos del documento, así como metadatos sobre el documento, como su ID y si existe.
     * Puedes utilizar los métodos de DocumentSnapshot para acceder a los datos del documento y convertirlos
     * en objetos de tu aplicación.
     *
     * QuerySnapshot: Esta clase representa el resultado de una consulta a Firestore.
     * Contiene cero o más DocumentSnapshot objetos, cada uno de los cuales contiene
     * los datos de un documento que coincide con la consulta. Puedes utilizar los métodos
     * de QuerySnapshot para iterar sobre los documentos devueltos por la consulta y trabajar con los datos de cada documento.
     */
}
