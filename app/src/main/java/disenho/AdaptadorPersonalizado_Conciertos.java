package disenho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_eventos.R;

import java.util.ArrayList;

import modelos.Conciertos;

public class AdaptadorPersonalizado_Conciertos extends RecyclerView.Adapter<AdaptadorPersonalizado_Conciertos.ViewHolder> {

    private Context context;
    private ArrayList<Conciertos> listaConciertos;

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Conciertos concierto);
    }


    public AdaptadorPersonalizado_Conciertos(Context context, ArrayList<Conciertos> listaConciertos) {
        this.context = context;
        this.listaConciertos = listaConciertos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_personalizado_conciertos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conciertos concierto = listaConciertos.get(position);
        holder.tv_nombreConcierto.setText(concierto.getNombreConciertos());
        holder.tv_lugarConcierto.setText(concierto.getLugar()+", "+concierto.getCiudad());
        holder.tv_fechaConcierto.setText(concierto.getFecha());
        holder.tv_generoConcierto.setText(concierto.getGenero());
        holder.tv_precioConcierto.setText(String.valueOf(concierto.getPrecio()));
        holder.iv_concierto.setImageResource(obtenerIdImagen(concierto.getImagen()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(concierto);
                }
            }
        });
    }

    @Override//retorna la cantidad de elementos que tiene la lista
    public int getItemCount() {
        return listaConciertos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombreConcierto, tv_lugarConcierto, tv_fechaConcierto, tv_generoConcierto, tv_precioConcierto;
        ImageView iv_concierto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombreConcierto = itemView.findViewById(R.id.tv_nombreConcierto);
            tv_lugarConcierto = itemView.findViewById(R.id.tv_lugarConcierto);
            tv_fechaConcierto = itemView.findViewById(R.id.tv_fechaConcierto);
            tv_generoConcierto = itemView.findViewById(R.id.tv_generoConcierto);
            tv_precioConcierto = itemView.findViewById(R.id.tv_precioConcierto);
            iv_concierto = itemView.findViewById(R.id.iv_concierto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(listaConciertos.get(position));
                    }
                }
            });
        }
    }

    public int obtenerIdImagen(int idImagen) {
        return context.getResources().getIdentifier("concierto_" + idImagen, "drawable", context.getPackageName());
    }
}