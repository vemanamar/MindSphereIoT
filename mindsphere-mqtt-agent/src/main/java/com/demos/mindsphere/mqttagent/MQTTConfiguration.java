package com.demos.mindsphere.mqttagent;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring Configuration class for MindSphere
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Configuration
public class MQTTConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MQTTConfiguration.class);

	@Autowired
	private MQTTProperties mqttProperties;

	@Autowired
	private MQTTSecurityHelper securityHelper;
	
	@Autowired
	private MQTTService mqttService;

	@Bean
	public IMqttClient mqttClient() {

		IMqttClient mqttClient = null;

		try {

			String mqttURI = null;
			if (mqttProperties.isTls()) {
				mqttURI = String.format("ssl://%s:%d", mqttProperties.getHostname(), mqttProperties.getPort());
			} else {
				mqttURI = String.format("tcp://%s:%d", mqttProperties.getHostname(), mqttProperties.getPort());
			}

			mqttClient = new MqttClient(mqttURI, mqttProperties.getClientId(), new MemoryPersistence());
			mqttClient.connect(mqttConnectOptions());
			mqttClient.subscribeWithResponse("/mindsphere/#", mqttService);

		} catch (MqttException e) {
			log.error("Error initiating MQTT Client.", e);
		} catch (Exception e) {
			log.error("Error initiating MQTT Client.", e);
		}

		log.info("MQTT Client is successfully initialized!");

		return mqttClient;
	}

	@Bean
	public MqttConnectOptions mqttConnectOptions() throws Exception {
		MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
		mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		mqttConnectOptions.setAutomaticReconnect(mqttProperties.isAutomaticReconnect());
		mqttConnectOptions.setCleanSession(mqttProperties.isCleanSession());
		mqttConnectOptions.setConnectionTimeout(mqttProperties.getConnectionTimeout());

		if (mqttProperties.isTls()) {
			log.info("Identified TLS being enabled.");
			// Set the SSL Factory
			SSLSocketFactory socketFactory = null;
			String caCertificateFileName = mqttProperties.getCaCertificateFileName();
			String clientCertificateFileName = mqttProperties.getClientCertificateFileName();
			String clientKeyFileName = mqttProperties.getClientKeyFileName();
			socketFactory = securityHelper.createSocketFactory(caCertificateFileName, clientCertificateFileName,
					clientKeyFileName);
			mqttConnectOptions.setSocketFactory(socketFactory);

		}
		return mqttConnectOptions;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
