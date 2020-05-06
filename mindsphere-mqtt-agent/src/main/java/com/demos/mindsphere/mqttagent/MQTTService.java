package com.demos.mindsphere.mqttagent;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.demos.mindsphere.mqttagent.converters.TimeSeriesConverter;
import com.demos.mindsphere.mqttagent.exceptions.InvalidMindSphereTopicFoundException;
import com.demos.mindsphere.mqttagent.model.MindsphereTopic;
import com.demos.mindsphere.mqttagent.model.TimeSeriesData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

/**
 * Service class that acts a MQTT Message Listener. If the received message is of type {@link #com.demos.mindsphere.mqttagent.model.TimeSeriesDataSet}, this
 * service class invokes an asynchronous task to ingest data to MindSphere.
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Service
public class MQTTService implements IMqttMessageListener {
	
	private static final Logger log = LoggerFactory.getLogger(MQTTService.class);
	
	@Autowired 
	private ObjectMapper objectMapper;
	
	@Autowired 
	private MindSphereService mindSphereService;	
	
	@Autowired
	private TimeSeriesConverter timeSeriesConverter;
	
	@Autowired
	private TaskExecutor taskExecutor;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {		
		log.info("Received Message on topic - " + topic);
		
		try {
				MindsphereTopic mindsphereTopic = new MindsphereTopic(topic);
				TimeSeriesData timeSeriesData = objectMapper.readValue(message.getPayload(), TimeSeriesData.class);			
				taskExecutor.execute(new IngestDataTask(mindsphereTopic.getAssetId(), mindsphereTopic.getPropertySetName(), timeSeriesData));
				//processMessage(mindsphereTopic, message);
				
		}catch(InvalidMindSphereTopicFoundException ex) {
			log.error(ex.getMessage(), ex);
		} 
		
	}
	
	
	private class IngestDataTask implements Runnable{
		
		private TimeSeriesData timeseriesData;
		private String assetId;
		private String propertySetName;
		
		public IngestDataTask(String assetId,String propertySetName, TimeSeriesData timeseriesData) {
			this.assetId = assetId;
			this.propertySetName = propertySetName;
			this.timeseriesData = timeseriesData;
		}

		@Override
		public void run() {
			try {
				log.info("Ingesting data to MindSphere....");				
				log.info(timeseriesData.toString());
				Timeseries timeseries = timeSeriesConverter.toTimeSeries(timeseriesData);
				mindSphereService.ingestTimeSeries(assetId, propertySetName, timeseries);
				log.info("Data ingested to MindSphere successfully....");		
			} catch (Exception e) {
				log.error("Error processing the MQTT message.", e);
			}
			 
		}
		
		
	}
	

//	public void processMessage(MindsphereTopic mindsphereTopic, MqttMessage message) {
//		log.info("Processing Message ....");
//		try {
//			
//			TimeSeriesData timeSeriesData = objectMapper.readValue(message.getPayload(), TimeSeriesData.class);
//			log.info(timeSeriesData.toString());
//			Timeseries timeseries = timeSeriesConverter.toTimeSeries(timeSeriesData);
//			mindSphereService.ingestTimeSeries(mindsphereTopic.getAssetId(), mindsphereTopic.getPropertySetName(), timeseries);
//		} catch (JsonParseException e) {
//			log.error("Error processing the MQTT message.", e);
//		} catch (JsonMappingException e) {
//			log.error("Error processing the MQTT message.", e);
//		} catch (IOException e) {
//			log.error("Error processing the MQTT message.", e);
//		}
//		
//		
//	}
 

}
