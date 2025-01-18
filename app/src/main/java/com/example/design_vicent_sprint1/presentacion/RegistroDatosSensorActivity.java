package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.design_vicent_sprint1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;

public class RegistroDatosSensorActivity extends AppCompatActivity {
    private TextView registroSensor;
    private int límiteDeDatos = 50; // maximos datos a recibir
    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_datos_sensor);

        Intent intent = getIntent();
        String tipoSensor = intent.getStringExtra("tipo-sensor");
        String edificio = intent.getStringExtra("edificio");

        // Petición a Firestore

        // la petición debe ir dirigida a cada sensor
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // documento: edificio/tipo-sensor
        db.collection("edificios/" + edificio + "/sensores/" + tipoSensor + "/")
                .limit(7) // limita la consulta a 50 documentos
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> datos = document.getData();
                        Log.d("Firestore", "Documento ID: " + document.getId() + " -> Datos: " + datos);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error al obtener documentos", e);
                });

        registroSensor = findViewById(R.id.textView10);
        registroSensor.setText("Registro " + tipoSensor);

        // on below line we are initializing our graph view.
        graphView = findViewById(R.id.idGraphView);

        // on below line we are adding data to our graph view.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                // on below line we are adding
                // each point on our x and y axis.
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 2)
        });

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        graphView.setTitle(tipoSensor + " en " + edificio);

        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.blue);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(16);

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series);
    }
}
