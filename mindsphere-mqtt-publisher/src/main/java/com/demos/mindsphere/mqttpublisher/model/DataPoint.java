package com.demos.mindsphere.mqttpublisher.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the time series datapoint. A datapoint represents any physical
 * quantity such as temperature, pressure etc which has a unit and measured
 * value at a given point of time.
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 *
 */
@Data
@ToString
public class DataPoint {
	
	/**
	 * Name of the data point.
	 */
	@Setter
	@Getter
	private String name;
	
	
	@Setter
	@Getter
	private String value; 
	 
}
