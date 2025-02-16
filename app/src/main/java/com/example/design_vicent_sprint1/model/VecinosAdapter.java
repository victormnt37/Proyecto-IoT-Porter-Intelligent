package com.example.design_vicent_sprint1.model;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.design_vicent_sprint1.R;

import java.util.List;
import com.example.design_vicent_sprint1.R;


public class VecinosAdapter extends RecyclerView.Adapter<VecinosAdapter.VecinoViewHolder> {

    public interface OnItemClickListener {
        void onEliminarClick(Vecino vecino, int position);
        void onContactarClick(Vecino vecino);
    }

    private List<Vecino> vecinos;
    private ImageLoader lectorImagenes;
    private OnItemClickListener listener;

    // Constructor que recibe la lista de vecinos y el lector de imágenes
    public VecinosAdapter(List<Vecino> vecinos, OnItemClickListener listener) {
        this.vecinos = vecinos;
        this.lectorImagenes = lectorImagenes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VecinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_vecino, parent, false);
        return new VecinoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VecinoViewHolder holder, int position) {
        Vecino vecino = vecinos.get(position);

        // Asignar el nombre y correo
        //holder.nombre.setText(vecino.getEdificio().getNombre());
        holder.correo.setText(vecino.getCorreo());

        // Cargar la imagen usando NetworkImageView
        Uri urlImagen = vecino.getPhotoUrl();
        if (urlImagen != null) {
            holder.imagen.setImageUrl(urlImagen.toString(), lectorImagenes);
        } else {
            holder.imagen.setDefaultImageResId(R.drawable.icon_perfil); // Imagen predeterminada
        }

        // Evento de clic para el menú de opciones
        holder.menuOpciones.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuOpciones);
            popupMenu.inflate(R.menu.menu_opciones_vecino); // tu menú XML

            // Configurar las acciones de cada opción del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.opcion_eliminar){
                    listener.onEliminarClick(vecino, position);
                    return true;
                }else if (item.getItemId() == R.id.opcion_contactar){
                    listener.onContactarClick(vecino);
                    return true;
                }else{
                    return false;
                }
            });
            popupMenu.show(); // Mostrar el menú
        });
    }


    @Override
    public int getItemCount() {
        return vecinos.size();
    }

    // Clase ViewHolder para los elementos del RecyclerView
    static class VecinoViewHolder extends RecyclerView.ViewHolder {

        TextView /*nombre,*/ correo;
        NetworkImageView imagen; // NetworkImageView para cargar imágenes desde URL
        ImageView menuOpciones;

        public VecinoViewHolder(@NonNull View itemView) {
            super(itemView);

            //nombre = itemView.findViewById(R.id.nombreVecino);
            correo = itemView.findViewById(R.id.correoVecino);
            imagen = itemView.findViewById(R.id.imagen);
            menuOpciones = itemView.findViewById(R.id.menuOpciones);
        }
    }

    public void removeItem(int position) {
        vecinos.remove(position);
        notifyItemRemoved(position);
    }
}
