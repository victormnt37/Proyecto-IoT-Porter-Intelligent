package com.example.design_vicent_sprint1.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.design_vicent_sprint1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EdificiosFirestoreAdapter extends FirestoreRecyclerAdapter<Edificio, EdificiosAdapter.ViewHolder> {

    protected View.OnClickListener onClickListener;
    protected Context context;

    public EdificiosFirestoreAdapter(@NonNull FirestoreRecyclerOptions<Edificio> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    public EdificiosAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        view.setOnClickListener(onClickListener);
        return new EdificiosAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull EdificiosAdapter
            .ViewHolder holder, int position, @NonNull Edificio edificio) {
        holder.textView.setText(edificio.getNombre());
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
