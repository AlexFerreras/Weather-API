package com.hackerrank.weather.service;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public List<Weather> getFilteredWeatherList( Optional<Date> date, Optional<List<String>> city,
                                                 Optional<String> sort){

        List<Weather> sortedWeatherList = weatherRepository.findAll();
        sortedWeatherList.sort(Comparator.comparing(Weather::getId));

        if (date.isPresent()){
            sortedWeatherList = sortedWeatherList.stream()
                    .filter(weather -> weather.getDate().compareTo(date.get()) == 0)
                    .collect(Collectors.toList());
        }

        if (city.isPresent()){
            sortedWeatherList = sortedWeatherList.stream()
                    .filter(weather -> city.get().stream()
                            .anyMatch(weather.getCity()::equalsIgnoreCase))
                    .collect(Collectors.toList());
        }

        if(sort.isPresent()){
            if(sort.get().equals("date")){
                sortedWeatherList.sort(Comparator.comparing(Weather::getDate));
            }

            if(sort.get().equals("-date")){
                sortedWeatherList.sort(Comparator.comparing(Weather::getDate).reversed());
            }
        }

        return sortedWeatherList;
    }
}
