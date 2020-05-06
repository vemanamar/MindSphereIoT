package com.demos.mindsphere.mqttagent.model;

import java.util.ArrayList;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Collection of TimeSeriesData.
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Data
@ToString
public class TimeSeriesDataSet {
	
	@Getter
	@Setter
	private String assetId;
	
	@Getter
	@Setter	
	private String propertySetName;
	
	@Getter
	@Setter
	private ArrayList<TimeSeriesData> timeseriesData;
	
	

}
