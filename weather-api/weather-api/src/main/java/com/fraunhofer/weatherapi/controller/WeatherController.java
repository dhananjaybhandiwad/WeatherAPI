package com.fraunhofer.weatherapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraunhofer.weatherapi.Entity.DerivedEntity.WeatherAPI;
import com.fraunhofer.weatherapi.Interface.WeatherServiceInterface;
import com.opencsv.CSVWriter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherController {
    @Autowired
    WeatherServiceInterface service;

    private final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    @GetMapping("/weatherApi/{latitude}/{longitude}")
    //@Retry(name = "weather-api", fallbackMethod = "hardCodedResponse")
    @CircuitBreaker(name = "weatherApi", fallbackMethod = "hardCodedResponse")
    public ResponseEntity<?> getWeather(@PathVariable String latitude, @PathVariable String longitude) {
        try {
            logger.info("WeatherApi Call Received");
            ResponseEntity<?> resp = service.getWeather(latitude, longitude);
            ObjectMapper mapper = new ObjectMapper();
            String respData = mapper.writeValueAsString(resp.getBody());
            WeatherAPI content = mapper.readValue(respData, WeatherAPI.class);
            write_file(content);
            return resp;
        } catch (JsonMappingException ex) {
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   public ResponseEntity<?> hardCodedResponse(Exception e){
        return new ResponseEntity<>("The External API is currently down", HttpStatus.INTERNAL_SERVER_ERROR);
   }

   public void write_file(WeatherAPI weather) throws IOException {
            File file = new File("./weather.csv");
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"Time","Temperature C","Rain mm","WindSpeed km/h"});
            for(int i=0;i<weather.hourly.time.size();i++){
                String forecast = weather.hourly.time.get(i) +" " + weather.hourly.temperature_2m.get(i) + " " + weather.hourly.rain.get(i)+" " +weather.hourly.windspeed_10m.get(i);
                String[] rowData = forecast.split(" ");
                data.add(rowData);
            }
            writer.writeAll(data);
            writer.close();
   }
}
