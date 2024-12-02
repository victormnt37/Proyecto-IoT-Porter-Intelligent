package com.example.design_vicent_sprint1.data;

import android.app.Application;

import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.EdificiosFirestoreAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Aplicacion extends Application {
    public EdificiosAsinc edificios;
    public EdificiosFirestoreAdapter adapter;

    @Override public void onCreate() {
        super.onCreate();

        edificios = new EdificiosFirestore();

        Query query = FirebaseFirestore.getInstance()
                .collection("edificios");

        FirestoreRecyclerOptions<Edificio> opciones =new FirestoreRecyclerOptions
                .Builder<Edificio>().setQuery(query, Edificio.class).build();

        //llena el recycler con los lugares
        adapter = new EdificiosFirestoreAdapter(opciones, this);

    }
}