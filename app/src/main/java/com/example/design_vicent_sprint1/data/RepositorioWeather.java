package com.example.design_vicent_sprint1.data;

import android.util.Log;

import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.Weather;

import retrofit2.Call;
import retrofit2.Response;
import java.util.List;

public class RepositorioWeather {
    private final WeatherApi api;
    private final String API_KEY = "5ed7c78738bf49c5b2e92050241512";

    public RepositorioWeather() {
        api = RetrofitClient.getInstance().create(WeatherApi.class);
    }

    /*
    Este es un ejemplo de como se puede gastar el api:

    WeatherRepository weatherRepository = new WeatherRepository();
    Edificio edificio = new Edificio(1, "Edificio Central", "Calle Principal", "Madrid");

    Weather weather = weatherRepository.getWeatherForEdificio(edificio);

    if (weather != null) {
        System.out.println("Estado del clima: " + weather.getWeatherStateName());
        System.out.println("Temperatura actual: " + weather.getTheTemp() + "°C");
    } else {
        System.out.println("No se pudo obtener el clima.");
    }

     */

    public Weather getWeatherForEdificio(Edificio edificio) {
        try {
            String ciudad = edificio.getCiudad();
            Call<WeatherResponse> call = api.getCurrentWeather(API_KEY, ciudad);
            Response<WeatherResponse> response = call.execute();

            return response.isSuccessful() ? response.body().toWeather() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

