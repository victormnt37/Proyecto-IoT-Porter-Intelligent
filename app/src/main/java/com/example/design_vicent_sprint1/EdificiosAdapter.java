package com.example.design_vicent_sprint1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EdificiosAdapter extends RecyclerView.Adapter<EdificiosAdapter.ViewHolder> {
    private List<Edificio> edificios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Edificio edificio);
    }

    public EdificiosAdapter(List<Edificio> edificios, OnItemClickListener listener) {
        this.edificios = edificios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Edificio edificio = edificios.get(position);
        holder.textView.setText(edificio.getNombre());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(edificio));
    }

    @Override
    public int getItemCount() {
        return edificios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}

