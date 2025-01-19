package com.example.design_vicent_sprint1.presentacion;

import static android.content.Intent.getIntent;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioVecinos;
import com.example.design_vicent_sprint1.model.Vecino;
import com.example.design_vicent_sprint1.model.VecinosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VecinosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVecinos;
    private Spinner spinnerPisos;
    private String edificioSeleccionado;
    private VecinosAdapter adapter;
    private List<Vecino> vecinos; // Lista completa de vecinos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vecinos);

        recyclerViewVecinos = findViewById(R.id.recyclerViewVecinos);
        spinnerPisos = findViewById(R.id.spinnerPiso);
        edificioSeleccionado = getIntent().getStringExtra("edificio");
        obtenerVecinosPorEdificio(edificioSeleccionado);

    }

    private void obtenerVecinosPorEdificio(String edificioId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference vecinosRef = db.collection("edificios").document(edificioId).collection("vecinos");

        vecinosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                vecinos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Vecino vecino = new Vecino();
                    vecino.setCorreo(doc.getId());
                    vecino.setPiso(doc.getString("piso"));
                    vecino.setPuerta(doc.getString("puerta"));
                    vecinos.add(vecino);
                }
                recyclerViewVecinos.setLayoutManager(new LinearLayoutManager(this));
                adapter = new VecinosAdapter(vecinos,
                        new VecinosAdapter.OnItemClickListener() {
                    @Override
                    public void onEliminarClick(Vecino vecino, int position) {
                        mostrarPopupDesvincular(vecino.getCorreo(), edificioSeleccionado, position);
                    }
                });
                recyclerViewVecinos.setAdapter(adapter);
                configurarSpinner();
            } else {
                Log.e("FirestoreError", "Error al obtener vecinos del edificio", task.getException());
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
                                .collection("vecinos")
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
            adapter.removeItem(position);
            popup.dismiss();
            Toast toast = Toast.makeText(this, "Vecino eliminado", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });
        cancelar.setOnClickListener(view -> {
            popup.dismiss();
        });

        // Mostrar el segundo pop-up
        popup.show();
    }

    private void configurarSpinner() {
        // Obtener los pisos únicos del edificio seleccionado
        List<String> pisos = obtenerPisosUnicos(vecinos);

        // Configurar el adaptador del Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, pisos);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPisos.setAdapter(spinnerAdapter);

        // Configurar listener para detectar cambios en la selección
        spinnerPisos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pisoSeleccionado = (String) parent.getItemAtPosition(position);
                filtrarVecinosPorPiso(pisoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });
    }

    private List<String> obtenerPisosUnicos(List<Vecino> vecinos) {
        List<String> pisos = new ArrayList<>();
        for (Vecino vecino : vecinos) {
            if (!pisos.contains(vecino.getPiso())) {
                pisos.add(vecino.getPiso());
            }
        }
        return pisos;
    }

    private void filtrarVecinosPorPiso(String piso) {
        List<Vecino> vecinosFiltrados = new ArrayList<>();
        for (Vecino vecino : vecinos) {
            if (vecino.getPiso().equals(piso)) {
                vecinosFiltrados.add(vecino);
            }
        }
        adapter = new VecinosAdapter(vecinosFiltrados,
                new VecinosAdapter.OnItemClickListener() {

                    @Override
                    public void onEliminarClick(Vecino vecino, int position) {
                        mostrarPopupDesvincular(vecino.getCorreo(), edificioSeleccionado, position);
                    }
                });
        recyclerViewVecinos.setAdapter(adapter);
    }
}
