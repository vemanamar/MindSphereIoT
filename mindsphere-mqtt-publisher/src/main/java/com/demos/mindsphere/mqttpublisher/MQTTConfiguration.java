package com.demos.mindsphere.mqttpublisher;

import java.io.File;
import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.demos.mindsphere.mqttpublisher.model.TimeSeriesDataSet;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring Configuration for MQTT Client Setup.
 * 
 * @author Amar Vemanaboyina
 * @since 1.0
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
			mqttClient.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					log.info("CallBack====> Message Arrived");
					
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					log.info("CallBack====> Message Delivery Completed");
					
				}
				
				@Override
				public void connectionLost(Throwable cause) {
					log.info("CallBack====> Connection is lost");					 
					
				}
			});
			
			mqttClient.connect(mqttConnectOptions());
			// mqttClient.subscribeWithResponse("/topic/amar/test", mqttService);

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
		mqttConnectOptions.setKeepAliveInterval(60);
		
		
		
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

	@Bean
	public TimeSeriesDataSet getTimeseriesDataSet(){
		
		String workingDir = System.getProperty("user.dir");
		log.info("Current Working Directory : " + workingDir);

		// Get the file.
		log.info("Processing the data file ...." + workingDir);
		File dataFile = new File(workingDir + "/data.json");
		ObjectMapper objectMapper = objectMapper();
		TimeSeriesDataSet timeSeriesDataSet =  null;
		try {
			timeSeriesDataSet = objectMapper.readValue(dataFile, TimeSeriesDataSet.class);
			log.info("Processing of the data file completed successfully.");
		} catch (IOException e) {
			log.error("Error processing the data.json file.", e);			 
		}
		
		return timeSeriesDataSet;

	}

}
