package adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_eventos.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import models.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventosViewHolder> {
    private ArrayList<Evento> listaEventos;
    private Context context;

    public EventoAdapter(Context context, ArrayList<Evento> listaEventos) {
        this.context = context;
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public EventosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_personalizado_foot, parent, false);
        return new EventosViewHolder(view);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void actualizarDatos(ArrayList<Evento> nuevaLista) {
        this.listaEventos = nuevaLista;
        notifyDataSetChanged();
    }

    //para cada item de la lista se le asigna un concierto
    @Override
    public void onBindViewHolder(@NonNull EventosViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);

        // Configura la información del concierto
        holder.tv_nombreEvento.setText(evento.getNombre());
        holder.tv_lugarEvento.setText(evento.getLugar());
        holder.tv_fechaEvento.setText(evento.getFecha());
        holder.tv_generoEvento.setText(evento.getGenero());
        holder.tv_precioEvento.setText(evento.getPrecio());

        // Obtén la URL de descarga de Firebase Storage y carga la imagen con Glide
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(evento.getImagenUrl());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri.toString())
                        .fitCenter()
                        .into(holder.iv_evento);
            }
        });
    }



    @Override//metodo que devuelve el numero de elementos que tiene la lista
    public int getItemCount() {
        return listaEventos.size();
    }

    // Clase interna, que extiende de RecyclerView.ViewHolder para poder acceder a los elementos de la vista
    public class EventosViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombreEvento, tv_lugarEvento, tv_fechaEvento, tv_generoEvento, tv_precioEvento;
        ImageView iv_evento;

        public EventosViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombreEvento = itemView.findViewById(R.id.tv_nombreEvento);
            tv_lugarEvento = itemView.findViewById(R.id.tv_lugarEvento);
            tv_fechaEvento = itemView.findViewById(R.id.tv_fechaEvento);
            tv_generoEvento = itemView.findViewById(R.id.tv_generoEvento);
            tv_precioEvento = itemView.findViewById(R.id.tv_precioEvento);
            iv_evento = itemView.findViewById(R.id.iv_evento);

            // ancho de la pantalla para que cada item se meta en una sola fila
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;

            // Establece el ancho de itemView (que es la CardView) al ancho de la pantalla
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = screenWidth;
            itemView.setLayoutParams(layoutParams);
        }
    }

    public int obtenerIdImagen(int idImagen) {
        return context.getResources().getIdentifier("concierto_" + idImagen, "drawable", context.getPackageName());
    }
}