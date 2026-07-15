package com.example.movielocations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 告诉 Spring "这个类是用来接请求的"
public class LocationController {

    record Location(String name, double lat, double lng){}

    @GetMapping("/api/locations") // 告诉 Spring 这个路径的 GET 请求归这个方法管
    public List<Location> getLocations(@RequestParam String movie){
        if(movie.equals("god father")){
            return List.of(new Location("西西里", 37.8, 14.2));
        }
        return List.of(
                new Location("东京柏悦酒店", 35.6852, 139.6900),
                new Location("涩谷十字路口", 35.6595, 139.7004)
        );
    }
}
