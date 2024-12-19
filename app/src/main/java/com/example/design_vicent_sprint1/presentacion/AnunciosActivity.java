package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.model.Anuncio;
import com.example.design_vicent_sprint1.model.AnunciosAdapter;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioAnuncios;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AnunciosActivity extends AppCompatActivity {

    private RepositorioAnuncios repositorioAnuncios;
    private RecyclerView recyclerViewAnuncios;
    private String edificioSeleccionado;
    private List<Anuncio> anuncios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        recyclerViewAnuncios = findViewById(R.id.recyclerViewAnuncios);
        edificioSeleccionado = getIntent().getStringExtra("edificio");
        obtenerAnunciosPorEdificio(edificioSeleccionado);

    }

    private void obtenerAnunciosPorEdificio(String edificioId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference anunciosRef = db.collection("edificios").document(edificioId).collection("anuncios");

        anunciosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                anuncios = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String asunto = doc.getString("asunto");
                    String texto = doc.getString("texto");
                    String autor = doc.getString("autor");
                    String fechaString = doc.getString("fecha");
                    Anuncio anuncio = new Anuncio(asunto, texto, autor, fechaString);
                    anuncios.add(anuncio);
                }
                anuncios.sort((a1, a2) -> {
                    if (a1.getFecha() == null || a2.getFecha() == null) {
                        return 0;
                    }
                    return a2.getFecha().compareTo(a1.getFecha());
                });
                recyclerViewAnuncios.setLayoutManager(new LinearLayoutManager(this));
                AnunciosAdapter adapter = new AnunciosAdapter(anuncios);
                recyclerViewAnuncios.setAdapter(adapter);
            } else {
                Log.e("FirestoreError", "Error al obtener anuncios", task.getException());
            }
        });
    }

}

