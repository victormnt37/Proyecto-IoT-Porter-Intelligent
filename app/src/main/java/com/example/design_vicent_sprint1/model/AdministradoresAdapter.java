package com.example.design_vicent_sprint1.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.design_vicent_sprint1.R;

import java.util.List;

public class AdministradoresAdapter extends RecyclerView.Adapter<AdministradoresAdapter.AdministradorViewHolder> {

    public interface OnItemClickListener {
        void onEliminarClick(Administrador administrador, int position);
        void onEditarClick(Administrador administrador);
    }

    private List<Administrador> administradores;
    private ImageLoader lectorImagenes;
    private OnItemClickListener listener;
    private String rol;
    private String userId;
    private final Activity actividad;
    public static final int REQUEST_CALL_PERMISSION = 100;

    // Constructor que recibe la lista de administradores y el lector de imágenes
    public AdministradoresAdapter(Activity activity, List<Administrador> administradores, ImageLoader lectorImagenes, OnItemClickListener listener, String rol, String user_id) {
        this.actividad = activity;
        this.administradores = administradores;
        this.lectorImagenes = lectorImagenes;
        this.listener = listener;
        this.rol = rol;
        this.userId = user_id;
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
        holder.telefono.setText(administrador.getTelefono());

        holder.itemView.setOnClickListener(v -> {
            String telefono = administrador.getTelefono();
            if (ContextCompat.checkSelfPermission(actividad, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                realizarLlamada(telefono); // Llamar directamente si hay permisos
            } else {
                solicitarPermisoLlamada(); // Solicitar permisos si no están concedidos
            }
        });

        // Cargar la imagen usando NetworkImageView
        Uri urlImagen = administrador.getPhotoUrl();
        if (urlImagen != null) {
            holder.imagen.setImageUrl(urlImagen.toString(), lectorImagenes);
        } else {
            holder.imagen.setDefaultImageResId(R.drawable.icon_perfil); // Imagen predeterminada
        }



        // Evento de clic para el menú de opciones
        if(rol.equals("vecino")){
            holder.menuOpciones.setVisibility(View.GONE);
        }else{

            holder.menuOpciones.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuOpciones);

                if(administrador.getCorreo().equals(userId)){
                    popupMenu.inflate(R.menu.menu_opciones_editar); // Inflar el menú XML específico para administradores
                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.opcion_editar) {
                            listener.onEditarClick(administrador);
                            return true;
                        }
                        return false;
                    });
                }else{
                    popupMenu.inflate(R.menu.menu_opciones_admin); // Inflar el menú XML específico para administradores
                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.opcion_editar) {
                            listener.onEditarClick(administrador);
                            return true;
                        } else if (item.getItemId() == R.id.opcion_eliminar) {
                            listener.onEliminarClick(administrador, position);
                            return true;
                        }
                        return false;
                    });
                }



                popupMenu.show(); // Mostrar el menú
            });
        }

    }

    private void realizarLlamada(String telefono) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + telefono));
        actividad.startActivity(intent); // Solo se ejecutará si los permisos ya están concedidos
    }

    private void solicitarPermisoLlamada() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, android.Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(actividad)
                    .setTitle("Permiso necesario")
                    .setMessage("Necesitamos permiso para realizar llamadas desde esta aplicación.")
                    .setPositiveButton("Conceder", (dialog, which) ->
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION))
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        Toast.makeText(actividad, "Permiso de llamada no concedido", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } else {
            // Solicitar el permiso directamente si no se necesita explicación adicional
            ActivityCompat.requestPermissions(actividad,
                    new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

    @Override
    public int getItemCount() {
        return administradores.size();
    }

    // Clase ViewHolder para los elementos del RecyclerView
    static class AdministradorViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, correo, telefono;
        NetworkImageView imagen; // NetworkImageView para cargar imágenes desde URL
        ImageView menuOpciones;

        public AdministradorViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreAdministrador);
            correo = itemView.findViewById(R.id.correoAdministrador);
            telefono = itemView.findViewById(R.id.telefonoAdministrador);
            imagen = itemView.findViewById(R.id.imagen);
            menuOpciones = itemView.findViewById(R.id.menuOpciones);
        }
    }

    public void removeItem(int position) {
        administradores.remove(position);
        notifyItemRemoved(position);
    }
}
