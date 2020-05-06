package com.demos.mindsphere.mqttpublisher;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.demos.mindsphere.mqttpublisher.model.TimeSeriesData;
import com.demos.mindsphere.mqttpublisher.model.TimeSeriesDataSet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * MQTT Publisher to publish the simulated data to MindSphere.
 * <p>
 * This class reads the data from the data.json file and publishes the data to
 * the MQTT broker that is configured.
 * </p>
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Component
public class MQTTPublisher {

	private static final Logger log = LoggerFactory.getLogger(MQTTPublisher.class);

	@Autowired
	private IMqttClient mqttClient;
	
	@Autowired
	private MqttConnectOptions mqttConnectOptions;

	@Autowired
	private TimeSeriesDataSet timeSeriesDataSet;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ApplicationContext applicationContext;

	private int counter;

	@Scheduled(fixedRateString = "${mindsphere.mqtt.publisher.interval}", initialDelay = 1000)
	public void publishMessages() {

		String topic = "/mindsphere/" + timeSeriesDataSet.getAssetId() + "/" + timeSeriesDataSet.getPropertySetName();

		if (counter >= timeSeriesDataSet.getTimeseriesData().size()) {
			log.info("Data Set ingestion completed!");
			log.info("Shutting Down the Application....");
			SpringApplication.exit(applicationContext, () -> 0);
			System.exit(0);
		}

		TimeSeriesData timeSeriesData = timeSeriesDataSet.getTimeseriesData().get(counter++);
		
		//Set the current time to timeseriesdata in UTC
		String currentTime = Instant.now().toString();
		timeSeriesData.setDateTime(currentTime);

		try {

			log.info("Publishing time series data ...");
			log.info(timeSeriesData.toString());
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload(objectMapper.writeValueAsString(timeSeriesData).getBytes());
			mqttMessage.setQos(0);
			if(mqttClient.isConnected()) {
				mqttClient.publish(topic, mqttMessage);
				log.info("Publishing done!");	
			}else {
				log.info("Reconnecting...");	
				try {
					//mqttClient.reconnect();
					while(true) {
						//log.info("Checking for connection to reset...");
						Instant startTime = Instant.now();
						Duration waitTime = Duration.between(startTime, startTime); //Initially it is 0
						if(mqttClient.isConnected() || (waitTime.getSeconds()  > 30)) {
							//Wait still connected!
							Instant endTime = Instant.now();
							waitTime = Duration.between(startTime, endTime);
							log.info("Time to reconnect ...." + waitTime.getSeconds() + " seconds");
							break;
						}
					}
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				mqttClient.publish(topic, mqttMessage);
				log.info("Publishing done!");					
			}
			

		} catch (JsonProcessingException e) {

			log.error("Error while publishing time series data.", e);

		} catch (MqttPersistenceException e) {

			log.error("Error while publishing time series data.", e);

		} catch (MqttException e) {

			log.error("Error while publishing time series data.", e);
			e.printStackTrace();
		}

	}

}
