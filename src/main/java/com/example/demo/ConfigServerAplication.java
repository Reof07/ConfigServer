package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Class ConfigServerAplication:
 * Punto de entrada de nuestro servidor de configuración
 * Spring Cloud Config Server.
 * 
 * @author Ramon Ojeda.
 */
/*
 * @EnableConfigServer: Es usada para insertar el servidor
 * en una aplicación Spring Boot.
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerAplication {

	public static void main (String[] args) {
		SpringApplication.run(ConfigServerAplication.class, args);
	}
}
