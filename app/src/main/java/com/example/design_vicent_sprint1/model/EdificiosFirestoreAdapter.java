package com.example.design_vicent_sprint1.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EdificiosFirestoreAdapter extends FirestoreRecyclerAdapter<Edificio, EdificiosFirestoreAdapter.ViewHolder> {

    protected View.OnClickListener onClickListener;
    protected Context context;

    public EdificiosFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Edificio> options, Context context) {
        super(options);
        this.context = context;
    }

    // ViewHolder personalizado para FirestoreRecyclerAdapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1); // Actualiza si usas otro diseño
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_edificios, parent, false); // Cambiar aquí Reemplaza con el diseño correcto si es necesario
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Edificio edificio) {
        holder.textView.setText(edificio.getNombre()); // Asigna los datos al TextView
    }

    public void setOnItemClickListener(View.OnClickListener onClick) {
        onClickListener = onClick;
    }

    public String getKey(int pos) {
        return super.getSnapshots().getSnapshot(pos).getId();
    }

    public int getPos(String id) {
        int pos = 0;
        while (pos < getItemCount()) {
            if (getKey(pos).equals(id)) return pos;
            pos++;
        }
        return -1;
    }
}
