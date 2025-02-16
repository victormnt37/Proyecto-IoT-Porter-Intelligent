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
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegistroDatosSensorActivity extends AppCompatActivity {
    private TextView registroSensor;
    private GraphView graphView;
    private TextView infoBubble;

    private final Set<String> sensoresConGrafico = Set.of(
            "Temperatura", "Accesos", "Movimiento", "Ruido", "Luz", "Humo y Gas"
    );

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

        if (sensoresConGrafico.contains(tipoSensor)) {
            graphView = findViewById(R.id.idGraphView);
            infoBubble = findViewById(R.id.infoBubble);

            Log.d("registro datos", registroDatos.toString());
            List<DataPoint> puntos = new ArrayList<>();

            // Ordenar las claves (fechas) del registro de datos
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta al formato de tus claves
            List<String> clavesOrdenadas = new ArrayList<>(registroDatos.keySet());
            Collections.sort(clavesOrdenadas, (a, b) -> {
                try {
                    Date dateA = dateFormat.parse(a);
                    Date dateB = dateFormat.parse(b);
                    return dateA.compareTo(dateB);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            });

            // Mapear claves ordenadas a valores
            for (int i = 0; i < clavesOrdenadas.size(); i++) {
                String clave = clavesOrdenadas.get(i);
                Object datosDia = registroDatos.get(clave);
                if (datosDia != null) {
                    String valorString = (String) ((HashMap<String, ?>) datosDia).get(tipoSensor);
                    try {
                        double valor = Double.parseDouble(valorString);
                        puntos.add(new DataPoint(i, valor));
                    } catch (NumberFormatException e) {
                        Log.e("Parse Error", "Valor no válido para " + clave + ": " + valorString);
                    }
                }
            }

            // Crear y configurar la serie de datos
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(puntos.toArray(new DataPoint[0]));
            graphView.addSeries(series);

            // Configuración del gráfico
            //graphView.setTitle(tipoSensor + " en " + edificio);
            graphView.setTitleColor(R.color.blue);
            graphView.setTitleTextSize(16);

            // Configurar etiquetas personalizadas para el eje X
            StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graphView);
            labelsFormatter.setHorizontalLabels(clavesOrdenadas.toArray(new String[0]));
            graphView.getGridLabelRenderer().setLabelFormatter(labelsFormatter);
            graphView.getGridLabelRenderer().setHorizontalLabelsAngle(110);

            // Configurar los límites del eje X
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(puntos.size() - 1);

            // Evento al tocar un punto del gráfico
            series.setOnDataPointTapListener((series1, dataPoint) -> {
                String info ="Dato=" + dataPoint.getY();
                infoBubble.setText(info);

                infoBubble.post(() -> {
                    Point screenPosition = convertirCoordenadasAPantalla(dataPoint);
                    if (screenPosition != null) {
                        ajustarPosicionInfoBubble(screenPosition);
                    }
                });
            });
        } else {
            // Paneles sin gráfico
            registroSensor.setText("No hay gráfico disponible para el sensor " + tipoSensor);
        }
    }

    /**
     * Convierte las coordenadas del DataPoint a las coordenadas de pantalla.
     */
    private Point convertirCoordenadasAPantalla(DataPointInterface dataPoint) {
        if (graphView == null) return null;

        double x = dataPoint.getX();
        double y = dataPoint.getY();

        double minX = graphView.getViewport().getMinX(false);
        double maxX = graphView.getViewport().getMaxX(false);
        double minY = graphView.getViewport().getMinY(false);
        double maxY = graphView.getViewport().getMaxY(false);

        int graphWidth = graphView.getWidth();
        int graphHeight = graphView.getHeight();

        int posX = (int) ((x - minX) / (maxX - minX) * graphWidth);
        int posY = graphHeight - (int) ((y - minY) / (maxY - minY) * graphHeight);

        int[] graphLocation = new int[2];
        graphView.getLocationOnScreen(graphLocation);

        return new Point(posX + graphLocation[0], posY + graphLocation[1]);
    }

    /**
     * Ajusta la posición de la burbuja de información en la pantalla.
     */
    private void ajustarPosicionInfoBubble(Point screenPosition) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) infoBubble.getLayoutParams();
        int bubbleWidth = infoBubble.getLayoutParams().width;

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
}
