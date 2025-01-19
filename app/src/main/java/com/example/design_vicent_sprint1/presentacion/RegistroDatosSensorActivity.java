package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.design_vicent_sprint1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegistroDatosSensorActivity extends AppCompatActivity {
    private TextView registroSensor;
    private GraphView graphView;
    private TextView infoBubble;
    private final Set<String> sensoresConGrafico = new HashSet<>(Arrays.asList("Temperatura", "Accesos", "Movimiento", "Ruido", "Luz", "Humo y Gas"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_datos_sensor);

        Intent intent = getIntent();
        String tipoSensor = intent.getStringExtra("tipo-sensor");
        String edificio = intent.getStringExtra("edificio");
        HashMap<String, Object> registroDatos = (HashMap<String, Object>) intent.getSerializableExtra("registro-datos");

        registroSensor = findViewById(R.id.textView10);
        registroSensor.setText("Registro " + tipoSensor);

        // Inicializar el gráfico
        if (sensoresConGrafico.contains(tipoSensor)) {

            graphView = findViewById(R.id.idGraphView);
            infoBubble = findViewById(R.id.infoBubble);

            // Rellenar registro datos
            Log.d("registro datos", registroDatos.toString());
            List<String> datos = new ArrayList<>();

            Set<String> clavesSet = registroDatos.keySet();
            List<String> dias = new ArrayList<>(clavesSet);

            Map<String, Object> registroOrdenado = new HashMap<>();

            for (String key : dias) {
                registroOrdenado.put(key, registroDatos.get(key));
            }

            for (Object datosDia : registroOrdenado.values()) {
                String dato = (String) ((HashMap<String, ?>) datosDia).get(tipoSensor);
                datos.add(dato);
            }

//            Collections.sort(dias);

            // Crear y configurar la serie de datos
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0, Integer.parseInt(datos.get(0))),
                    new DataPoint(1, Integer.parseInt(datos.get(1))),
                    new DataPoint(2, Integer.parseInt(datos.get(2))),
                    new DataPoint(3, Integer.parseInt(datos.get(3))),
                    new DataPoint(4, Integer.parseInt(datos.get(4))),
                    new DataPoint(5, Integer.parseInt(datos.get(5))),
                    new DataPoint(6, Integer.parseInt(datos.get(6)))
            });

            // Configuración del gráfico
            graphView.setTitle(tipoSensor + " en " + edificio);
            graphView.setTitleColor(R.color.blue);
            graphView.setTitleTextSize(16);

            // Configurar etiquetas personalizadas para el eje X
            StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graphView);
            labelsFormatter.setHorizontalLabels(new String[]{
                    dias.get(0), dias.get(1), dias.get(2), dias.get(3), dias.get(4), dias.get(5), dias.get(6)
            });
            graphView.getGridLabelRenderer().setLabelFormatter(labelsFormatter);
            graphView.getGridLabelRenderer().setHorizontalLabelsAngle(110);

            // Configurar el título del eje X
            //graphView.getGridLabelRenderer().setHorizontalAxisTitle("Días de la semana");

            // Configurar límites iniciales
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(6);

            //        graphView.getViewport().setYAxisBoundsManual(true);
            //        graphView.getViewport().setMinY(0);
            //        graphView.getViewport().setMaxY(8);

            // Añadir la serie al gráfico
            graphView.addSeries(series);

            series.setOnDataPointTapListener((series1, dataPoint) -> {
                String info = "X=" + dataPoint.getX() + ", Y=" + dataPoint.getY();
                infoBubble.setText(info);

                infoBubble.post(() -> {
                    Point screenPosition = convertirCoordenadasAPantalla(dataPoint);
                    if (screenPosition != null) {
                        int screenWidth = getResources().getDisplayMetrics().widthPixels;

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) infoBubble.getLayoutParams();
                        int bubbleWidth = infoBubble.getLayoutParams().width; // Usa el ancho fijo definido en el XML

                        int leftMargin = screenPosition.x - (bubbleWidth / 2);
                        if (leftMargin + bubbleWidth > screenWidth) {
                            leftMargin = screenPosition.x - bubbleWidth - 10;
                        }
                        if (leftMargin < 0) {
                            leftMargin = 10;
                        }

                        int topMargin = screenPosition.y - infoBubble.getHeight() - 10;
                        if (topMargin < 0) {
                            topMargin = screenPosition.y + 10;
                        }

                        params.leftMargin = leftMargin;
                        params.topMargin = topMargin;
                        infoBubble.setLayoutParams(params);

                        infoBubble.setVisibility(View.VISIBLE);
                    }
                });
            });
        } else {
            // Paneles sin gráfico
        }
    }

    /**
     * Convierte las coordenadas del DataPoint a las coordenadas de pantalla.
     */
    private Point convertirCoordenadasAPantalla(DataPointInterface dataPoint) {
        if (graphView == null) return null;

        // Obtener el Viewport y el ancho/alto de la gráfica
        double x = dataPoint.getX();
        double y = dataPoint.getY();

        // Obtener el rango actual de la gráfica
        double minX = graphView.getViewport().getMinX(false);
        double maxX = graphView.getViewport().getMaxX(false);
        double minY = graphView.getViewport().getMinY(false);
        double maxY = graphView.getViewport().getMaxY(false);

        // Calcular las posiciones relativas en píxeles
        int graphWidth = graphView.getWidth();
        int graphHeight = graphView.getHeight();

        // Convertir los valores de la gráfica a valores de píxeles
        int posX = (int) ((x - minX) / (maxX - minX) * graphWidth);
        int posY = graphHeight - (int) ((y - minY) / (maxY - minY) * graphHeight); // Invertir para coord. pantalla

        // Ajustar el offset relativo al GraphView
        int[] graphLocation = new int[2];
        graphView.getLocationOnScreen(graphLocation);

        return new Point(posX + graphLocation[0], posY + graphLocation[1]);
    }
}
