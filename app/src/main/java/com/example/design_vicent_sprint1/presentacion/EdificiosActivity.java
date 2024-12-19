package com.example.design_vicent_sprint1.presentacion;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.Edificios;
import com.example.design_vicent_sprint1.data.RepositorioEdificios;
import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.EdificioMenuAdapter;
import com.example.design_vicent_sprint1.model.EdificiosAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EdificiosActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerViewEdificios;
    private String userId;
    private String id_edificioSeleccionado;
    private List<Edificio> listaEdificios = new ArrayList<>();
    private Set<String> listaIds;
    private Map<String,String> lista_edificios_y_roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edificios);
        Bundle extras = getIntent().getExtras();
        userId = extras.getString("userId");
        id_edificioSeleccionado = extras.getString("edificio");
        recyclerViewEdificios = findViewById(R.id.recyclerViewEdificios);

       cargarEdificos(userId);
    }

    private void cargarEdificos(String userId){

        CollectionReference edificios_con_permiso = FirebaseFirestore.getInstance().
                collection("usuarios").document(userId).collection("edificios");

        edificios_con_permiso.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                lista_edificios_y_roles = new HashMap<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    lista_edificios_y_roles.put(document.getId(), document.getString("rol"));
                }

                Set<String> listaIds = lista_edificios_y_roles.keySet();
                CollectionReference edificios = FirebaseFirestore.getInstance().collection("edificios");
                List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                for (String id : listaIds) {
                    Task<DocumentSnapshot> t = edificios.document(id).get();
                    tasks.add(t);
                    t.addOnCompleteListener(doc -> {
                        if (doc.isSuccessful() && doc.getResult() != null) {
                            DocumentSnapshot documentSnapshot = doc.getResult();
                            Edificio edificio = documentSnapshot.toObject(Edificio.class);
                            Log.d("firestore", ""+edificio.getId());
                            if(!listaEdificios.contains(edificio)){
                                listaEdificios.add(edificio);
                            }
                        } else {
                            Log.e("FirestoreError", "Error al obtener el documento con ID: " + id, t.getException());
                        }
                    });
                }
                Tasks.whenAllComplete(tasks).addOnCompleteListener(task2 -> {
                    recyclerViewEdificios.setLayoutManager(new GridLayoutManager(this, 1));

                    EdificiosAdapter adapter = new EdificiosAdapter(listaEdificios, new EdificiosAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Edificio edificio) {
                            String id = edificio.getId();
                            onEdificioSeleccionado(id); // Manejar selección
                        }

                        @Override
                        public void onDeleteClick(int position) {
                            // Eliminar el edificio de la lista
                            listaEdificios.remove(position);
                            recyclerViewEdificios.getAdapter().notifyItemRemoved(position);
                        }
                    });
                    recyclerViewEdificios.setAdapter(adapter);
                });
            }
        });
    }

    private void onEdificioSeleccionado(String id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("edificio", id);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
}
