package com.example.design_vicent_sprint1.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.design_vicent_sprint1.R;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> {

    private final List<Contacto> contactos;
    private final Activity actividad;
    public static final int REQUEST_CALL_PERMISSION = 100;
    private String rol;

    public ContactosAdapter(List<Contacto> contactos, Activity actividad, String rol) {
        this.contactos = contactos;
        this.actividad = actividad;
        this.rol = rol;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contactos, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {

        Contacto contacto = contactos.get(position);
        holder.txtNombre.setText(contacto.getNombre());
        holder.txtTelefono.setText(contacto.getTelefono());

        // Configurar el clic en el ítem para realizar una llamada
        holder.itemView.setOnClickListener(v -> {
            String telefono = contacto.getTelefono();
            if (ContextCompat.checkSelfPermission(actividad, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                realizarLlamada(telefono); // Llamar directamente si hay permisos
            } else {
                solicitarPermisoLlamada(); // Solicitar permisos si no están concedidos
            }
        });
        if(rol.equals("vecino")){
            holder.imageView.setVisibility(View.GONE);
        }else{
            holder.imageView.setOnClickListener(view -> {

            });
        }

        // Evento de clic para el menú de opciones
        holder.menuOpciones.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.menuOpciones);
            popupMenu.inflate(R.menu.menu_opciones_contacto); // Inflar el menú XML

            // Configurar las acciones de cada opción del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.opcion_editar) {

                    return true;
                } else if (item.getItemId() == R.id.opcion_eliminar) {

                    return true;
                } else if (item.getItemId() == R.id.opcion_info) {

                    return true;
                }
                return false;
            });
            popupMenu.show(); // Mostrar el menú
        });
    }

    @Override
    public int getItemCount() {
        return contactos.size();
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

    static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtTelefono;
        ImageView imageView;
        ImageView menuOpciones;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.contactName);
            txtTelefono = itemView.findViewById(R.id.contactPhone);
            imageView = itemView.findViewById(R.id.menuOpciones);
            menuOpciones = itemView.findViewById(R.id.menuOpciones);
        }
    }
}
