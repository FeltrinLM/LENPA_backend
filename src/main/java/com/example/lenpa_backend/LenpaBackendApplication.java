package com.example.lenpa_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LenpaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LenpaBackendApplication.class, args);
    }

}
