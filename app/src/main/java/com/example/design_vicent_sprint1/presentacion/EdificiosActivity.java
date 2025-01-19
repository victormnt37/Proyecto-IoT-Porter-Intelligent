package com.example.design_vicent_sprint1.presentacion;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
                        public void onDeleteClick(int position, Edificio edificio) {
                            String id = edificio.getId();
                            String rol = lista_edificios_y_roles.get(id);
                            mostrarPopupDesvincular(userId, id, rol);
                        }
                    });
                    recyclerViewEdificios.setAdapter(adapter);
                });
            }
        });
    }

    private void mostrarPopupDesvincular(String correo, String edificio, String rol) {

        // Crear el segundo pop-up (Dialog)
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_desvincular_edificio);  // Asegúrate de que este layout existe
        popup.setCanceledOnTouchOutside(true);
        Button cancelar = popup.findViewById(R.id.btn_cancelar);
        Button confimar = popup.findViewById(R.id.btn_confirmar);

        // Hacer el fondo del segundo pop-up transparente
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        confimar.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .document(correo)
                    .collection("edificios")
                    .document(edificio)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("desvincular","Edificio eliminado de la subcolección del usuario.");

                        String subcollection = rol.equals("admin") ? "administradores" : "vecinos";
                        db.collection("edificios")
                                .document(edificio)
                                .collection(subcollection)
                                .document(correo)
                                .delete()
                                .addOnSuccessListener(aVoid1 -> {
                                    Log.d("desvincular","Usuario eliminado de la subcolección del edificio.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("desvincular","Error al eliminar el usuario de la subcolección del edificio: " + e.getMessage());
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("desvincular","Error al eliminar el edificio de la subcolección del usuario: " + e.getMessage());
                    });
                popup.dismiss();
                Toast toast = Toast.makeText(this, "Edificio desvinculado", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
        });
        cancelar.setOnClickListener(view -> {
            popup.dismiss();
        });

        // Mostrar el segundo pop-up
        popup.show();
    }



    private void onEdificioSeleccionado(String id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("edificio", id);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
}
