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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.model.Administrador;
import com.example.design_vicent_sprint1.model.AnunciosAdapter;
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
    private String rol;
    private ContactosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        edificioSeleccionado = getIntent().getStringExtra("edificio");
        rol = getIntent().getStringExtra("rol");
        obtenerContactosPorEdificio(edificioSeleccionado, rol);
    }

    private void obtenerContactosPorEdificio(String edificioId, String rol) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference contactosRef = db.collection("edificios").document(edificioId).collection("contactos");

        contactosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Contacto> contactos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String id = doc.getId();
                    String nombre = doc.getString("nombre");
                    String telefono = doc.getString("telefono");
                    contactos.add(new Contacto(id, nombre, telefono));
                }
                recyclerViewContactos.setLayoutManager(new LinearLayoutManager(this));
                adapter = new ContactosAdapter(contactos, this, rol, new ContactosAdapter.OnItemClickListener() {
                    @Override
                    public void onEliminarClick(Contacto contacto, int position) {
                        mostrarPopupBorrar(contacto.getId(), edificioSeleccionado, position);
                    }

                    @Override
                    public void onEditarClick(Contacto contacto) {

                    }
                });
                recyclerViewContactos.setAdapter(adapter);
            } else {
                Log.e("FirestoreError", "Error al obtener contactos", task.getException());
            }
        });
    }

    private void mostrarPopupBorrar(String id, String edificio, int position) {

        // Crear el segundo pop-up (Dialog)
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_eliminar_contacto);  // Asegúrate de que este layout existe
        popup.setCanceledOnTouchOutside(true);
        Button cancelar = popup.findViewById(R.id.btn_cancelar);
        Button confimar = popup.findViewById(R.id.btn_confirmar);

        // Hacer el fondo del segundo pop-up transparente
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        confimar.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("edificios")
                    .document(edificio)
                    .collection("contactos")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Contacto","Contacto eliminado con éxito.");
                        adapter.removeItem(position);
                    })
                    .addOnFailureListener(e -> System.err.println("Error al eliminar el contacto: " + e.getMessage()));
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

