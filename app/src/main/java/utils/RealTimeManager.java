package utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;

import modelos.Conciertos;

public class RealTimeManager {

    private static RealTimeManager instance;
    private DatabaseReference databaseReference;

    public RealTimeManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("conciertos");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("festivales");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("fiestaspopulares");
    }

    public static RealTimeManager getInstance() {
        if (instance == null) {
            instance = new RealTimeManager();
        }
        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }



    //Devuelve un livedata con la lista de conciertos para que la actividad principal pueda observarla
    public LiveData<List<Conciertos>> getConciertos() {
        MutableLiveData<List<Conciertos>> conciertosLiveData = new MutableLiveData<>();
        DatabaseReference conciertosRef = FirebaseDatabase.getInstance().getReference().child("conciertos");

        conciertosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Conciertos> conciertos = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Conciertos concierto = postSnapshot.getValue(Conciertos.class);
                    conciertos.add(concierto);
                    Log.d("FirebaseData", "Concierto: " + concierto.getNombre());
                }
                conciertosLiveData.setValue(conciertos);
                Log.d("FirebaseData", "Number of concerts read: " + conciertos.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error al leer los datos
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return conciertosLiveData;
    }
}
