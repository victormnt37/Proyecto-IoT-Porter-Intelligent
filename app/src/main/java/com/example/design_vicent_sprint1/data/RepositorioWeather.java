package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Edificio;
import com.example.design_vicent_sprint1.model.Weather;

import retrofit2.Call;
import retrofit2.Response;
import java.util.List;

public class RepositorioWeather {
    private final MetaWeatherService api;

    public RepositorioWeather() {
        api = RetrofitClient.getInstance().create(MetaWeatherService.class);
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
            Call<List<Weather>> call = api.getWeatherByCity(ciudad);
            Response<List<Weather>> response = call.execute();

            return response.body() != null && !response.body().isEmpty()
                    ? response.body().get(0)
                    : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

