package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityService {

    private Map<Integer, City> cities = new HashMap<>();

    public CityService() {
        cities.put(1, new City(1, "Hong Kong"));
        cities.put(2, new City(2, "Bangkok"));
        cities.put(3, new City(3, "London"));
        cities.put(4, new City(4, "Paris"));
    }

    public List<City> getAllCities() {
        return new ArrayList<>(cities.values());
    }

    public City findById(int id) {
        return cities.get(id);
    }
}
