package com.vip.omdb;

import java.lang.StringBuilder;
import java.io.IOException;

import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;


/**
 * This class handles communication with the OMDb API. As such, it
 * only contains methods to connect to and make requests to the API.
 * 
 * @author Cyril Casapao
 */
public class OMDBConnector {

	private static CloseableHttpClient client;
	
	
	/**
	 * Constructor that initializes the HTTP client.
	 */
	public OMDBConnector() {
		client = HttpClients.createDefault();
	}
	
	
	public void close() throws IOException {
		client.close();
	}
	
	
	/**
	 * This method makes the HTTP request to the API. It will throw
	 * an IOException if the request fails.
	 * 
	 * @param uri			The URI string we want to query
	 * @param year 			The year the movie came out
	 * @return String		The response from the API
	 */
	public String makeHttpRequest(String title, String year) throws IOException {
		String formattedUri = buildUri(title, year);
		HttpGet request = new HttpGet(formattedUri);

		HttpResponse response = client.execute(request);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		return jsonResponse;
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
}