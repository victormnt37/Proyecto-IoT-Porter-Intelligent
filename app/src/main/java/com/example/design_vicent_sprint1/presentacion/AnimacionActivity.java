package com.example.design_vicent_sprint1.presentacion;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.design_vicent_sprint1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AnimacionActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animacion);

        LottieAnimationView animationView = findViewById(R.id.animacion);
        verificaSiUsuarioValidado();
        new Handler().postDelayed(() -> {
            if(i == null ){
                i = new Intent(AnimacionActivity.this, CustomLoginActivity.class);
            }
            startActivity(i);
            finish(); // Finaliza SplashActivity para que no regrese al presionar "Atrás"
        }, 3000); // Tiempo en milisegundos (ajústalo a la duración de tu animación) // Opcional, si usas lottie_autoPlay no es necesario
    }

    private void verificaSiUsuarioValidado() {

        if (auth.getCurrentUser() != null) {

            String cuenta_usuario = auth.getCurrentUser().getEmail();
            if(cuenta_usuario == null || cuenta_usuario.isEmpty()){
                cuenta_usuario = auth.getCurrentUser().getDisplayName(); ;
            }

            //combrobar si usuario autorizado (tiene edificios vinculados)
            DocumentReference usuario = FirebaseFirestore.getInstance()
                    .collection("usuarios").document(cuenta_usuario);
            String cuenta = cuenta_usuario;
            usuario.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot userId = task.getResult();
                    if (userId.exists()) {
                        i = new Intent(AnimacionActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("userId", cuenta);
                    } else {
                        auth.signOut();
                        i = new Intent(AnimacionActivity.this, CustomLoginActivity.class);
                    }
                } else {
                    Log.e("Firestore", "Error al obtener el documento", task.getException());
                    i = new Intent(AnimacionActivity.this, CustomLoginActivity.class);
                }
            });
        }
    }
}
