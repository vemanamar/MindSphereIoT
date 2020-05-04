package com.demos.mindsphere.mqttpublisher;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration properties for MQTT Client configuration.
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
    
	/**
	 * Flag for automatic reconnect option for MQTT connection
	 */
	private boolean automaticReconnect;

	/**
	 * Flag for clean session for MQTT connection
	 */
	private boolean cleanSession;

	/**
	 * Value for connection timeout in seconds
	 */
	private int connectionTimeout;

	/**
	 * Unique MQTT Client Id
	 */
	private String clientId;

	/**
	 * Host name of the MQTT Broker
	 */
	private String hostname;

	/**
	 * Port details of MQTT Broker for establishing the connection
	 */
	private int port;
	
	/**
	 * Flag to specify TLS connection enabled or not.
	 */
	private boolean tls;
	
	/**
	 * Certificate Authority file name. Only when {@link #tls} is set to true.
	 * <p>Supports only pem format</p>
	 */
	private String caCertificateFileName;
		
	/**
	 * Client certificate file name. Only when {@link #tls} is set to true.
	 */
	private String clientCertificateFileName;
	
	/**
	 * Client key file name. Only when {@link #tls} is set to true.
	 */
	private String clientKeyFileName;
	
	/**
	 * Client key password. Only when {@link #tls} is set to true.
	 */
	private String clientKeyPassword;



}
