package com.vip.extractor;

//import com.vip.attributes.*;
import com.vip.omdb.OMDBConnector;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;


/**
* This class extracts information from an OMDb request.
* 
* @author Cyril Casapao
* 
* @TODO 	Currently, this class uses terminal input and print
* 			statements to make API requests. Eventually, this code
* 			will be used within a GUI so the user can look up info
* 			about a specified movie. When we implement that, we will
* 			need to remove the terminal-style interaction here.
*/
public class InfoExtractor {
	
	Scanner scan;
	
	/**
	 * Constructor method.
	 */
	public InfoExtractor() {
		scan = new Scanner(System.in);
	}
	
	
	/**
	 * Main method. Right now it is only used for testing.
	 * 
	 * @TODO Remove this method later.
	 */
	public static void main(String[] args) {
		InfoExtractor extractor = new InfoExtractor();
		try {
			extractor.runTest();
		} catch (IOException e) {
			System.out.println("Encountered problem with the API.");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Bad input.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method takes the information received from an OMDb request
	 * and parses it to separate entries that fall under the same
	 * category.
	 * 
	 * For example, an API response will hold genre information in a
	 * String that looks like "Action, Adventure, Comedy". However,
	 * we want to split it into "Action", "Adventure", and "Comedy". 
	 * This way our Movie object can have a list of multiple genres 
	 * instead of one String that contains several genres.
	 * 
	 * @param Map			The Map holding the information from a request
	 * @param category		The type of information to extract from the API
	 * 						response (e.g., genre, director, cast, etc.)
	 * @return ArrayList	An ArrayList holding each piece of information
	 * 						in a separate index.
	 * 
	 * @TODO We will need to change how Movie objects store information.
	 * 		 In addition, we'll need to remove these print statements.
	 */
	public ArrayList<String> splitCategoryInfo(
			Map<String, String> infoMap, 
			String category
	)
	{
		
		// Split the string using the string ", " as a delimiter
		// then return the information as an ArrayList.
		String[] separatedString = infoMap.get(category).split(", ");
		ArrayList<String> categoryInfo = 
				new ArrayList<String>(Arrays.asList(separatedString));
		
		//---------------------------------------------------------------|
		// TODO: Remove these statements later.
		//---------------------------------------------------------------|
		System.out.println("-----------------------------------------------------------------");
		System.out.println("InfoExtractor.splitCategoryInfo(): " + category + "...");
		for(String entity : categoryInfo){
			System.out.println(entity);
		}
		System.out.println("");
		
		return categoryInfo;
	}
	
	
	/**
	 * This method takes the JSON response we receive from a specific movie
	 * request and turns it into a Java Map. This allows us to work easily 
	 * using a native Java object while also preserving the useful key-value
	 * relationship inherent in JSON.
	 * 
	 * @param jsonString 	The JSON information we receive from OMDb
	 * @return Map			A map holding key-value pairs of movie info
	 */
	public Map<String, String> extractMovieInfo(String apiResponse) {
		Gson gson = new Gson();
		
		// The fromJson() method takes a JSON string as its first parameter
		// and a Class as a second. There's no clean simple way to get the
		// Map type so we have to use the ugly method seen here.
		Map<String, String> information =
			gson.fromJson(
				apiResponse,
				new TypeToken<Map<String, String>>(){}.getType()
			);
		
		return information;
	}
	
	
	/**
	 * This method deals with search results. It converts the results we
	 * get from the API into an array of JSON objects that we can further
	 * parse to get information about each result.
	 * 
	 * @param apiResponse	The JSON information we receive from OMDb
	 * @return ArrayList	An ArrayList of SearchResult objects
	 */
	public ArrayList<SearchResult> extractSearchResults(String apiResponse) {
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(apiResponse).getAsJsonObject();
		JsonArray results = object.get("Search").getAsJsonArray();
		
		int numResults = results.size();
		
		ArrayList<SearchResult> listResults = new ArrayList<SearchResult>();
		
		for(int i = 0; i < numResults; i++) {
			JsonObject thisResult = results.get(i).getAsJsonObject();
			
			String title = thisResult.get("Title").toString();
			String year = thisResult.get("Year").toString();
			String id = thisResult.get("imdbID").toString();
			String poster = thisResult.get("Poster").toString();
			
			SearchResult result = new SearchResult(title, year, id, poster);
			listResults.add(result);
		}
		
		return listResults;
	}
	
	
	/**
	 * This runs the same OMDb connection test as OMDBConnector used
	 * to run.
	 * 
	 * @TODO Remove this method later.
	 */
	public void runTest() throws IOException {
		OMDBConnector connector = new OMDBConnector();
		
		System.out.println("Welcome to the OMDb API tester!");
		
		while(true) {
			System.out.println("Type MOVIE to search for a specific movie. " +
					"Type anything else to perform a search. Enter nothing " +
					"to quit.");
			String requestType = scan.nextLine();
			if(requestType.isEmpty()){
				break;
			}
			
			System.out.println("Enter a movie title: ");
			String title = scan.nextLine();
			
			if(title.isEmpty()){
				break;
			}
			
			System.out.println("Enter the year the movie came out: ");
			String year = scan.nextLine();
			
			String response = "";
			if(requestType.equalsIgnoreCase("movie")) {
				response = connector.requestMovie(title, year);
				Map<String, String> infoMap = extractMovieInfo(response);
				printJson(infoMap);
				
				splitCategoryInfo(infoMap, "Genre");
				splitCategoryInfo(infoMap, "Director");
				splitCategoryInfo(infoMap, "Writer");
				splitCategoryInfo(infoMap, "Actors");
			} else {
				response = connector.requestSearch(title, year);
				ArrayList<SearchResult> results = extractSearchResults(response);
				
				// TODO: Remove this later.
				for(SearchResult result : results) {
					System.out.println("");
					System.out.println("Title: " + result.getTitle());
					System.out.println("Year: " + result.getYear());
					System.out.println("IMDB ID: " + result.getId());
					System.out.println("Poster: " + result.getPoster());
				}
			}

		}
		
		connector.close();
		scan.close();
		
		System.out.println("Goodbye!");
	}
	
	
	/**
	 * Print the deserialized JSON response. This method is for testing.
	 * 
	 * @param information	The map representing the JSON response
	 * 
	 * @TODO: Remove this method when we implement user OMDb queries
	 */
	public void printJson(Map<String, String> information) {
		for(Map.Entry<String, String> entry : information.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue();
			System.out.println(key + ": " + value);
		}
	}
}

