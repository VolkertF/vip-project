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
 * The API takes requests in the form of HTTP GET requests. The URI
 * supplied must also follow a certain format that looks like this...
 * 
 *	  http://www.omdbapi.com/?t=Star+Wars&y=1977&plot=long&r=json
 *
 * ...where t denotes the title and y denotes the year. In addition,
 * we can perform a search by using different parameters in our URI.
 * This class contains methods to do both kinds of requests.
 * 
 * @author Cyril Casapao
 */
public class OMDBConnector {

	// The client that makes our API requests
	private CloseableHttpClient client;
	
	// The StringBuilder used to create API requests
	private StringBuilder uriBuilder;
	
	// Constants used to specify request type
	private final String MOVIE_REQUEST;
	private final String SEARCH_REQUEST;
	
	
	/**
	 * Constructor that initializes the HTTP client and request type
	 * constants.
	 */
	public OMDBConnector() {
		client = HttpClients.createDefault();
		uriBuilder = new StringBuilder();
		
		MOVIE_REQUEST = "movie";
		SEARCH_REQUEST = "search";
	}
	
	
	/**
	 * This method closes the connection to the OMDb API. It should
	 * always be called when the request is finished. It will throw
	 * an IOException if something goes wrong when closing the
	 * connection.
	 */
	public void close() throws IOException {
		client.close();
	}
	
	
	/**
	 * This method tries to get information about a specified movie from
	 * the API. It will throw an IOException if something goes wrong.
	 * 
	 * @param title			The name of the movie
	 * @param year 			The year the movie came out
	 * @return String		The response from the API
	 */
	public String requestMovie(String title, String year) throws IOException {
		String formattedUri = buildUri(title, year, MOVIE_REQUEST);
		String apiResponse = makeRequest(formattedUri);
		return apiResponse;
	}
	
	
	/**
	 * This method searches the API for the requested information. It 
	 * 
	 * @param title			The name of the movie
	 * @param year 			The year the movie came out
	 * @return String		The response from the API
	 */
	public String requestSearch(String title, String year) throws IOException {
		String formattedUri = buildUri(title, year, SEARCH_REQUEST);
		String apiResponse = makeRequest(formattedUri);
		return apiResponse;
	}
	
	
	/**
	 * This method takes a user-specified request and converts it into
	 * the format accepted by the OMDb API.
	 * 
	 * @param title 		The title of the movie to find
	 * @param year 			The year the movie came out
	 * @param requestType	The type of request to make
	 * @return String 		A string representing the API request
	 */
	private String buildUri(
			String title,
			String year,
			String requestType
	) 
	{
		// Reinitialize the StringBuilder
		uriBuilder.delete(0, uriBuilder.length());
		uriBuilder.append("http://www.omdbapi.com/");
		
		// Check if the user requested a specific movie. Otherwise, conduct
		// a general search using the supplied parameters.
		if(requestType.equals(MOVIE_REQUEST)) {
			uriBuilder.append("?t=");
		} else {
			uriBuilder.append("?s=");
		}
		
		// Tokenize the title to remove whitespace, then add them to the
		// URI string with pluses in between each token.
		removeWhitespace(title);
		
		// Add the final parameters to the URI based on the request
		if(year != null && !year.isEmpty()) {
			uriBuilder.append("&y=").append(year);
		}
		
		if(requestType.equals(MOVIE_REQUEST)) {
			uriBuilder.append("&plot=long");
		}
		
		uriBuilder.append("&r=json");
		return uriBuilder.toString();
	}
	
	
	/**
	 * This method removes whitespace from the given String and 
	 * replaces them with plus signs so the API accepts it.
	 * 
	 * @param toModify	The  String to remove whitespace from
	 */
	private void removeWhitespace(String toModify) {
		String[] tokens = toModify.split("\\s");
		for(int i = 0; i < tokens.length; i++) {
			if(i != 0) {
				uriBuilder.append("+");
			}
			uriBuilder.append(tokens[i]);
		}																	//one request. And if the request is cached about 30ms.
	}
	
	
	/**
	 * This method sends the request to the API. It throws an
	 * IOException if something went wrong with the request.
	 * 
	 * @param uri		The URI of the API request
	 * @return String	The String representing the JSON response
	 * 					received from the API
	 */
	private String makeRequest(String uri) throws IOException {
		System.out.println("CONNECTOR: asking " + uri);
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		return jsonResponse;		
	}
}