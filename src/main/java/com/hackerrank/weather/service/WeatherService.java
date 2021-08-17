package com.hackerrank.weather.service;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Weather> getFilteredWeatherList( Optional<Date> date, Optional<List<String>> cities,
                                                 Optional<String> sort){

        List<Weather> sortedWeatherList = weatherRepository.findAll();
        sortedWeatherList.sort(Comparator.comparing(Weather::getId));

        if (date.isPresent()){
            sortedWeatherList = getFilterWeatherListByDate(date.get(), sortedWeatherList);
        }

        if (cities.isPresent()){
            sortedWeatherList = getFilterWeatherListByCity(cities.get(), sortedWeatherList);
        }

        if(sort.isPresent()){
            sortedWeatherListByDate(sort.get(), sortedWeatherList);
        }

        return sortedWeatherList;
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

    private List<Weather> getFilterWeatherListByDate(Date date, List<Weather> sortedWeatherList) {
        return sortedWeatherList.stream()
                .filter(weather -> weather.getDate().compareTo(date) == 0)
                .collect(Collectors.toList());
    }
}
