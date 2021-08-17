package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
     * {@code POST  /weather}  : Creates a new weather.
     * <p>
     * Creates a new weather,
     * @param weather the valid weather to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the created weather.
     */
    @PostMapping
    public ResponseEntity<?> saveWeather (@Valid @RequestBody Weather weather){
        Weather createdWeather = weatherRepository.save(weather);
        return new ResponseEntity<>(createdWeather, HttpStatus.CREATED);
    }

    /**
     * {@code GET /weather} : get all weather.
     *
     * @param date, city, sort @optionals the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all weathers.
     */
    @GetMapping
    ResponseEntity<List<Weather>> getWeatherList(@RequestParam(name = "date", required = false)
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd")Optional<Date> date,
                                                 @RequestParam(name = "city",required = false) Optional<List<String>> city,
                                                 @RequestParam(name = "sort", required = false) Optional<String> sort) {

        List<Weather> sorterWeatherList = weatherRepository.findAll();
        sorterWeatherList.sort(Comparator.comparing(Weather::getId));

        if (date.isPresent()){
            sorterWeatherList = sorterWeatherList.stream()
                    .filter(weather -> weather.getDate().compareTo(date.get()) == 0)
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
     * {@code GET /weather/<id>} : get the weather whith the provided id.
     *
     * @param id of the weather to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weather whith provided id, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    ResponseEntity<?> getWeatherById(@PathVariable int id) {

        Optional<Weather> weather = weatherRepository.findById(id);
        if(!weather.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(weather, HttpStatus.OK);
    }


    /**
     * GET request to /repos/{repoId}/events:
     *
     * returns a collection of events related to the given repository
     * the response code is 200, and the response body is an array of events related to the given repository ordered by their ids in increasing order

     * GET request to /events/{eventId}:
     * returns an event with the given id
     * if the matching event exists, the response code is 200 and the response body is the matching event object
     * if there is no event in the collection with the given id, the response code is 404
     *
     *
     * GET request to all endpoints that return a collection of objects (/repos/{repoId}/events ,/users/{userId}/events, /events)
     *
     * takes the optional parameter public, which is used to filter the returned objects, e.g.:
     */

    @GetMapping("/events/{eventId}")
    ResponseEntity<?> getEventById(@PathVariable int eventId) {

        Optional<Weather> weather = weatherRepository.findById(eventId);
        if(!weather.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(weather);
    }


}
