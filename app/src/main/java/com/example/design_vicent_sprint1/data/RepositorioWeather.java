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

