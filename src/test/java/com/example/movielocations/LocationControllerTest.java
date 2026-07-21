package com.example.movielocations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 刚才你验证接口的方式是：启动应用 → 开浏览器 → 肉眼看 JSON。测试就是把这套人工检查写成代码，
 * 以后每次改代码跑一下 mvn test，几秒钟自动验证所有接口（一个可访问的URL）没被改坏。
 */
@WebMvcTest(LocationController.class)
public class LocationControllerTest {
    @Autowired
    MockMvc mockMvc; //假浏览器

    @Test
    void returnsLocationsForMovie() throws Exception {
        mockMvc.perform(get("/api/locations").param("movie", "lost in tokyo"))
                .andExpect(status().isOk())                      // 期望 200
                .andExpect(jsonPath("$[0].name").value("东京柏悦酒店")); //$[0].name = JSON 数组第 0 个元素的 name 字段。
    }
}
