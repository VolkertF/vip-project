package com.vip.omdb;

import java.util.Scanner;
import java.util.Map;
import java.lang.StringBuilder;
import java.io.IOException;

import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class connects to the OMDb API.
 * 
 * @author Cyril Casapao
 */
public class OMDBConnector {

	private static CloseableHttpClient client;
	private static Scanner scan;
	
	
	/**
	 * Constructor.
	 */
	public OMDBConnector() {
		client = HttpClients.createDefault();
		scan = new Scanner(System.in);

	}
	
	
	/**
	 * Here is the main method. It runs a dialogue loop that allows us
	 * to interactively query the OMDb API. As an example, try entering
	 * "star wars" as the title and "1977" as a year. Or maybe "Up" as
	 * a title and "2009" as a year.
	 */
	public static void main(String[] args) {
		OMDBConnector connector = new OMDBConnector();
		
		System.out.println("Welcome to the OMDb API tester! To quit, " +
				"type EXIT when prompted for a year.");
		
		while(true) {
			System.out.println("Enter a movie title: ");
			String title = scan.nextLine();
			
			System.out.println("Enter the year the movie came out: ");
			String year = scan.nextLine();
			
			if(year.equals("EXIT")) {
				break;
			}
			
			String uri = connector.buildUri(title,year);
			connector.makeHttpRequest(uri);
		}
		
		scan.close();
		try {
			client.close();
		} catch(IOException e) {
			System.out.println("Error closing HTTP client.");
			e.printStackTrace();
		}
		
		System.out.println("Goodbye!");
	}
	
	
	/**
	 * This method takes the title and year that the user inputs and combines
	 * them into a format accepted by the OMDb api. Namely, they must follow
	 * a format like this...
	 * 
	 *	  http://www.omdbapi.com/?t=Star+Wars&y=1977&plot=long&r=json
	 *
	 * ...where t denotes the title and y denotes the year. For some
	 * reason, some movies seem to return incomplete information if
	 * a year isn't specified. There is probably a way around this
	 * but this will do for the feasibility study.
	 * 
	 * @param title 	The title of the movie to find
	 * @param year 		The year the movie came out
	 * @return String 	A string representing the API request
	 * 
	 * @TODO Find a way to get around needing the year parameter.
	 */
	private String buildUri(String title, String year) {
		StringBuilder uri = new StringBuilder("http://www.omdbapi.com/?t=");
		
		// Tokenize the title to remove whitespace, then add them to the
		// URI string with pluses in between each token.
		String[] titleTokens = title.split("\\s");
		for(int i = 0; i < titleTokens.length; i++) {
			if(i != 0) {
				uri.append("+");
			}
			uri.append(titleTokens[i]);
		}
		
		// Add the final parameters to the URI
		uri.append("&y=").append(year).append("&plot=long&r=json");
		return uri.toString();
	}
	
	
	/**
	 * This method makes the HTTP request to the API.
	 * 
	 * @param uri		The URI string we want to query
	 */
	public void makeHttpRequest(String uri) {
		
		// Initialize a request with our complete URI
		double startTime = System.currentTimeMillis();
		System.out.println("Checking " + uri);
		HttpGet request = new HttpGet(uri);
		
		// Make the request to the API and deserialize the response
		try {
			HttpResponse response = client.execute(request);
			String jsonString = EntityUtils.toString(response.getEntity());
			deserializeJson(jsonString);
		} catch(IOException e) {
			System.out.println("Something went wrong.");
			e.printStackTrace();
		}
		double endTime = System.currentTimeMillis();
		System.out.println("The enquiry took " + (endTime - startTime) + "ms");	//This will took for one about half a second for
																				//one request. And if the request is cached about 30ms.
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
	private Map<String, String> deserializeJson(String jsonString) {
		Gson gson = new Gson();
		
		// The fromJson() method takes a JSON string as its first parameter
		// and a Class as a second. There's no clean simple way to get the
		// Map type so we have to use the ugly method seen here.
		Map<String, String> information =
			gson.fromJson(
				jsonString,
				new TypeToken<Map<String, String>>(){}.getType()
			);
		
		// Print the map info to make sure we got it
		for(Map.Entry<String, String> entry : information.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue();
			System.out.println(key + ": " + value);
		}
		
		return information;
	}
}