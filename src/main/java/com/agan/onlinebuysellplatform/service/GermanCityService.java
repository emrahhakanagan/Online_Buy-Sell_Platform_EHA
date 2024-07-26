package com.agan.onlinebuysellplatform.service;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.repository.GermanCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GermanCityService {
    private final GermanCityRepository germanCityRepository;

    public List<GermanCity> getAllCities() {
        return germanCityRepository.findAll();
    }

    public GermanCity getCityById(Long id) {
        return germanCityRepository.findById(id).orElse(null);
    }

    public GermanCity saveCity(GermanCity city) {
        return germanCityRepository.save(city);
    }

    public void deleteCity(Long id) {
        germanCityRepository.deleteById(id);
    }
}
