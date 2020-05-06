package com.demos.mindsphere.mqttagent.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for the application
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@RestController
public class MQTTPublisherController {
	
	
	@GetMapping("/")
	public String about() {
		return "MindSphere MQTT Agent";
	}
	
	

}
