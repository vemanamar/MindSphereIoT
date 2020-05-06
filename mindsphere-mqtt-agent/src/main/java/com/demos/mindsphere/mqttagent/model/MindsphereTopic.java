package com.demos.mindsphere.mqttagent.model;

import com.demos.mindsphere.mqttagent.exceptions.InvalidMindSphereTopicFoundException;


/**
 * Represents the MindSphere Topic received from the MQTT broker and provides handy methods
 * to extract Asset details, propertyset name from the received from the topic.
 * 
 * 
 * @author Amar Vemenaboyina
 * @version 1.0
 * 
 *
 */
public class MindsphereTopic {
	
	/**
	 * Id of MindSphere Asset
	 * 
	 */
	private String assetId;
	
	
	/**
	 * PropertySetName of the asset.
	 * 
	 */
	private String propertySetName;
	
	public MindsphereTopic(String mqttTopic) throws InvalidMindSphereTopicFoundException {
		
		String[] tokens = mqttTopic.split("/");
		if(tokens.length != 4) {
			String message = "Topic string - " + mqttTopic + " is a valid Mindsphere Topic!";
			throw new InvalidMindSphereTopicFoundException(message);
		}
		
		this.assetId =  tokens[2];
		this.propertySetName = tokens[3];
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getPropertySetName() {
		return propertySetName;
	}

	public void setPropertySetName(String propertySetName) {
		this.propertySetName = propertySetName;
	}
	
	

}
