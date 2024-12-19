package com.example.design_vicent_sprint1.model;

public class Weather {
    private double temperature;  // Temperatura actual     // Fecha (opcional)
    private String condition;    // Estado del clima (ej. "Soleado", "Lluvia")
    private String iconUrl;      // URL del icono del clima

    public Weather(double temperature, String condition, String iconUrl) {
        this.condition = condition;
        this.iconUrl = iconUrl;
        this.temperature = temperature;
    }

    // Getters
    public String getCondition() {
        return condition;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public double getTemperature() {
        return temperature;
    }
}

