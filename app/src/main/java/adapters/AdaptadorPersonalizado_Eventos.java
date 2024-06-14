package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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

public class AdaptadorPersonalizado_Eventos extends RecyclerView.Adapter<AdaptadorPersonalizado_Eventos.ViewHolder> {

    private Context context;
    private ArrayList<Evento> listaEventos;

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void actualizarDatos(ArrayList<Evento> nuevaLista) {
        this.listaEventos = nuevaLista;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Evento evento);
    }


    public AdaptadorPersonalizado_Eventos(Context context, ArrayList<Evento> listaEventos) {
        this.context = context;
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_personalizado_eventos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);
        //Conciertos concierto = listaConciertos.get(position);
        holder.tv_nombreEvento.setText(evento.getNombre());
        holder.tv_lugarEvento.setText(evento.getLugar()+", "+evento.getCiudad());
        holder.tv_fechaEvento.setText(evento.getFecha());
        holder.tv_generoEvento.setText(evento.getGenero());
        holder.tv_precioEvento.setText(String.valueOf(evento.getPrecio()));
        //holder.iv_concierto.setImageResource(obtenerIdImagen(concierto.getImagen()));

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(evento);
                }
            }
        });
    }

    @Override//retorna la cantidad de elementos que tiene la lista
    public int getItemCount() {
        return listaEventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombreEvento, tv_lugarEvento, tv_fechaEvento, tv_generoEvento, tv_precioEvento;
        ImageView iv_evento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombreEvento = itemView.findViewById(R.id.tv_nombreEvento);
            tv_lugarEvento = itemView.findViewById(R.id.tv_lugarEvento);
            tv_fechaEvento = itemView.findViewById(R.id.tv_fechaEvento);
            tv_generoEvento = itemView.findViewById(R.id.tv_generoEvento);
            tv_precioEvento = itemView.findViewById(R.id.tv_precioEvento);
            iv_evento = itemView.findViewById(R.id.iv_evento);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(listaEventos.get(position));
                    }
                }
            });
        }
    }

    public int obtenerIdImagen(int idImagen) {
        return context.getResources().getIdentifier("concierto_" + idImagen, "drawable", context.getPackageName());
    }
}