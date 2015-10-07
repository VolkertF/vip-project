package com.extractor;

import com.vip.attributes.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;


/**
 * This class extracts information from an OMDb request.
 * 
 * @author Cyril Casapao
 */
public class InfoExtractor {
	
	
	/**
	 * Constructor method.
	 * @TODO: Make it work.
	 */
	public InfoExtractor() {
		
	}
	
	
	/**
	 * This method takes the information received from an OMDb request
	 * and creates a new Movie object.
	 * 
	 * @param Map		The Map holding the information from a request
	 */
	public void extract(Map<String, String> infoMap) {
		Movie movie = new Movie();
		
		movie.setYearReleased(Integer.parseInt(infoMap.get("Year")));
		movie.setDirector(infoMap.get("Director"));
//		movie.setWriter(infoMap.get("Writer"));
		movie.setGenre(infoMap.get("Genre"));
	}
	
	/**
	 * This method takes the JSON response we receive from OMDb and turns
	 * it into a Java Map. This allows us to work easily using a native
	 * Java object while also preserving the useful key-value relationship
	 * inherent in JSON.
	 * 
	 * @param jsonString 	The JSON information we receive from OMDb
	 * @return Map			A map holding key-value pairs of movie info
	 */
	public Map<String, String> deserializeJson(String jsonString) {
		Gson gson = new Gson();
		
		// The fromJson() method takes a JSON string as its first parameter
		// and a Class as a second. There's no clean simple way to get the
		// Map type so we have to use the ugly method seen here.
		Map<String, String> information =
			gson.fromJson(
				jsonString,
				new TypeToken<Map<String, String>>(){}.getType()
			);
		
		return information;
	}
	
}
