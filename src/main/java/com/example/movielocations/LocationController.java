package com.example.movielocations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 告诉 Spring "这个类是用来接请求的"
public class LocationController {

    private final LocationRepository repository;

    public LocationController(LocationRepository repository){
        this.repository = repository;
    }

    @GetMapping("/api/locations") // 告诉 Spring 这个路径的 GET 请求归这个方法管
    public List<Location> getLocations(@RequestParam String movie){
        return repository.findByMovie(movie);
    }
}
