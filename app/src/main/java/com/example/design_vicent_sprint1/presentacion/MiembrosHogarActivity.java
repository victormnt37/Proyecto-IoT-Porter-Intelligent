package com.example.design_vicent_sprint1.presentacion;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.model.MiembrosHogarAdapter;
import com.example.design_vicent_sprint1.model.Vecino;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiembrosHogarActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVecinos;
    private String edificioSeleccionado;
    private String userId;
    private MiembrosHogarAdapter adapter;
    private List<Vecino> vecinos;
    private String piso_hogar;
    private String puerta_hogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros_hogar);

        recyclerViewVecinos = findViewById(R.id.recyclerViewVecinos);
        edificioSeleccionado = getIntent().getStringExtra("edificio");
        userId = getIntent().getStringExtra("userId");
        FloatingActionButton btn_emergente = findViewById(R.id.btn_emergente);
        btn_emergente.setImageResource(R.drawable.icon_addfondo);
        btn_emergente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupAdd(v);
            }
        });
        obtenerVecinosMismoPisoYPuerta(edificioSeleccionado, userId);

    }

    private void mostrarPopupAdd(View v) {

        Dialog popupVecinos = new Dialog(this);
        popupVecinos.setContentView(R.layout.popup_anyadir_miembro_hogar);
        popupVecinos.setCanceledOnTouchOutside(true);

        // Hacer el fondo del segundo pop-up transparente
        popupVecinos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputLayout tilCorreo = popupVecinos.findViewById(R.id.tlCorreo);
        EditText correo = popupVecinos.findViewById(R.id.etCorreo);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button btnAdd = popupVecinos.findViewById(R.id.btn_confirmar);
        btnAdd.setOnClickListener(view -> {
            String correo_i = correo.getText().toString();

            if(!verificarCorreo(correo_i)){
                tilCorreo.setError("Correo no válido");
            } else{
                DocumentReference usuarioRef = db.collection("usuarios").document(correo_i);

                usuarioRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        usuarioRef.set(new HashMap<>())
                                .addOnSuccessListener(aVoid -> {
                                    usuarioRef.collection("edificios").document(edificioSeleccionado)
                                            .set(new HashMap<String, Object>() {{
                                                put("rol", "vecino");
                                            }});
                                });

                    } else {
                        usuarioRef.collection("edificios").document(edificioSeleccionado)
                                .set(new HashMap<String, Object>() {{
                                    put("rol", "vecino");
                                }});

                    }
                    DocumentReference edificioRef = db.collection("edificios").document(edificioSeleccionado);

                    edificioRef.get().addOnSuccessListener(documentSnapshot2 -> {
                        edificioRef.collection("vecinos").document(correo_i)
                                .set(new HashMap<String, Object>() {{
                                    put("piso", piso_hogar);
                                    put("puerta", puerta_hogar);
                                }});
                    });
                    adapter.addItem(new Vecino(piso_hogar,puerta_hogar, correo_i), adapter.getItemCount());
                    popupVecinos.dismiss();
                    Toast toast = Toast.makeText(this, "Compañero añadido", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
            }
        });
        configurarTeclado(popupVecinos);
        // Mostrar el segundo pop-up
        popupVecinos.show();
    }

    private void configurarTeclado(Dialog popupView){
        ConstraintLayout rootLayout = popupView.findViewById(R.id.contenedor); // Asegúrate de que este ID sea el correcto
        rootLayout.setOnTouchListener((v, event) -> {
            View currentFocus = popupView.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
            }
            return false; // Devuelve false para que otros eventos de toque se procesen normalmente
        });
    }

    private boolean verificarCorreo(String correo){
        if (correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return true;
        }
        return false;
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
                    piso_hogar = piso;
                    String puerta = vecino.getPuerta();
                    puerta_hogar = puerta;
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
                                        adapter = new MiembrosHogarAdapter(vecinos, new MiembrosHogarAdapter.OnItemClickListener() {
                                            @Override
                                            public void onEliminarClick(Vecino vecino, int position) {
                                                mostrarPopupDesvincular(vecino, vecino.getCorreo(), edificioSeleccionado, position);
                                            }
                                        });
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

    private void mostrarPopupDesvincular(Vecino vecino, String correo, String edificio, int position) {

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


}
