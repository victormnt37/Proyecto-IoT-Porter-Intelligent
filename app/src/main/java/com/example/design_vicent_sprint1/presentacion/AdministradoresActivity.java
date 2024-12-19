package com.example.design_vicent_sprint1.presentacion;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.model.Administrador;
import com.example.design_vicent_sprint1.model.AdministradoresAdapter;
import com.example.design_vicent_sprint1.model.Contacto;
import com.example.design_vicent_sprint1.model.ContactosAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdministradoresActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAdministradores;
    private AdministradoresAdapter administradoresAdapter;
    private String edificioSeleccionado;
    private ImageLoader lectorImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administradores);

        recyclerViewAdministradores = findViewById(R.id.recyclerViewAdministradores);
        edificioSeleccionado = getIntent().getStringExtra("edificio");

        // Configuración del RecyclerView
        recyclerViewAdministradores.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el lector de imágenes
        //lectorImagenes = new ImageLoader(Volley.newRequestQueue(this), new LruBitmapCache());

        obtenerAdministradoresPorEdificio(edificioSeleccionado);
    }

    // Método para cargar datos de ejemplo de administradores
    private List<Administrador> cargarAdministradores() {
        List<Administrador> administradores = new ArrayList<>();
        administradores.add(new Administrador("admin1@example.com", "Administrador 1", "123456789"));
        administradores.add(new Administrador("admin2@example.com", "Administrador 2", "987654321"));
        administradores.add(new Administrador("admin3@example.com", "Administrador 3", "567890123"));
        return administradores;
    }

    private void obtenerAdministradoresPorEdificio(String edificioId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference adminsRef = db.collection("edificios").document(edificioId).collection("administradores");

        adminsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Administrador> administradores = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Administrador administrador = new Administrador();
                    administrador.setCorreo(doc.getId());
                    administrador.setNombre(doc.getString("nombre"));
                    administrador.setTelefono(doc.getString("telefono"));
                    administradores.add(administrador);
                }
                recyclerViewAdministradores.setLayoutManager(new LinearLayoutManager(this));
                administradoresAdapter = new AdministradoresAdapter(administradores, lectorImagenes);
                recyclerViewAdministradores.setAdapter(administradoresAdapter);
            } else {
                Log.e("FirestoreError", "Error al obtener contactos", task.getException());
            }
        });
    }
}
