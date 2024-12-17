package com.example.design_vicent_sprint1.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.design_vicent_sprint1.R;
import com.example.design_vicent_sprint1.data.RepositorioWeather;

import java.util.List;

public class PanelAdapter extends RecyclerView.Adapter<PanelAdapter.PanelViewHolder> {

    private List<Panel> paneles;
    private String edificioSeleccionado;
    private SensorData datosSensor;

    public PanelAdapter(List<Panel> paneles, String edificioSeleccionado, SensorData datosSensor) {
        this.paneles = paneles;
        this.edificioSeleccionado = edificioSeleccionado;
        this.datosSensor = datosSensor;
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

        switch (panel.getTipo()) {
            case "Tiempo":
                llenarTiempo(holder);
                break;
            case "Movimiento":
                // recibe la distancia
                break;
            case "Ruido":
                // valor
                break;
            case "Luz":
                // valor
                break;
            case "Vibraciones":
                // valor
                break;
            case "Humo y Gas":
                //
                break;
        }
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

    public void llenarTiempo(PanelViewHolder holder) {
        RepositorioWeather RepositorioWeather = new RepositorioWeather();
        // pedir el edificio con el edificioSeleccionado
        Edificio edificio = new Edificio("", "Edificio Central", "Calle Principal", "Madrid");
        //Edificio edificio = panel.getEdificio();

        Weather weather = RepositorioWeather.getWeatherForEdificio(edificio);

        TextView elTiempo = new TextView(holder.itemView.getContext());
        TextView temperaturaActual = new TextView(holder.itemView.getContext());

        // ahora mismo weather siempre es nulo
        if (weather != null) {
            elTiempo.setText("Estado del clima: " + weather.getCondition() + ", Temperatura en " + edificio.getCiudad() + ": " + weather.getTemperature() + "°C");
        } else {
            elTiempo.setText("No se pudo obtener el clima.");
        }

        if (datosSensor != null) {
            temperaturaActual.setText("Temperatura actual: " + datosSensor.getTemperatura());
        }

        holder.panelVacio.addView(elTiempo);
    }

    public void actualizarDatos( SensorData nuevosDatosSensor) {
        this.datosSensor = nuevosDatosSensor;
        Log.i("Datos actualizados", datosSensor.toString());
        notifyDataSetChanged(); // notifica al RecyclerView que los datos han cambiado
    }
}
