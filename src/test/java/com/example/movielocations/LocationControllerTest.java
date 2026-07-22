package com.example.movielocations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
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

    @MockitoBean                      // 往容器里放一个"假 Repository",Controller 会注入到它
    LocationRepository repository;

    @Test
    void returnsLocationsForMovie() throws Exception {
        when(repository.findByMovie("harry potter"))
                .thenReturn(List.of(new Location("harry potter", "Alnwick Castle", 55.4156, -1.7059)));

        mockMvc.perform(get("/api/locations").param("movie", "harry potter"))
                .andExpect(status().isOk())                      // 期望 200
                .andExpect(jsonPath("$[0].name").value("Alnwick Castle")); //$[0].name = JSON 数组第 0 个元素的 name 字段。
    }
}
