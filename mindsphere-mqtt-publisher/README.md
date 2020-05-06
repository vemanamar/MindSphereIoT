# mindsphere-mqtt-publisher
A Spring Boot based MQTT Client to simulate time series data to a MQTT broker. It is assumed that for the MQTT broker, a MQTT client is attached to ingest data to MindSphere (please refer to project mindsphere-mqtt-agent for more details). This application implements CommandLineRunner interface of SpringBoot and runs as a standalone application.

## Overview
This application is based on Spring Boot. To setup this application, import the mindsphere-mqtt-publisher to your local development environment. This application uses the Eclipse Paho Library for messaging data to Eclipse Mosquitto broker, `test.mosquitto.org` which is publicly hosted and holds support for TLS communication. You can alternatively change the broker settings in the `application.properties`. Refer to section [Configuring MQTT](#configuring-mqtt) for more details.

The following are the steps you need to follow before building and executing the application:
- An asset with relevant aspect(s) in MindSphere
- If TLS communication is enabled with MQTT broker, create relevant certificate files. Refer to section [Configuring Certificates](#configuring-certificates).
- Create data.json file which holds your simulated data. Refer to section [data.json](#data.json)
- Configure MQTT connection settings. Refer to section [Configuring MQTT](#configuring-mqtt)

## Configuring Certificates
To establish TLS communication with the MQTT broker, you should have the following certificate files:
1. Certificate Authority(CA) file, which is provided by relevant MQTT broker provider. This application supports only PEM file format.
2. Client Key
3. Client Certificate

Files 2 and 3 needs to be created by you for your relevant client. Refer to the [link](https://test.mosquitto.org/), where you can download directly the CA file for Mosquitto broker. For Mosquitto broker, you can also refer to this [link](https://test.mosquitto.org/ssl/) for creation of client key and client certificate. All the above mentioned files needs to be placed in the resources folder of the build and accordingly configured in `application.properties`. Refer to section [Configuring MQTT](#configuring-mqtt) for more details.

## data.json
The file data.json contains the simulated data that would be sent as a payload to the MQTT broker. The structure of the json is as follows:

| - assetId

| - propertySetName

| - array of Timeseries Data


where,

assetId - Id of the asset in MindSphere to which data needs to be ingested

propertySetName - The aspect of the asset to which the data is mapped and ingested

Array of TimeSeriesData - This is collection of TimeSeriesData. 


The structure of TimeSeriesData is as follows:
```
[		
	    {	    
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
A TimeSeriesData element consists of two properties: dateTime (UTC Format) and dataPoints (Which is collection of DataPoint). A DataPoint technically is a measurable quantity or characteristic of an asset. For example temperature or pressure of a pump. One has to ensure that the names and values specified should match accordingly the name and type of the variable specified for the aspect (propertysetName) for respective asset in MindSphere. To know more about MindSphere and assets modelling please refer to section [References](#references). An example of data.json is available in mindsphere-mqtt-publisher folder. In the data.json, the dateTime property is not set as this would be automatically set by application while sending the data to MQTT broker considering the time at which data is being sent. The application uses the following format for the topic to notify MQTT broker:
```
/mindsphere/assetId/propertySetName

```
Further each TimeSeriesData element is notified based on the interval specified by `mindsphere.mqtt.publisher.interval` property in application.properties file. The value for interval needs to be specified in milliseconds. The application ends once all the elements are notified accordingly. In the default implementation, the application ends after ingesting two datapoints each with 5 second interval.

## Configuring MQTT
The `application.properties` file provides properties using which you can configure the settings for your MQTT broker connection. 

**mqtt.automaticReconnect** - Flag to set whether automatic reconnection to the MQTT broker needs to be setup when there is a connectivity loss. Allowable entries are true or false.

**mqtt.cleanSession** - To determine whether the connection needs to be persistent or publishing only. Setting clean session to true will make the client, publishing only and no session information is persisted. Allowable entries are true or false.

**mqtt.connectionTimeout** - Connection timeout for your MQTT broker connectivity. Needs to be specified in seconds

**mqtt.clientId** - Unique Client Id for your publisher

**mqtt.hostname** - Hostname of your MQTT broker. For Eclipse Mosquitto it is `test.mosquito.org`

**mqtt.port** - Port for your MQTT broker communication, usually specified by the broker provider. Please be informed that the port 
would vary based on the connection type (TLS or websockets or tcp). In case of `test.mosquito.org`, it is 8884 for TLS communication.

**mqtt.tls** - If set to true, uses certificates for secured communication with the MQTT broker. If false, default tcp communication is established. I strongly suggest to use TLS based solutions for Industrial IoT solutions.

**mqtt.caCertificateFileName** - Certificate Authority filename. It is provided by the MQTT broker provider. This file has to be copied to your `resources` folder. This application supports only PEM format.

**mqtt.clientCertificateFileName** - Client certificate file name.This file has to be copied to your `resources` folder.

**mqtt.clientKeyFileName** -  Client key file name. This file has to be copied to your `resources` folder.

**mqtt.clientKeyFileName** - Client key password.

 
## References
1. [About MindSphere](https://siemens.mindsphere.io/en)
2. [MindSphere Assets](https://documentation.mindsphere.io/resources/html/asset-manager/en-US/index.html)
3. [Eclipse Mosquitto Broker](https://test.mosquitto.org/)
4. [About MQTT](https://dzone.com/refcardz/getting-started-with-mqtt?chapter=1)

 
