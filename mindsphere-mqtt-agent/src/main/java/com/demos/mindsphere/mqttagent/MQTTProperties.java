package com.demos.mindsphere.mqttagent;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration properties for MQTT connection
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MQTTProperties {

	private boolean automaticReconnect;

	private boolean cleanSession;

	private int connectionTimeout;

	private String clientId;

	private String hostname;

	private int port;
	
	private boolean tls;
	
	private String caCertificateFileName;
	
	private String clientCertificateFileName;
	
	private String clientKeyFileName;
	
	private String clientKeyPassword;



}
