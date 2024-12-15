package com.example.design_vicent_sprint1.model;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> {

    private final List<Contacto> contactos;

    public ContactosAdapter(List<Contacto> contactos) {
        this.contactos = contactos;
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

        // Configurar el clic en el ítem para iniciar una llamada
        holder.itemView.setOnClickListener(v -> {
            String telefono = contacto.getTelefono();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telefono));
            if (v.getContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED) {
                v.getContext().startActivity(intent);
            } else {
                // Opcional: Muestra un mensaje si no tienes permisos
                Toast.makeText(v.getContext(), "Permiso para llamar no concedido", Toast.LENGTH_SHORT).show();
            }
        });


        // Si tienes una imagen asociada, usa un cargador como Glide o Picasso
        // Glide.with(holder.imgContacto.getContext()).load(contacto.getFotoUrl()).into(holder.imgContacto);
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtTelefono;
        TextView imgContacto;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.contactName);
            txtTelefono = itemView.findViewById(R.id.contactPhone);
            imgContacto = itemView.findViewById(R.id.contactDescription);
        }
    }
}
