package com.fraunhofer.weatherapi.Interface;

import org.springframework.http.ResponseEntity;

public interface WeatherServiceInterface {
    public ResponseEntity<?> getWeather(String lat, String lon);
}
