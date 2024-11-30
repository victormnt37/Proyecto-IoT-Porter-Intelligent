package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Weather;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MetaWeatherService {
    @GET("location/search/")
    Call<List<Weather>> getWeatherByCity(@Query("query") String city);
}
