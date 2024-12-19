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

public class AdministradoresAdapter extends RecyclerView.Adapter<AdministradoresAdapter.AdministradorViewHolder> {

    private List<Administrador> administradores;
    private ImageLoader lectorImagenes;

    // Constructor que recibe la lista de administradores y el lector de imágenes
    public AdministradoresAdapter(List<Administrador> administradores, ImageLoader lectorImagenes) {
        this.administradores = administradores;
        this.lectorImagenes = lectorImagenes;
    }

    @NonNull
    @Override
    public AdministradoresAdapter.AdministradorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_administrador, parent, false);
        return new AdministradoresAdapter.AdministradorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdministradoresAdapter.AdministradorViewHolder holder, int position) {
        Administrador administrador = administradores.get(position);

        // Asignar el nombre y correo
        holder.nombre.setText(administrador.getNombre());
        holder.correo.setText(administrador.getCorreo());

        // Cargar la imagen usando NetworkImageView
        Uri urlImagen = administrador.getPhotoUrl();
        if (urlImagen != null) {
            holder.imagen.setImageUrl(urlImagen.toString(), lectorImagenes);
        } else {
            holder.imagen.setDefaultImageResId(R.drawable.icon_perfil); // Imagen predeterminada
        }



        // Evento de clic para el menú de opciones
        holder.menuOpciones.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuOpciones);
            popupMenu.inflate(R.menu.menu_opciones_vecino); // Inflar el menú XML específico para administradores

            // Configurar las acciones de cada opción del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.opcion_editar) {
                    Toast.makeText(holder.itemView.getContext(),
                            "Editar: " + administrador.getNombre(), Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.opcion_eliminar) {
                    Toast.makeText(holder.itemView.getContext(),
                            "Eliminar: " + administrador.getNombre(), Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.opcion_info) {
                    Toast.makeText(holder.itemView.getContext(),
                            "Información de: " + administrador.getNombre(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popupMenu.show(); // Mostrar el menú
        });
    }

    @Override
    public int getItemCount() {
        return administradores.size();
    }

    // Clase ViewHolder para los elementos del RecyclerView
    static class AdministradorViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, correo;
        NetworkImageView imagen; // NetworkImageView para cargar imágenes desde URL
        ImageView menuOpciones;

        public AdministradorViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreAdministrador);
            correo = itemView.findViewById(R.id.correoAdministrador);
            imagen = itemView.findViewById(R.id.imagen);
            menuOpciones = itemView.findViewById(R.id.menuOpciones);
        }
    }
}
