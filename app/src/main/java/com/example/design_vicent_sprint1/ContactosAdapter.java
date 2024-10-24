package com.example.design_vicent_sprint1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ViewHolder> {
    private List<Contacto> contactos;

    public ContactosAdapter(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacto contacto = contactos.get(position);
        holder.textView1.setText(contacto.getNombre());
        holder.textView2.setText(contacto.getTelefono());
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(android.R.id.text1);
            textView2 = itemView.findViewById(android.R.id.text2);
        }
    }
}

