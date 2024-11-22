package com.example.design_vicent_sprint1.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;

import java.util.List;

public class PuertasAdapter extends RecyclerView.Adapter<PuertasAdapter.PuertaViewHolder> {

    private List<Puerta> puertas;

    public PuertasAdapter(List<Puerta> puertas) {
        this.puertas = puertas;
    }

    @NonNull
    @Override
    public PuertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puerta, parent, false);
        return new PuertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuertaViewHolder holder, int position) {
        Puerta puerta = puertas.get(position);
        holder.nombrePuerta.setText(puerta.getNombre());
        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return puertas.size();
    }

    public static class PuertaViewHolder extends RecyclerView.ViewHolder {
        TextView nombrePuerta;
        public PuertaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePuerta = itemView.findViewById(R.id.nombrePuerta);
        }
    }
}

