package com.demos.mindsphere.mqttagent.converters;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.demos.mindsphere.mqttagent.model.DataPoint;
import com.demos.mindsphere.mqttagent.model.TimeSeriesData;
import com.siemens.mindsphere.sdk.timeseries.model.Timeseries;


/**
 * Converter class to convert {@link com.demos.mindsphere.mqttagent.model.TimeSeriesData} to 
 * {@link com.siemens.mindsphere.sdk.timeseries.model.Timeseries}.
 * 
 * @author Amar Vemanaboyina
 * 
 * @version 1.0
 * 
 *
 */
@Component
public class TimeSeriesConverter {
	
	
	public Timeseries toTimeSeries(TimeSeriesData timeSeriesData) {		
		Timeseries timeseries = new Timeseries();
		timeseries.setTime(timeSeriesData.getDateTime());
		
		//Create the dataMap
		Map<String,Object> dataMap = timeseries.getFields();
		for (DataPoint dataPoint : timeSeriesData.getDataPoints()) {
			dataMap.put(dataPoint.getName(), dataPoint.getValue());
		} 
		return timeseries;
	}
	
	

}
