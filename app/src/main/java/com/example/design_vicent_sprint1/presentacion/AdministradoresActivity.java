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
    private String rol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administradores);

        recyclerViewAdministradores = findViewById(R.id.recyclerViewAdministradores);
        edificioSeleccionado = getIntent().getStringExtra("edificio");
        rol = getIntent().getStringExtra("rol");
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
                administradoresAdapter = new AdministradoresAdapter(this, administradores, lectorImagenes,
                        new AdministradoresAdapter.OnItemClickListener() {
                            @Override
                            public void onEliminarClick(Administrador administrador, int position) {
                                mostrarPopupDesvincular(administrador.getCorreo(), edificioSeleccionado, position);
                            }

                            @Override
                            public void onEditarClick(Administrador administrador) {

                            }
                        },rol);
                recyclerViewAdministradores.setAdapter(administradoresAdapter);
            } else {
                Log.e("FirestoreError", "Error al obtener contactos", task.getException());
            }
        });
    }
    private void mostrarPopupDesvincular(String correo, String edificio, int position) {

        // Crear el segundo pop-up (Dialog)
        Dialog popup = new Dialog(this);
        popup.setContentView(R.layout.popup_eliminar_usuario);  // Asegúrate de que este layout existe
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

                        db.collection("edificios")
                                .document(edificio)
                                .collection("administradores")
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
            administradoresAdapter.removeItem(position);
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
