package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.service.GermanCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class GermanCityController {
    private final GermanCityService germanCityService;

    @GetMapping
    public List<GermanCity> getAllCities() {
        return germanCityService.getAllCities();
    }

    @GetMapping("/{id}")
    public GermanCity getCityById(@PathVariable Long id) {
        return germanCityService.getCityById(id);
    }

    @PostMapping
    public GermanCity createCity(@RequestBody GermanCity city) {
        return germanCityService.saveCity(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        germanCityService.deleteCity(id);
    }
}
