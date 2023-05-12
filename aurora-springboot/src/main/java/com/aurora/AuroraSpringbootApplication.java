package com.aurora;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan({"com.aurora.mapper","com.aurora.newMeans.*.mapper"})
//@MapperScan("com.aurora.mapper")
@Slf4j
public class AuroraSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuroraSpringbootApplication.class, args);
        log.info("\n---------------------------------------------------------------------\n"+
                "接口Swagger文档: http://localhost:8080/swagger-ui.html"+
                "\n----------------------------------------------------------------------");

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
