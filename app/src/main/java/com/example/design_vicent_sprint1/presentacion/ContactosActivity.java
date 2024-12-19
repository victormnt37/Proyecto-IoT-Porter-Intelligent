package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.model.Contacto;
import com.example.design_vicent_sprint1.model.ContactosAdapter;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioContactos;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactosActivity extends AppCompatActivity {

    private  List<Contacto> contactos;
    private RecyclerView recyclerViewContactos;
    private String edificioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        edificioSeleccionado = getIntent().getStringExtra("edificio");
        obtenerContactosPorEdificio(edificioSeleccionado);
    }

    private void obtenerContactosPorEdificio(String edificioId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contactosRef = db.collection("edificios").document(edificioId).collection("contactos");

        contactosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Contacto> contactos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    contactos.add(doc.toObject(Contacto.class));
                }
                recyclerViewContactos.setLayoutManager(new LinearLayoutManager(this));
                ContactosAdapter adapter = new ContactosAdapter(contactos, this);
                recyclerViewContactos.setAdapter(adapter);
            } else {
                Log.e("FirestoreError", "Error al obtener contactos", task.getException());
            }
        });
    }
}

