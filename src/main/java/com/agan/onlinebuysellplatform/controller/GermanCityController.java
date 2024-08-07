package com.agan.onlinebuysellplatform.controller;

import com.agan.onlinebuysellplatform.model.GermanCity;
import com.agan.onlinebuysellplatform.service.GermanCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GermanCity> getCityById(@PathVariable Long id) {
        GermanCity city = germanCityService.getCityById(id);

        if (city == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(city);
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
