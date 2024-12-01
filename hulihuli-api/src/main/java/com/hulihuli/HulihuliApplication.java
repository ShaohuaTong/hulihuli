package com.hulihuli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HulihuliApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(HulihuliApplication.class, args);
    }

}
