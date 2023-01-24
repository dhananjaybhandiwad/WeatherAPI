package com.fraunhofer.weatherapi.service;

import com.fraunhofer.weatherapi.Interface.WeatherServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class WeatherService implements WeatherServiceInterface {

    public ResponseEntity<?> getWeather(String lat, String lon){
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&hourly=temperature_2m,rain,windspeed_10m";
            RestTemplate template = new RestTemplate();
            Object response = template.getForObject(url, Object.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(HttpClientErrorException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
