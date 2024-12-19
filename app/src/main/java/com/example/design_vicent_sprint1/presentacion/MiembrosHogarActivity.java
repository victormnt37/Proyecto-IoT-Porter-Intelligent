package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioContactos;
import com.example.design_vicent_sprint1.model.Contacto;
import com.example.design_vicent_sprint1.model.ContactosAdapter;
import com.example.design_vicent_sprint1.model.MiembrosHogarAdapter;
import com.example.design_vicent_sprint1.model.Vecino;
import com.example.design_vicent_sprint1.model.VecinosAdapter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MiembrosHogarActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVecinos;
    private String edificioSeleccionado;
    private String userId;
    private MiembrosHogarAdapter adapter;
    private List<Vecino> vecinos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros_hogar);

        recyclerViewVecinos = findViewById(R.id.recyclerViewVecinos);
        edificioSeleccionado = getIntent().getStringExtra("edificio");
        userId = getIntent().getStringExtra("userId");
        obtenerVecinosMismoPisoYPuerta(edificioSeleccionado, userId);
    }

    private void obtenerVecinosMismoPisoYPuerta(String edificioId, String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference vecinoDoc = db.collection("edificios").document(edificioId).collection("vecinos").document(userId);

        vecinoDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Vecino vecino = new Vecino();
                vecino.setCorreo(task.getResult().getId());
                vecino.setPiso(task.getResult().getString("piso"));
                vecino.setPuerta(task.getResult().getString("puerta"));
                if (vecino != null) {
                    String piso = vecino.getPiso();
                    String puerta = vecino.getPuerta();
                    db.collection("edificios").document(edificioId).collection("vecinos")
                            .whereEqualTo("piso", piso)
                            .whereEqualTo("puerta", puerta)
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    vecinos = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc : task1.getResult()) {
                                        if (!doc.getId().equals(userId)) {
                                            Vecino vecinoItem = new Vecino();
                                            vecinoItem.setCorreo(doc.getId());
                                            vecinoItem.setPiso(doc.getString("piso"));
                                            vecinoItem.setPuerta(doc.getString("puerta"));
                                            vecinos.add(vecinoItem);
                                        }
                                        recyclerViewVecinos.setLayoutManager(new LinearLayoutManager(this));
                                        adapter = new MiembrosHogarAdapter(vecinos);
                                        recyclerViewVecinos.setAdapter(adapter);
                                    }
                                } else {
                                    Log.e("FirestoreError", "Error al buscar vecinos del mismo piso y puerta", task1.getException());
                                }
                            });
                }
            } else {
                Log.e("FirestoreError", "Error al obtener el vecino", task.getException());
            }
        });
    }


}
