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

public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.AnuncioViewHolder> {

    private List<Anuncio> anuncios;

    public AnunciosAdapter(List<Anuncio> anuncios) {
        this.anuncios = anuncios;
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
            Toast.makeText(holder.itemView.getContext(), "Opciones del anuncio: " + anuncio.getAsunto(), Toast.LENGTH_SHORT).show();
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
}
