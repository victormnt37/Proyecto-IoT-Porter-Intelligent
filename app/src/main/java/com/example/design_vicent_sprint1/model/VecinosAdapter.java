package com.example.design_vicent_sprint1.model;

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

public class VecinosAdapter extends RecyclerView.Adapter<VecinosAdapter.VecinoViewHolder> {

    private List<Vecino> vecinos;

    public VecinosAdapter(List<Vecino> vecinos) {
        this.vecinos = vecinos;
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

        holder.nombre.setText(vecino.getPiso());
        holder.correo.setText(vecino.getCorreoElectronico());
        //holder.imagen.setImageResource(vecino.getImagenResId());

        holder.menuOpciones.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Opciones para: " + vecino.getPiso(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return vecinos.size();
    }

    static class VecinoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, correo;
        ImageView imagen, menuOpciones;

        public VecinoViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreVecino);
            correo = itemView.findViewById(R.id.correoVecino);
            imagen = itemView.findViewById(R.id.imagenVecino);
            menuOpciones = itemView.findViewById(R.id.menuOpciones);
        }
    }
}
