package com.example.design_vicent_sprint1.data;

import com.example.design_vicent_sprint1.model.Weather;

public class WeatherResponse {
    private Current current;

    public static class Current {
        private double temp_c;
        private Condition condition;

        public static class Condition {
            private String text;
            private String icon;
        }
    }

    public Weather toWeather() {
        return new Weather(current.temp_c, current.condition.text, current.condition.icon);
    }
}

