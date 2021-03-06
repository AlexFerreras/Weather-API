package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import com.hackerrank.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/weather")
public class WeatherApiRestController {


    private final WeatherRepository weatherRepository;
    private final WeatherService weatherService;

    @Autowired
    public WeatherApiRestController(WeatherRepository weatherRepository, WeatherService weatherService) {
        this.weatherRepository = weatherRepository;
        this.weatherService = weatherService;
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

        List<Weather> filteredWeatherList = weatherService
                .getFilteredWeatherList(date, city, sort);

        return new ResponseEntity<>(filteredWeatherList, HttpStatus.OK);
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


}
