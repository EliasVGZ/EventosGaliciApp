package com.example.proyecto_eventos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import modelos.Conciertos;

public class AdaptadorPersonalizado_Conciertos extends ArrayAdapter {

    private Activity context;
    private ArrayList<Conciertos> listaConciertos;
    private int layoutPersonalizado;

    public AdaptadorPersonalizado_Conciertos(@NonNull Activity context,
                                             int layoutPersonalizado,
                                             ArrayList<Conciertos> listaConciertos) {
        super(context, layoutPersonalizado, listaConciertos);

        this.context = context;
        this.layoutPersonalizado = layoutPersonalizado;
        this.listaConciertos = listaConciertos;
    }

    private static class ViewHolder {

        TextView tv_nombreConcierto;
        TextView tv_lugarConcierto;
        TextView tv_fechaConcierto;
        TextView tv_generoConcierto;
        TextView tv_precioConcierto;
        TextView tv_ciudad;
        ImageView iv_concierto;


    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Conciertos concierto = (Conciertos) getItem(position);
        View fila = convertView;
        ViewHolder holder;

        // Optimizamos el inflado
        if (fila == null) {
            // Inflamos el layout personalizado
            LayoutInflater layoutInflater = context.getLayoutInflater();
            fila = layoutInflater.inflate(layoutPersonalizado, null);

            holder = new ViewHolder();
            holder.iv_concierto = fila.findViewById(R.id.iv_concierto);
            holder.tv_nombreConcierto = fila.findViewById(R.id.tv_nombreConcierto);
            holder.tv_lugarConcierto = fila.findViewById(R.id.tv_lugarConcierto);
            //holder.tv_ciudad = fila.findViewById(R.id.tv_ciudad);
            holder.tv_fechaConcierto = fila.findViewById(R.id.tv_fechaConcierto);
            holder.tv_generoConcierto = fila.findViewById(R.id.tv_generoConcierto);
            holder.tv_precioConcierto = fila.findViewById(R.id.tv_precioConcierto);

            fila.setTag(holder); // Guardamos los atributos dentro del holder
        } else {
            holder = (ViewHolder) fila.getTag();
        }

        // Asignamos los datos del concierto a las vistas
        holder.tv_nombreConcierto.setText(concierto.getNombreConciertos());
        holder.tv_lugarConcierto.setText(concierto.getLugar()+", "+concierto.getCiudad());
        //holder.tv_ciudad.setText(concierto.getCiudad());
        holder.tv_fechaConcierto.setText(concierto.getFecha());
        holder.tv_generoConcierto.setText(concierto.getGenero());
        holder.tv_precioConcierto.setText(String.valueOf(concierto.getPrecio()));

        // Cargamos la imagen correspondiente al concierto en el ImageView
        holder.iv_concierto.setImageResource(obtenerIdImagen(concierto.getImagen()));

        return fila;
    }

    @SuppressLint("DiscouragedApi")
    public int obtenerIdImagen(int idImagen) {

        return getContext().getResources().getIdentifier("imagen" + idImagen, "drawable", getContext().getPackageName());
    }

}
