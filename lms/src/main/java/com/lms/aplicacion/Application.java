package com.lms.aplicacion;

import com.lms.controlador.RDFController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan("com.lms")
// incluye los paquetes en los que obtener fuentes
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) throws IOException {
        // Arrancar aplicacion
        RDFController.init();
        SpringApplication.run(Application.class, args);
    }
}