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

    public void cargarDatosConciertos(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("conciertos").get().addOnCompleteListener(listener);
    }

    public void cargarConciertosRelacionados(String genero, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("conciertos").whereEqualTo("genero", genero).get().addOnCompleteListener(listener);
    }
    public void cargarConciertosPorCiudad(CheckBox checkBox, OnCompleteListener<QuerySnapshot> listener) {
        if (checkBox.isChecked()) {
            String ciudad = checkBox.getText().toString();
            db.collection("conciertos").whereEqualTo("ciudad", ciudad).get().addOnCompleteListener(listener);
        }
    }
}
