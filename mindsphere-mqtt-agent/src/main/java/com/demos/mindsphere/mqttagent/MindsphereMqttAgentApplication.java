package com.demos.mindsphere.mqttagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * A Spring Boot Microservice that acts as a MQTT client to ingest data to MindSphere
 * 
 * @author Amar Vemanaboyina 
 * @version 1.0
 *
 *
 */
@SpringBootApplication
public class MindsphereMqttAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindsphereMqttAgentApplication.class, args);
	}

}
