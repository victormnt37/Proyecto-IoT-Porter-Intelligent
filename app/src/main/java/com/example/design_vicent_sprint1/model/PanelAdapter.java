package com.example.design_vicent_sprint1.model;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioWeather;
import com.example.design_vicent_sprint1.presentacion.MainActivity;
import com.example.design_vicent_sprint1.presentacion.RegistroDatosSensorActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PanelAdapter extends RecyclerView.Adapter<PanelAdapter.PanelViewHolder> {

    private List<Panel> paneles;
    private String edificioSeleccionado;
    private SensorData datosSensor;
    private Context context;
    private HashMap<String, Object> registroDatos;

    private PanelViewHolder holderActual;
    
    private final String noData = "Esperando datos MQTT...";

    public PanelAdapter(List<Panel> paneles, String edificioSeleccionado, SensorData datosSensor, Context context, Map<String, Object> registroDatos) {
        this.paneles = paneles;
        this.edificioSeleccionado = edificioSeleccionado;
        this.datosSensor = datosSensor;
        this.context = context;
        this.registroDatos = (HashMap<String, Object>) registroDatos;
    }

    @NonNull
    @Override
    public PanelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_panel, parent, false);
        return new PanelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanelViewHolder holder, int position) {
        Panel panel = paneles.get(position);
        holder.tipoPanel.setText(panel.getTipo());

        holder.panelVacio.removeAllViews();

        holder.panelVacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarActividad(panel);
            }
        });

        actualizarDatos(panel, holder);
        holderActual = holder;
    }

    @Override
    public int getItemCount() {
        return paneles.size();
    }

    public static class PanelViewHolder extends RecyclerView.ViewHolder {
        TextView tipoPanel;
        LinearLayout panelVacio;

        public PanelViewHolder(@NonNull View itemView) {
            super(itemView);
            tipoPanel = itemView.findViewById(R.id.tipoPanel);
            panelVacio = itemView.findViewById(R.id.panelVacio);
        }
    }

    public void llenarDatosMQTT(SensorData datos, RecyclerView recyclerView) {
        this.datosSensor = datos;

        for (int i = 0; i < paneles.size(); i++) {
            int position = i;
            Panel panel = paneles.get(i);

            holderActual.itemView.post(() -> {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);

                if (viewHolder instanceof PanelAdapter.PanelViewHolder) {
                    PanelAdapter.PanelViewHolder holder = (PanelAdapter.PanelViewHolder) viewHolder;

                    holder.panelVacio.removeAllViews();
                    holder.tipoPanel.setText(panel.getTipo());
                    actualizarDatos(panel, holder);
                }
            });
        }
    }

    public void actualizarDatos(Panel panel, PanelViewHolder holder) {
        switch (panel.getTipo()) {
            case "Actividad Reciente":
                mostrarActividadReciente(holder);
                break;
            case "Temperatura":
                mostrarTemperatura(holder);
                break;
            case "Accesos":
                mostrarAccesos(holder);
                break;
            case "Movimiento":
                mostrarMovimiento(holder);
                break;
            case "Ruido":
                mostrarRuido(holder);
                break;
            case "Luz":
                mostrarLuz(holder);
                break;
            case "Humo y Gas":
                mostrarGas(holder);
                break;
            default:
                break;
        }
    }

    public void llenarTiempo(PanelViewHolder holder) {
        // Contacto con el api del tiempo, actualmente no implementado
        RepositorioWeather RepositorioWeather = new RepositorioWeather();
        // pedir el edificio con el edificioSeleccionado
        Edificio edificio = new Edificio("", "Edificio Central", "Calle Principal", "Madrid");
        //Edificio edificio = panel.getEdificio();

        Weather weather = RepositorioWeather.getWeatherForEdificio(edificio);

        TextView elTiempo = new TextView(holder.itemView.getContext());

        // ahora mismo weather siempre es nulo
        if (weather != null) {
            elTiempo.setText("Estado del clima: " + weather.getCondition() + ", Temperatura en " + edificio.getCiudad() + ": " + weather.getTemperature() + "°C");
        } else {
            elTiempo.setText("No se pudo obtener el clima.");
        }
    }

    public void mostrarActividadReciente(PanelViewHolder holder) {
        // peticion de los datos
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView actividadReciente = new TextView(holder.itemView.getContext());

        db.collection("edificios/" + edificioSeleccionado + "/actividad-reciente")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Obtener el último documento
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // Extraer los datos del documento
                        Map<String, Object> data = document.getData();

                        if (data != null) {
                            StringBuilder builder = new StringBuilder();
                            if (data.get("distancia") == "true") {
                                builder.append("Movimiento detectado").append("\n");
                            }

                            String gas = (String) data.get("gas");
                            String gasSinPorcentaje = gas.replace("%", "");
                            double porcentajeGas = Double.parseDouble(gasSinPorcentaje);

                            if (porcentajeGas >= 1.00) {
                                builder.append("Gas detectado").append("\n");
                            }

                            String luz = (String) data.get("luz");
                            String numeroSinPorcentaje = luz.replace("%", "");
                            double porcentajeLuz = Double.parseDouble(numeroSinPorcentaje);

                            if (porcentajeLuz <= 30.00) {
                                builder.append("Poca luz").append("\n");
                            } else if (porcentajeLuz <= 70.00) {
                                builder.append("Sombra").append("\n");
                            } else if (porcentajeLuz > 70.00) {
                                builder.append("Luz alta").append("\n");
                            }

                            if (data.get("ruido") == "true") {
                                builder.append("Ruido detectado").append("\n");
                            }

//                            builder.append("Temperatura: ").append(data.get("temperatura")).append("\n");

                            // Mostrar los datos en el TextView
                            actividadReciente.setText(builder.toString());
                        } else {
                            actividadReciente.setText("El documento no contiene datos.");
                        }
                    } else {
                        actividadReciente.setText("No se encontraron documentos.");
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejar errores
                    actividadReciente.setText("Error al cargar datos: " + e.getMessage());
                });

        // mostrar
        holder.panelVacio.removeAllViews();
        holder.panelVacio.setOrientation(LinearLayout.HORIZONTAL);
        holder.panelVacio.addView(actividadReciente);
    }

    public void mostrarTemperatura(PanelViewHolder holder) {
        // Crear un TextView para el texto
        TextView temperaturaActual = new TextView(holder.itemView.getContext());
        temperaturaActual.setTextSize(16);
        temperaturaActual.setPadding(8, 8, 8, 8);

        // Crear un ImageView para la imagen
        ImageView iconoTemperatura = new ImageView(holder.itemView.getContext());

        // Convertir dp a píxeles para tamaños adaptativos
        int tamañoEnDp = 75; // Tamaño deseado en dp
        int tamañoEnPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                tamañoEnDp,
                holder.itemView.getResources().getDisplayMetrics()
        );

        // Configurar las dimensiones del ícono
        LinearLayout.LayoutParams layoutParamsIcono = new LinearLayout.LayoutParams(
                tamañoEnPx, // Ancho en píxeles
                tamañoEnPx  // Alto en píxeles
        );
        layoutParamsIcono.setMargins(0, 0, 16, 0); // Margen derecho para separar del texto
        iconoTemperatura.setLayoutParams(layoutParamsIcono);
        iconoTemperatura.setImageResource(R.drawable.icon_termometro); // Reemplaza con tu recurso de imagen

        // Configurar el texto
        if (datosSensor != null) {
            double temperatura = datosSensor.getTemperatura();
            if (temperatura == 100000000.0) {
                temperaturaActual.setText("Esperando datos de temperatura...");
            } else {
                temperaturaActual.setText("Temperatura actual: " + temperatura + "°C");
            }
        } else {
            temperaturaActual.setText("No hay datos disponibles.");
        }

        // Limpiar el contenedor y agregar las vistas
        holder.panelVacio.removeAllViews();

        // Cambiar orientación del contenedor para alinearlos horizontalmente
        holder.panelVacio.setOrientation(LinearLayout.HORIZONTAL);

        holder.panelVacio.addView(iconoTemperatura); // Agregar el icono primero
        holder.panelVacio.addView(temperaturaActual); // Agregar el texto después
    }



    // Método para mostrar accesos
    public void mostrarAccesos(PanelViewHolder holder) {
        // Crear un TextView para los accesos
        // TODO: poner datos firestore aqui
        TextView accesos = new TextView(holder.itemView.getContext());
        accesos.setTextSize(16);
        accesos.setPadding(8, 8, 8, 8);

        // Crear un ImageView para el ícono
        ImageView iconoAccesos = new ImageView(holder.itemView.getContext());
        configurarIcono(iconoAccesos, R.drawable.icon_puerta); // Reemplaza con el recurso adecuado

        // Configurar el texto
        if (registroDatos != null) {
            List<String> datos = new ArrayList<>();

            for (Object datosDia : registroDatos.values()) {
                String dato = (String) ((HashMap<String, ?>) datosDia).get("Accesos");
                datos.add(dato);
            }

//            Log.d("Registro", registroDatos.toString());
//            Log.d("Accesos", datos.toString());
//            accesos.setText(datos.get(6));
        }

        configurarPanel(holder, iconoAccesos, accesos);
    }

    // Método para mostrar movimiento
    public void mostrarMovimiento(PanelViewHolder holder) {
        TextView hayMovimiento = new TextView(holder.itemView.getContext());
        hayMovimiento.setTextSize(16);
        hayMovimiento.setPadding(8, 8, 8, 8);

        ImageView iconoMovimiento = new ImageView(holder.itemView.getContext());
        configurarIcono(iconoMovimiento, R.drawable.icon_movimiento);

        if (datosSensor != null) {
            Boolean distancia = datosSensor.getDistancia();
            if (distancia != null) {
                hayMovimiento.setText(distancia ? "Hay movimiento. ¿Será el cartero?" : "No hay movimiento. Todo tranquilo por aquí.");
            } else {
                hayMovimiento.setText("Esperando datos de movimiento...");
            }
        } else {
            hayMovimiento.setText("No hay datos disponibles.");
        }

        configurarPanel(holder, iconoMovimiento, hayMovimiento);
    }

    // Método para mostrar ruido
    public void mostrarRuido(PanelViewHolder holder) {
        TextView hayRuido = new TextView(holder.itemView.getContext());
        hayRuido.setTextSize(16);
        hayRuido.setPadding(8, 8, 8, 8);

        ImageView iconoRuido = new ImageView(holder.itemView.getContext());
        configurarIcono(iconoRuido, R.drawable.icon_alerta);

        if (datosSensor != null) {
            Boolean ruido = datosSensor.getRuido();
            if (ruido != null) {
                hayRuido.setText(ruido ? "Hay ruido. Será un coche, o la vecina de Fédor." : "No hay ruido. La calle está tranquila.");
            } else {
                hayRuido.setText("Esperando datos de ruido...");
            }
        } else {
            hayRuido.setText("No hay datos disponibles.");
        }

        configurarPanel(holder, iconoRuido, hayRuido);
    }

    // Método para mostrar luz
    public void mostrarLuz(PanelViewHolder holder) {
        TextView nivelDeLuz = new TextView(holder.itemView.getContext());
        nivelDeLuz.setTextSize(16);
        nivelDeLuz.setPadding(8, 8, 8, 8);

        ImageView iconoLuz = new ImageView(holder.itemView.getContext());
        configurarIcono(iconoLuz, R.drawable.icon_sol);

        if (datosSensor != null) {
            String luz = datosSensor.getLuz();
            nivelDeLuz.setText(luz != null ? "Nivel de luz actual: " + luz : "Esperando datos de luz...");
        } else {
            nivelDeLuz.setText("No hay datos disponibles.");
        }

        configurarPanel(holder, iconoLuz, nivelDeLuz);
    }

    // Método para mostrar gas
    public void mostrarGas(PanelViewHolder holder) {
        TextView nivelDeGas = new TextView(holder.itemView.getContext());
        nivelDeGas.setTextSize(16);
        nivelDeGas.setPadding(8, 8, 8, 8);

        ImageView iconoGas = new ImageView(holder.itemView.getContext());
        configurarIcono(iconoGas, R.drawable.icon_fuego);

        if (datosSensor != null) {
            String gas = datosSensor.getGas();
            nivelDeGas.setText(gas != null ? "Nivel de gas actual: " + gas : "Esperando datos de gas...");
        } else {
            nivelDeGas.setText("No hay datos disponibles.");
        }

        configurarPanel(holder, iconoGas, nivelDeGas);
    }

    // Método auxiliar para configurar los íconos
    private void configurarIcono(ImageView icono, int recurso) {
        int tamañoEnDp = 75; // Tamaño deseado en dp
        int tamañoEnPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                tamañoEnDp,
                icono.getContext().getResources().getDisplayMetrics()
        );

        LinearLayout.LayoutParams layoutParamsIcono = new LinearLayout.LayoutParams(tamañoEnPx, tamañoEnPx);
        layoutParamsIcono.setMargins(0, 0, 16, 0); // Margen derecho para separar del texto
        icono.setLayoutParams(layoutParamsIcono);
        icono.setImageResource(recurso);
    }

    // Método auxiliar para configurar el panel
    private void configurarPanel(PanelViewHolder holder, ImageView icono, TextView texto) {
        holder.panelVacio.removeAllViews();
        holder.panelVacio.setOrientation(LinearLayout.HORIZONTAL);
        holder.panelVacio.addView(icono);
        holder.panelVacio.addView(texto);
    }

    private void lanzarActividad(Panel panel) {
        Intent intent = new Intent(context, RegistroDatosSensorActivity.class);
        if (datosSensor != null) {
            // TODO: pasar datos de registro
            intent.putExtra("tipo-sensor", panel.getTipo());
            intent.putExtra("edificio", edificioSeleccionado);
            intent.putExtra("registro-datos", registroDatos);
        }
        context.startActivity(intent);
    }
}
