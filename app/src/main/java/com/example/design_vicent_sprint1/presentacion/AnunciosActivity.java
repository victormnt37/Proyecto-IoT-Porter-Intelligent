package com.example.design_vicent_sprint1.presentacion;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

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
    private AnunciosAdapter adapter;

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
                    String id = doc.getId();
                    String asunto = doc.getString("asunto");
                    String texto = doc.getString("texto");
                    String autor = doc.getString("autor");
                    String fechaString = doc.getString("fecha");
                    Anuncio anuncio = new Anuncio(id, asunto, texto, autor, fechaString);
                    anuncios.add(anuncio);
                }
                anuncios.sort((a1, a2) -> {
                    if (a1.getFecha() == null || a2.getFecha() == null) {
                        return 0;
                    }
                    return a2.getFecha().compareTo(a1.getFecha());
                });
                recyclerViewAnuncios.setLayoutManager(new LinearLayoutManager(this));
                adapter = new AnunciosAdapter(anuncios, new AnunciosAdapter.OnItemClickListener() {
                    @Override
                    public void onEliminarClick(Anuncio anuncio, int position) {
                        mostrarPopupBorrar(anuncio.getId(), edificioSeleccionado, position);
                    }

                    @Override
                    public void onEditarClick(Anuncio anuncio) {

                    }
                });
                recyclerViewAnuncios.setAdapter(adapter);
            } else {
                Log.e("FirestoreError", "Error al obtener anuncios", task.getException());
            }
        });
    }
    private void mostrarPopupBorrar(String id, String edificio, int position) {

        // Crear el segundo pop-up (Dialog)
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_eliminar_anuncio);  // Asegúrate de que este layout existe
        popup.setCanceledOnTouchOutside(true);
        Button cancelar = popup.findViewById(R.id.btn_cancelar);
        Button confimar = popup.findViewById(R.id.btn_confirmar);

        // Hacer el fondo del segundo pop-up transparente
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        confimar.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("edificios")
                    .document(edificio)
                    .collection("anuncios")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Anuncio","Anuncio eliminado con éxito.");
                        adapter.removeItem(position);
                    })
                    .addOnFailureListener(e -> System.err.println("Error al eliminar el anuncio: " + e.getMessage()));
            popup.dismiss();
            Toast toast = Toast.makeText(this, "Administrador eliminado", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });
        cancelar.setOnClickListener(view -> {
            popup.dismiss();
        });

        // Mostrar el segundo pop-up
        popup.show();
    }

}

