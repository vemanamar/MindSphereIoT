package com.demos.mindsphere.mqttagent;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siemens.mindsphere.sdk.core.MindsphereCredentials;
import com.siemens.mindsphere.sdk.core.RestClientConfig;
import com.siemens.mindsphere.sdk.timeseries.apiclient.TimeSeriesClient;

/**
 * MindSphere Configuration Class
 * 
 * @author Amar Vemanaboyina
 * 
 * @version 1.0
 * 
 *
 */

@Configuration
public class MindSphereConfig {
	
	private static final Logger log = LoggerFactory.getLogger(MindSphereConfig.class);

	@Autowired
	private MindSphereProperties mindsphereProperties;

	@Bean
	public TimeSeriesClient timeSeriesClient() {		
		TimeSeriesClient timeSeriesClient = TimeSeriesClient.builder().mindsphereCredentials(mindsphereCredentials())
				.restClientConfig(restClientConfig()).build();
		return timeSeriesClient;
	}

	@Bean
	public RestClientConfig restClientConfig() {
		RestClientConfig restClientConfig = RestClientConfig.builder().connectionTimeoutInSeconds(100).build();
		return restClientConfig;
	}

	@Bean
	public MindsphereCredentials mindsphereCredentials() {
		
		log.info("MindSphere Client Id : " + mindsphereProperties.getClientId());
		log.info("MindSphere Client Secret : " + mindsphereProperties.getClientSecret());
		log.info("MindSphere Tenant : " + mindsphereProperties.getTenant());
		MindsphereCredentials credentials = null;
		
		if(mindsphereProperties.isTechnicalCredentials()) {
			log.info("Using Technical User Credentials ...");
			credentials = MindsphereCredentials.tenantCredentialsBuilder()
					.clientId(mindsphereProperties.getClientId())
					.clientSecret(mindsphereProperties.getClientSecret())
					.tenant(mindsphereProperties.getTenant()).build();
			
			log.info("ClientId : " + credentials.getTenantCredential().getClientId());
			log.info("ClientSecret : " + credentials.getTenantCredential().getClientSecret());
			log.info("Tenant : " + credentials.getTenantCredential().getTenant());
			
			
		}else {
			
			log.info("Using Application User Credentials ...");
			credentials = MindsphereCredentials.appCredentialsBuilder()
					.appName(mindsphereProperties.getAppName())
					.appVersion(mindsphereProperties.getAppVersion())
					.keyStoreClientId(mindsphereProperties.getClientId())
					.keyStoreClientSecret(mindsphereProperties.getClientSecret())
					.hostTenant(mindsphereProperties.getTenant())
					.userTenant(mindsphereProperties.getTenant())
					.build();
			
			log.info("App Name : " + credentials.getAppCredential().getAppName());
			log.info("App Version : " + credentials.getAppCredential().getAppVersion());
			log.info(" Key Store ClientId : " + credentials.getAppCredential().getKeyStoreClientId());
			log.info("Key Strore ClientSecret : " + credentials.getAppCredential().getKeyStoreClientSecret());
			log.info("Host Tenant : " + credentials.getAppCredential().getHostTenant());
			log.info("User Tenant : " + credentials.getAppCredential().getUserTenant());
		}
		
		
		
		 
		
		
		return credentials;
	}

}
