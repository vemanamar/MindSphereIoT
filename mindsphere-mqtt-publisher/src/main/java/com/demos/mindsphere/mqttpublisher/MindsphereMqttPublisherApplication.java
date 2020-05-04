package com.demos.mindsphere.mqttpublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * MQTT Client that acts as a publisher to publish data to 
 * Mosquitto MQTT broker hosted as cloud service. Supports TLS connection.
 * 
 * 
 * @author Amar Vemanaboyina
 * @see MQTTConfiguration
 * @version 1.0
 *
 */
@SpringBootApplication
@EnableScheduling
public class MindsphereMqttPublisherApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(MindsphereMqttPublisherApplication.class);

	@Autowired
	private ObjectMapper objectMapper;

	public static void main(String[] args) {
		SpringApplication.run(MindsphereMqttPublisherApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {		
		log.info("Ingestion process started ....");
	 
	}

}
