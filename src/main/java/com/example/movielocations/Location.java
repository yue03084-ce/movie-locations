package com.example.movielocations;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity                // 声明:这是个实体类,对应一张表(表名默认=类名小写)
public class Location {

    @Id                                                  // 声明:下面这个字段是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主键值由数据库自增生成(1,2,3...)
    private Long id;

    private String movie;   // 没标注解的字段,自动映射成同名列
    private String name;
    private double lat;
    private double lng;

    protected Location() {}   // JPA 规范要求:必须有无参构造器(框架从数据库读数据时,
    // 先无参造出空对象再逐个填字段)。protected=只给框架用

    public Location(String movie, String name, double lat, double lng) {
        this.movie = movie;   // 给你自己代码用的构造器
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
    // getter:框架和 JSON 序列化都靠它读字段值
    // IDE 生成:光标放类里 → 右键 → Generate → Getter → 全选

    public double getLng() {
        return lng;
    }

    public Long getId() {
        return id;
    }

    public String getMovie() {
        return movie;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

}
