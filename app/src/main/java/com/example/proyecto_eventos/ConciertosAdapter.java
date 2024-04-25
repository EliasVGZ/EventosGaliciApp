package com.example.proyecto_eventos;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import modelos.Conciertos;

public class ConciertosAdapter extends RecyclerView.Adapter<ConciertosAdapter.ConciertosViewHolder> {
    private ArrayList<Conciertos> listaConciertos ;

    public ConciertosAdapter(ArrayList<Conciertos> listaConciertos) {
        this.listaConciertos = listaConciertos;
    }

    @NonNull
    @Override
    public ConciertosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí debes inflar la vista de cada elemento de la lista
        // y devolver un nuevo ViewHolder
        return null;
    }



    @Override
    public void onBindViewHolder(@NonNull ConciertosViewHolder holder, int position) {
        // Aquí debes actualizar la vista del ViewHolder con los datos del concierto
    }

    @Override
    public int getItemCount() {
        return listaConciertos.size();
    }

    public static class ConciertosViewHolder extends RecyclerView.ViewHolder {
        public ConciertosViewHolder(@NonNull View itemView) {
            super(itemView);
            // Aquí debes encontrar las vistas dentro de itemView
        }
    }
}
