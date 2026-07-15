package com.nexora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nexora")
public class NexoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexoraApplication.class, args);
    }
}
