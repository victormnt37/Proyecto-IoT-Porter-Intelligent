package com.example.design_vicent_sprint1.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.design_vicent_sprint1.R;
import java.util.List;

public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.AnuncioViewHolder> {

    public interface OnItemClickListener {
        void onEliminarClick(Anuncio anuncio, int position);
        void onEditarClick(Anuncio anuncio);
        void onCompartirClick(Anuncio anuncio);
    }

    private List<Anuncio> anuncios;
    private OnItemClickListener listener;

    public AnunciosAdapter(List<Anuncio> anuncios, OnItemClickListener listener) {
        this.anuncios = anuncios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_anuncio, parent, false);
        return new AnuncioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get(position);

        holder.hora.setText(anuncio.getFecha());
        holder.titulo.setText(anuncio.getAsunto());
        holder.descripcion.setText(anuncio.getTexto());

        holder.menuOpciones.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuOpciones);
            popupMenu.inflate(R.menu.menu_opciones_admin); // Inflar el menú XML específico para administradores

            // Configurar las acciones de cada opción del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.opcion_editar) {
                    listener.onEditarClick(anuncio);
                    return true;
                } else if (item.getItemId() == R.id.opcion_eliminar) {
                    listener.onEliminarClick(anuncio, position);
                    return true;
                }else if (item.getItemId() == R.id.opcion_compartir) {
                    listener.onCompartirClick(anuncio);
                    return true;
                }
                return false;
            });
            popupMenu.show(); // Mostrar el menú
        });
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        TextView hora, titulo, descripcion;
        ImageView menuOpciones;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);

            hora = itemView.findViewById(R.id.horaAnuncio);
            titulo = itemView.findViewById(R.id.tituloAnuncio);
            descripcion = itemView.findViewById(R.id.descripcionAnuncio);
            menuOpciones = itemView.findViewById(R.id.menuOpciones);
        }
    }

    public void removeItem(int position) {
        anuncios.remove(position);
        notifyItemRemoved(position);
    }
}
