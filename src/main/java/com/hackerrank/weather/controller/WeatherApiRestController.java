package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/weather")
public class WeatherApiRestController {
    /**
     * Requirements:
     * The REST service must expose the /weather endpoint, which allows for managing the collection of weather records in the following way:
     *
     * POST request to /weather:
     *
     * creates a new weather data record
     * expects a valid weather data object as its body payload, except that it does not have an id property; you can assume that the given object is always valid
     * adds the given object to the collection and assigns a unique integer id to it
     * the response code is 201 and the response body is the created record, including its unique id
     */

    @PostMapping
    public ResponseEntity<?> saveWeather (@Valid @RequestBody Weather weather){


        return null;
    }



}
