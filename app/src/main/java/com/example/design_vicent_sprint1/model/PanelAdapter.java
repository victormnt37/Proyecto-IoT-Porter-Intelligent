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

import java.util.List;

public class PanelAdapter extends RecyclerView.Adapter<PanelAdapter.PanelViewHolder> {

    private List<Panel> paneles;
    private String edificioSeleccionado;
    private SensorData datosSensor;
    private Context context;

    private PanelViewHolder holderActual;
    
    private final String noData = "Esperando datos MQTT...";

    public PanelAdapter(List<Panel> paneles, String edificioSeleccionado, SensorData datosSensor, Context context) {
        this.paneles = paneles;
        this.edificioSeleccionado = edificioSeleccionado;
        this.datosSensor = datosSensor;
        this.context = context;
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
                break;
            case "Tiempo":
//                llenarTiempo(holder);
                mostrarTemperatura(holder);
                break;
            case "Acceso":
                break;
            case "Estado de Puertas":
                break;
            case "Movimiento":
                // recibe la distancia
                mostrarMovimiento(holder);
                break;
            case "Ruido":
                // recibe si hay ruido o no
                mostrarRuido(holder);
                break;
            case "Luz":
                // recibe el nivel de luz
                mostrarLuz(holder);
                break;
            case "Humo y Gas":
                // recibe el nivel de gas
                mostrarGas(holder);
                break;
            default:
                break;
        }
    }

    public void llenarTiempo(PanelViewHolder holder) {
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
        iconoTemperatura.setImageResource(R.drawable.icon_alerta); // Reemplaza con tu recurso de imagen

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




    public void mostrarMovimiento(PanelViewHolder holder) {
        TextView hayMovimiento = new TextView(holder.itemView.getContext());
        if (datosSensor != null) {
            Boolean distancia = datosSensor.getDistancia();
            if (distancia != null) {
                if (distancia) {
                    hayMovimiento.setText("Hay movimiento. ¿Será el cartero?");
                } else {
                    hayMovimiento.setText("No hay movimiento. Todo tranquilo por aquí.");
                }
            } else {
                hayMovimiento.setText(noData);
            }
        } else {
            hayMovimiento.setText(noData);
        }
        holder.panelVacio.addView(hayMovimiento);
    }

    public void mostrarRuido(PanelViewHolder holder) {
        TextView hayRuido = new TextView(holder.itemView.getContext());
        if (datosSensor != null) {
            Boolean ruido = datosSensor.getRuido();
            if (ruido != null) {
                if (ruido) {
                    hayRuido.setText("Hay ruido. Será un coche, o la vecina de Fédor.");
                } else {
                    hayRuido.setText("No hay ruido. La calle está tranquila.");
                }
            } else {
                hayRuido.setText(noData);
            }
        } else {
            hayRuido.setText(noData);
        }
        holder.panelVacio.addView(hayRuido);
    }


    public void mostrarLuz(PanelViewHolder holder) {
        TextView nivelDeLuz = new TextView(holder.itemView.getContext());
        if (datosSensor != null) {
            String luz = datosSensor.getLuz(); // Asegúrate del tipo retornado (Integer, Double, etc.)
            if (luz != null) {
                nivelDeLuz.setText("Nivel de luz actual: " + luz);
            } else {
                nivelDeLuz.setText(noData);
            }
        } else {
            nivelDeLuz.setText(noData);
        }
        holder.panelVacio.addView(nivelDeLuz);
    }

    public void mostrarGas(PanelViewHolder holder) {
        TextView nivelDeGas = new TextView(holder.itemView.getContext());
        if (datosSensor != null) {
            String gas = datosSensor.getGas(); // Asegúrate del tipo retornado (Integer, Double, etc.)
            if (gas != null) {
                nivelDeGas.setText("Nivel de gas actual: " + gas);
            } else {
                nivelDeGas.setText(noData);
            }
        } else {
            nivelDeGas.setText(noData);
        }
        holder.panelVacio.addView(nivelDeGas);
    }
    private void lanzarActividad(Panel panel) {
        Intent intent = new Intent(context, RegistroDatosSensorActivity.class);
        if (datosSensor != null) {
            intent.putExtra("tipo-sensor", panel.getTipo());
            intent.putExtra("edificio", edificioSeleccionado);
        }
        context.startActivity(intent);
    }
}
