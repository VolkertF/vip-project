package vip_project.omdb;

import java.util.Scanner;

import java.lang.StringBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;

/**
 * This class connects to the OMDb API.
 * 
 * @author Cyril Casapao
 * 
 * @TODO Implement JSON parsing and store it in a data structure. I think
 * 		 a map would be a good choice because it preserves the key-value
 * 		 relationship we receive from the API.
 */
public class OMDBConnector {

	private static CloseableHttpClient client;
	private static Scanner scan;
	
	/**
	 * Constructor, yay let's build can you tell I coded this late at night...
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
	 * 
	 * @param args
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
		
		// This has to go in a try-catch because apparently it's 
		// possible to fail when you close the CLOSABLE HTTP client.
		try {
			client.close();
		} catch(IOException e) {
			System.out.println("Error closing HTTP client.");
			e.printStackTrace();
		}
		
		System.out.println("Goodbye!");
	}
	
	/**
	 * This method makes the HTTP request to the API.
	 * 
	 * @param uri		The URI string we want to query
	 * 
	 * @TODO Eventually, this will return organized info
	 * 		 about the movie we requested. But not yet.
	 * 		 Patience...
	 */
	public void makeHttpRequest(String uri) {
		
		// Initialize a request with our completed URI
		System.out.println("Checking " + uri);
		HttpGet request = new HttpGet(uri);
		
		// Make the request to the API and print the info we receive
		try {
			HttpResponse response = client.execute(request);
			BufferedReader inFromAPI = new BufferedReader(
					new InputStreamReader(
						response.getEntity().getContent()
					)
			);
			
			String line = "";
			while((line = inFromAPI.readLine()) != null) {
				System.out.println(line);
			}
		} catch(IOException e) {
			System.out.println("Something went wrong.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method parses the title and year to make them fit the
	 * format the OMDb API expects in URIs. Namely, they must
	 * follow a format like this...
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