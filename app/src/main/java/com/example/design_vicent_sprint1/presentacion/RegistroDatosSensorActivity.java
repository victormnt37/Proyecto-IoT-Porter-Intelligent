package com.example.design_vicent_sprint1.presentacion;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.design_vicent_sprint1.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

public class RegistroDatosSensorActivity extends AppCompatActivity {
    private TextView registroSensor;
    private GraphView graphView;
    private TextView infoBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_datos_sensor);

        // Recibir datos del Intent
        Intent intent = getIntent();
        String tipoSensor = intent.getStringExtra("tipo-sensor");
        String edificio = intent.getStringExtra("edificio");

        registroSensor = findViewById(R.id.textView10);
        registroSensor.setText("Registro " + tipoSensor);

        // Inicializar el gráfico
        graphView = findViewById(R.id.idGraphView);
        infoBubble = findViewById(R.id.infoBubble);

        // Crear y configurar la serie de datos
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 3), // Lunes
                new DataPoint(1, 5), // Martes
                new DataPoint(2, 2), // Miércoles
                new DataPoint(3, 6), // Jueves
                new DataPoint(4, 4), // Viernes
                new DataPoint(5, 7), // Sábado
                new DataPoint(6, 1)  // Domingo
        });

        // Configuración del gráfico
        graphView.setTitle(tipoSensor + " en " + edificio);
        graphView.setTitleColor(R.color.blue);
        graphView.setTitleTextSize(16);

// Configurar etiquetas personalizadas para el eje X
        StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graphView);
        labelsFormatter.setHorizontalLabels(new String[]{
                "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"
        });
        graphView.getGridLabelRenderer().setLabelFormatter(labelsFormatter);

// Configurar el título del eje X
        //graphView.getGridLabelRenderer().setHorizontalAxisTitle("Días de la semana");





        // Configurar límites iniciales
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(6);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(8);

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
