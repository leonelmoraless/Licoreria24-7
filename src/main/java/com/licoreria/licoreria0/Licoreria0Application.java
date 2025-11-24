/*
 * Es la que arranca la aplicacion Spring Boot para entrar el local host 8080 definido en
el application.properties
 */
package com.licoreria.licoreria0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync 
@SpringBootApplication 
public class Licoreria0Application {

    public static void main(String[] args) {
        SpringApplication.run(Licoreria0Application.class, args);
    }

}