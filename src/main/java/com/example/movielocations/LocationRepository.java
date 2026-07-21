package com.example.movielocations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//这个接口会动态生成一个类不需要手动实现，然后这个类有很多现成方法和findByMovie这个方法
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByMovie(String movie);
}
