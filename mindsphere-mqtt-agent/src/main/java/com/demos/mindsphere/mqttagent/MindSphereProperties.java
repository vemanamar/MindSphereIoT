package com.demos.mindsphere.mqttagent;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration Properties for MindSphere
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Configuration
@ConfigurationProperties(prefix = "mindsphere")
@Data
public class MindSphereProperties {
	
	/**
	 * Technical User ClientId. Also it is the keyStoreClientId in case App Credentials are used for accessing MindSphere API.
	 * 
	 */
	private String clientId;
	
	
	/**
	 * Technical User Client Secret. Also it is the keyStoreClientSecret in case App Credentials are used for accessing MindSphere API.
	 * 
	 */
	private String clientSecret;
	
	
	/**
	 * MindSphere Tenant Name. Also it is the host tenant name in case App Credentials are used for accessing MindSphere API.
	 * 
	 */
	private String tenant;
	
	/**
	 * Flag to specify whether technical user credentials for accessing the MindSphere API
	 *  
	 */
	private boolean technicalCredentials;
	
	
	/**
	 * Name of the application for using App credentials for accessing the MindSphere API
	 * 
	 */
	private String appName;
	
	
	/**
	 * Version of the application for using App credentials for accessing the MindSphere API
	 * 
	 */	
	private String appVersion;
	
	
	/**
	 * User Tenant Name for using App credentials for accessing the MindSphere API
	 * 
	 */	
	private String userTenant;
	
	
	
	
	
	

}
