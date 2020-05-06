package com.demos.mindsphere.mqttagent.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;


/**
 * Represents a collection of data points at a given timestamp. This class
 * currently supports only UTC format of time format.
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 *
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSeriesData {
	
	
	private String dateTime; //UTC format
	
	private ArrayList<DataPoint> dataPoints;

}
