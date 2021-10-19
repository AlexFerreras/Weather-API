package com.hackerrank.weather.service;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public List<Weather> getFilteredWeatherList( Optional<Date> date, Optional<List<String>> cities,
                                                 Optional<String> sort){

        List<Weather> WeatherList = date.isPresent() ? weatherRepository.getAllByDate(date.get()) : weatherRepository.findAll();

         if(cities.isPresent()) {
            WeatherList = getFilterWeatherListByCity(cities.get(), WeatherList);
        }

        if(!sort.isPresent()){
            //sorting List by id.
            WeatherList.sort(Comparator.comparing(Weather::getId));
        }else{
            sortedWeatherListByDate(sort.get(), WeatherList);
        }

        return WeatherList;
    }

    private void sortedWeatherListByDate(String sort, List<Weather> sortedWeatherList) {
        if(sort.equals("date")){
            sortedWeatherList.sort(Comparator.comparing(Weather::getDate));
        }

        if(sort.equals("-date")){
            sortedWeatherList.sort(Comparator.comparing(Weather::getDate).reversed());
        }
    }

    private List<Weather> getFilterWeatherListByCity(List<String> cityList, List<Weather> sortedWeatherList) {
        return sortedWeatherList.stream()
                .filter(weather -> cityList.stream()
                        .anyMatch(weather.getCity()::equalsIgnoreCase))
                .collect(Collectors.toList());
    }
}
