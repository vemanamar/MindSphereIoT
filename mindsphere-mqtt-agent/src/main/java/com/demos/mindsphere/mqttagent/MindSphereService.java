package com.demos.mindsphere.mqttagent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siemens.mindsphere.sdk.core.exception.MindsphereException;
import com.siemens.mindsphere.sdk.timeseries.apiclient.TimeSeriesClient;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;

/**
 * Service class to ingest data to MindSphere Asset
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Service
public class MindSphereService {

	private static final Logger log = LoggerFactory.getLogger(MindSphereService.class);

	@Autowired
	private TimeSeriesClient timeSeriesClient;

	public void ingestTimeSeries(String assetId, String propertySet, Timeseries timeSeries) {
		List<Timeseries> timeseriesList = new ArrayList<>();
		timeseriesList.add(timeSeries);
		try {
			timeSeriesClient.putTimeseries(assetId, propertySet, timeseriesList);
			log.info("Successfully ingested data for " + assetId +  " with propertyset - " + propertySet);
			log.info("Timeseries Data : " + timeSeries.toString());			
		} catch (MindsphereException ex) {
			log.error("Unable to ingest data for " + assetId +  " with propertyset - " + propertySet, ex);
		}

	}

}
