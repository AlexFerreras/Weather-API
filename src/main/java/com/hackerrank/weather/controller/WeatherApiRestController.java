package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
public class WeatherApiRestController {


    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherApiRestController(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

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
        Weather createdWeather = weatherRepository.save(weather);
        return new ResponseEntity<>(createdWeather, HttpStatus.CREATED);
    }

    /**
     * GET request to /weather:
     *
     * the response code is 200
     * the response body is an array of matching records, ordered by their ids in increasing order
     * accepts an optional query string parameter, date, in the format YYYY-MM-DD, for example /weather/?date=2019-06-11. When this parameter is present, only the records with the matching date are returned.
     * accepts an optional query string parameter, city, and when this parameter is present, only the records with the matching city are returned. The value of this parameter is case insensitive, so "London" and "london" are equivalent. Moreover, it might contain several values, separated by commas (e.g. city=london,Moscow), meaning that records with the city matching any of these values must be returned.
     * accepts an optional query string parameter, sort, that can take one of two values: either "date" or "-date". If the value is "date", then the ordering is by date in ascending order. If it is "-date", then the ordering is by date in descending order. If there are two records with the same date, the one with the smaller id must come first.
     */
    @GetMapping
    ResponseEntity<List<Weather>> getWeatherList(@RequestParam(name = "date", required = false) Optional<Date> date,
                                     @RequestParam(name = "city",required = false) Optional<List<String>> city,
                                     @RequestParam(name = "sort", required = false) Optional<String> sort) {


        List<Weather> sorterWeatherList = weatherRepository.findAll();
        sorterWeatherList.sort(Comparator.comparing(Weather::getId));

        if (date.isPresent()){
            sorterWeatherList = sorterWeatherList.stream()
                    .filter(weather -> weather.getDate().equals(date.get()))
                    .collect(Collectors.toList());
        }

        if (city.isPresent()){
            sorterWeatherList = sorterWeatherList.stream()
                    .filter(weather -> city.get().stream()
                            .anyMatch(weather.getCity()::equalsIgnoreCase))
                    .collect(Collectors.toList());
        }

        if(sort.isPresent()){
            if(sort.get().equals("date")){
                sorterWeatherList.sort(Comparator.comparing(Weather::getDate));
            }

            if(sort.get().equals("-date")){
                sorterWeatherList.sort(Comparator.comparing(Weather::getDate).reversed());
            }
        }


        return new ResponseEntity<>(sorterWeatherList, HttpStatus.OK);
    }

    /**
     * GET request to /weather/<id>:
     *
     * returns a record with the given id
     * if the matching record exists, the response code is 200 and the response body is the matching object
     * if there is no record in the collection with the given id, the response code is 404
     */

    @GetMapping("/{id}")
    ResponseEntity<?> getWeatherById(@PathVariable int id) {

        Optional<Weather> weather = weatherRepository.findById(id);
        if(!weather.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(weather, HttpStatus.OK);
    }



}
