package com.example.design_vicent_sprint1.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.design_vicent_sprint1.R;
import java.util.List;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder> {

    private List<Notificacion> notificaciones;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Notificacion notificacion);
    }

    public NotificacionesAdapter(List<Notificacion> notificaciones, OnItemClickListener listener) {
        this.notificaciones = notificaciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        try {
            Notificacion notificacion = notificaciones.get(position);

            switch (notificacion.getTipo()) {
                case "incendio":
                    holder.iconoNotificacion.setImageResource(R.drawable.icon_alerta);
                    break;
                case "emergencia":
                    holder.iconoNotificacion.setImageResource(R.drawable.icon_alerta);
                    break;
                case "robo":
                    holder.iconoNotificacion.setImageResource(R.drawable.icon_alerta);
                    break;
                case "anuncios":
                    holder.iconoNotificacion.setImageResource(R.drawable.icon_anuncio);
                    break;
                case "Sensor":
                    holder.iconoNotificacion.setImageResource(R.drawable.icon_sensor);
                    break;
                default:
                    holder.iconoNotificacion.setImageResource(R.drawable.icon_notificaciones);
                    break;
            }
            holder.itemView.setOnClickListener(v -> listener.onItemClick(notificacion));
            holder.textoNotificacion.setText(notificacion.getTexto());
            holder.fechaNotificacion.setText(notificacion.getFechaS());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView textoNotificacion;
        TextView fechaNotificacion;
        ImageView iconoNotificacion;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textoNotificacion = itemView.findViewById(R.id.textoNotificacion);
            fechaNotificacion = itemView.findViewById(R.id.fechaNotificacion);
            iconoNotificacion = itemView.findViewById(R.id.iconoNotificacion);
        }
    }
}
