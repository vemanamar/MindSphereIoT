# mindsphere-mqtt-agent
A Spring Boot based MQTT Client that runs as an application in MindSphere. It is subscribed to MQTT broker through TLS secured connection. This MindSphere application
receives timeseries data a payload and ingests data accordingly to relevant asset in MindSphere. For this service, you need to have relevant publisher to notify. Please refer to
mindsphere-mqtt-publisher project within this repository.

## Overview
This application is based on Spring Boot. To setup this application, import the mindsphere-mqtt-agent to your local development environment. 
This application uses the Eclipse Paho Library for receiving data from Eclipse Mosquitto broker, `test.mosquitto.org` which is publicly hosted and holds support for TLS communication. 
You can alternatively change the broker settings in the `application.properties`. Refer to section [Configuring MQTT](#configuring-mqtt) for more details. Please refer to [Prerequisites](#prerequisites)
section before setting up the application.

### Prerequisities
The following list specifies the prerequisites one should have before deploying the application on to MindSphere
- One should have either MindSphere Developer Plan from development perspective
- Mindsphere Java SDK needs to be downloaded from the [MindSphere Java JDK](https://developer.mindsphere.io/resources/mindsphere-sdk-java-v2/index.html). This needed to be installed
as dependency to your local maven repository. For this use case I have used MindSphere Java SDK V2.
- An asset with relevant aspect(s) in MindSphere
- For TLS communication iwith MQTT broker, create relevant certificate files. Refer to section [Configuring Certificates](#configuring-certificates).
- Configure MQTT connection settings. Refer to section [Configuring MQTT](#configuring-mqtt)
- Cloud Foundry CLI
- MindSphere Cloud Foundry Access

## Configuring Certificates
To establish TLS communication with the MQTT broker, you should have the following certificate files:
1. Certificate Authority(CA) file, which is provided by relevant MQTT broker provider. This application supports only PEM file format.
2. Client Key
3. Client Certificate

Files 2 and 3 needs to be created by you for your relevant client. Refer to the [link](https://test.mosquitto.org/), where you can download 
directly the CA file for Mosquitto broker. For Mosquitto broker, you can also refer to this [link](https://test.mosquitto.org/ssl/) for creation of client key and client certificate. 
All the above mentioned files needs to be placed in the resources folder of the build and accordingly configured in `application.properties`. 
Refer to section [Configuring MQTT](#configuring-mqtt) for more details.

## Configuring MQTT
The `application.properties` file provides properties using which you can configure the settings for your MQTT broker connection. 

**mqtt.automaticReconnect** - Flag to set whether automatic reconnection to the MQTT broker needs to be setup when there is a connectivity loss. Allowable entries are true or false.

**mqtt.cleanSession** - To determine whether the connection needs to be persistent or publishing only. Setting clean session to true will make the client, 
publishing only and no session information is persisted. Allowable entries are true or false.

**mqtt.connectionTimeout** - Connection timeout for your MQTT broker connectivity. Needs to be specified in seconds

**mqtt.clientId** - Unique Client Id for your publisher

**mqtt.hostname** - Hostname of your MQTT broker. For Eclipse Mosquitto it is `test.mosquito.org`

**mqtt.port** - Port for your MQTT broker communication, usually specified by the broker provider. Please be informed that the port 
would vary based on the connection type (TLS or websockets or tcp). In case of `test.mosquito.org`, it is 8884 for TLS communication.

**mqtt.tls** - If set to true, uses certificates for secured communication with the MQTT broker. If false, default tcp communication is established. 
I strongly suggest to use TLS based solutions for Industrial IoT solutions.

**mqtt.caCertificateFileName** - Certificate Authority filename. It is provided by the MQTT broker provider. This file has to be copied to your `resources` folder. 
This application supports only PEM format.

**mqtt.clientCertificateFileName** - Client certificate file name.This file has to be copied to your `resources` folder.

**mqtt.clientKeyFileName** -  Client key file name. This file has to be copied to your `resources` folder.

**mqtt.clientKeyFileName** - Client key password.

## Configuring MindSphere
The `application.properties` file provides properties using which you can configure the settings for MindSphere.

**mindsphere.technicalCredentials** - If set true, the application uses technical user credentials for accessing the MindSphere API, Else if false it uses Application Credentials. 

**mindsphere.clientId** - If technicalCredentials is set to true this is considered for clientId. For Application credentials, this acts as KeyStore ClientId.

**mindsphere.clientSecret** - If technicalCredentials is set to true this is considered for clientSecret. For Application credentials, this acts as KeyStore ClientSecret.

**mindsphere.tenant** -  If technicalCredentials is set to true this is considered for tenant. For Application credentials this acts as Host tenant name.

**mindsphere.app-name** - Only used in the case of Application Credentials and represents name of the application.

**mindsphere.app-version** - Only used in the case of Application Credentials and represents version of the application.

**mindsphere.user-tenant** - Only used in the case of Application Credentials and represents name of the user tenant.

In the case of developer tenant, the host tenant name and user tenant name as same if you are using application credentials. Though for this example these credentials are configured as part of your 
application properties, the ideal way is to set these as cloud foundry environment variables and access them accordingly in your application. To know more about MindSphere associated concepts please refer 
to section [References](#references).  

## MQTT Payload amd Topic

The payload received will be of type TimeSeriesData. The structure of TimeSeriesData is as follows:

```
[		
        
	    {	    
		  "dateTime" : "05-06T02:10:23Z",
	      "dataPoints" : [	      
	           {
	              "name" : "Humidity",
	              "value" : 20.0	           
	           },	           
	            {
	              "name" : "Temperature",
	              "value" : 20.0	           
	           }	      
	      ] 
	    },
	    {
          "dateTime" : "05-06T02:10:28Z",	    
	      "dataPoints" : [
	           {
	              "name" : "Humidity",
	              "value" : 25.0
	           
	           },	           
	            {
	              "name" : "Temperature",
	              "value" : 10.0
	           
	           }	      
	      ]
	    }
	]
	
```
A TimeSeriesData element consists of two properties: dateTime (UTC Format) and dataPoints (Which is collection of DataPoint). A DataPoint technically is a measurable quantity or 
characteristic of an asset. For example temperature or pressure of a pump. One has to ensure that the names and values specified should match accordingly the name and type of 
the variable specified for the aspect (propertysetName) for respective asset in MindSphere. To know more about MindSphere and assets modelling please refer 
to section [References](#references).  This application is subscribed to topic of the below format. 

```
/mindsphere/assetId/propertySetName

```
In wild card form,
```
/mindsphere/#

```
Once it receives notification, this application will process TimeSeriesData to MindSphere specific TimeSeries data model and then ingest the data to relevant asset and associated propertyset 
asynchronously using an asynchronous task executor.

## Deploy the application
Before deploying the application into MindSphere, I would recommend you to refer to point 3 of section [References](#references). As a next step edit the `manifest.yml` file with your application name.
As a part of application creation process in MindSphere, use `/**` as endpoint for your application component.

## Results
To view the results, ensure the application is deployed successfully in MindSphere platform. Run your mindsphere-mqtt-publisher application locally. Open `FleetManager` application in MindSphere and select
relevant aspect and datetime range to view the trends. Alternatively, if you are familiar with MindSphere APIs you can use PostMan to view the results. Refer to point 6 of section [References](#references). 

 
## References
1. [About MindSphere](https://siemens.mindsphere.io/en)
2. [MindSphere Assets](https://documentation.mindsphere.io/resources/html/asset-manager/en-US/index.html)
3. [Develop, Deploy, Register and Release an application on MindSphere](https://developer.mindsphere.io/howto/howto-develop-and-register-an-application.html)
4. [MindSphere Technical User Credentials](https://developer.mindsphere.io/howto/howto-local-development.html#create-service-credentials)
5. [MindSphere Application Credentials](https://developer.mindsphere.io/howto/howto-selfhosted-api-access.html#application-credentials)
6. [Accessing MindSphere APIs during Local Development](https://developer.mindsphere.io/howto/howto-local-development.html)
7. [Eclipse Mosquitto Broker](https://test.mosquitto.org/)
8. [About MQTT](https://dzone.com/refcardz/getting-started-with-mqtt?chapter=1)

 
