package com.example.design_vicent_sprint1.presentacion;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.design_vicent_sprint1.R;


public class AnimacionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animacion);

        LottieAnimationView animationView = findViewById(R.id.animacion);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(AnimacionActivity.this, CustomLoginActivity.class);
            startActivity(intent);
            finish(); // Finaliza SplashActivity para que no regrese al presionar "Atrás"
        }, 3000); // Tiempo en milisegundos (ajústalo a la duración de tu animación) // Opcional, si usas lottie_autoPlay no es necesario
    }
}
