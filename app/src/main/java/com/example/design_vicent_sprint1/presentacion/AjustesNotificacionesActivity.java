package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.design_vicent_sprint1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AjustesNotificacionesActivity extends AppCompatActivity {
    private Button btnGuardar;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Switch switchAccessos, switchMovimientos, switchRuidos, switchLuz, switchVibraciones, switchGas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_notificaciones);

        switchAccessos = findViewById(R.id.switchAccessos);
        switchMovimientos = findViewById(R.id.switchMovimientos);
        switchRuidos = findViewById(R.id.switchRuidos);
        switchLuz = findViewById(R.id.switchLuz);
        switchVibraciones = findViewById(R.id.switchVibraciones);
        switchGas = findViewById(R.id.switchGas);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // actualizar datos notificaciones

//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                DocumentReference docRef = db.collection("edificios").document("edificio");
//                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w("Firestore", "Error al escuchar cambios", e);
//                            return;
//                        }
//                        if (snapshot != null && snapshot.exists()) {
//                            String valor = snapshot.getString("campo");
//                            mostrarNotificacion("Actualización", valor);
//                        }
//                    }
//                });


                // volver a main activity
                String user_id = auth.getCurrentUser().getUid();
                Intent i = new Intent(AjustesNotificacionesActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("userId", user_id);
                startActivity(i);
                finish();
            }
        });
    }
}
