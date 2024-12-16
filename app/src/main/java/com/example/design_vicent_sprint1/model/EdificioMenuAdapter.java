package com.example.design_vicent_sprint1.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.design_vicent_sprint1.R;
import java.util.List;

public class EdificioMenuAdapter extends RecyclerView.Adapter<EdificioMenuAdapter.ViewHolder> {
    private List<Edificio> listaEdificios;
    private OnEdificioSeleccionado listener;

    public interface OnEdificioSeleccionado {
        void onSeleccionar(Edificio edificio);
    }

    public EdificioMenuAdapter(List<Edificio> listaEdificios, OnEdificioSeleccionado listener) {
        this.listaEdificios = listaEdificios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para el ítem del menú
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_edificios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Edificio edificio = listaEdificios.get(position);

        // Asignar valores a las vistas del ítem
        holder.nombreEdificio.setText(edificio.getNombre());
        holder.itemView.setOnClickListener(v -> listener.onSeleccionar(edificio));

        // Personaliza si necesitas botones u otros elementos
        holder.imagenEdificio.setOnClickListener(v -> listener.onSeleccionar(edificio));
    }

    @Override
    public int getItemCount() {
        return listaEdificios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreEdificio;
        ImageView imagenEdificio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEdificio = itemView.findViewById(R.id.nombreEdificio);
            imagenEdificio = itemView.findViewById(R.id.iconoEdificio);
        }
    }
}
