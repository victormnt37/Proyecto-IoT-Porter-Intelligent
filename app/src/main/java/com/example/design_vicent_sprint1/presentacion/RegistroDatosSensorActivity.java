package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.design_vicent_sprint1.R;

public class RegistroDatosSensorActivity extends AppCompatActivity {
    private TextView registroSensor;
    private TextView graficoSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_datos_sensor);

        Intent intent = getIntent();
        String tipoSensor = intent.getStringExtra("tipo-sensor");
        String edificio = intent.getStringExtra("edificio");

        registroSensor = findViewById(R.id.textView10);
        registroSensor.setText("Registro " + tipoSensor);

        graficoSensor = findViewById(R.id.textView11);
        graficoSensor.setText("Aquí se verá los gráficos del sensor");
    }
}
