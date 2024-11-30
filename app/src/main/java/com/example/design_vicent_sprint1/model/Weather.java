package com.example.design_vicent_sprint1.model;

public class Weather {
    private String weather_state_name;  // Estado del clima (nublado, soleado, etc.)
    private String applicable_date;     // Fecha para la que aplica el clima
    private float min_temp;             // Temperatura mínima
    private float max_temp;             // Temperatura máxima
    private float the_temp;             // Temperatura actual

    // Getters para acceder a los datos
    public String getWeatherStateName() {
        return weather_state_name;
    }

    public String getApplicableDate() {
        return applicable_date;
    }

    public float getMinTemp() {
        return min_temp;
    }

    public float getMaxTemp() {
        return max_temp;
    }

    public float getTheTemp() {
        return the_temp;
    }
}
