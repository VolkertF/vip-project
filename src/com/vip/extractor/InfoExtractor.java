package com.vip.extractor;

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
	
	// Constants used to specify request type
	private final String MOVIE_REQUEST;
	private final String SEARCH_REQUEST;
	private final String EPISODE_LIST_REQUEST;
	
	/**
	 * Constructor method.
	 */
	public InfoExtractor() {
		scan = new Scanner(System.in);
		MOVIE_REQUEST = "movie";
		SEARCH_REQUEST = "search";
		EPISODE_LIST_REQUEST = "episode list";
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
	public ArrayList<String> splitCategoryInfoString(
			String categoryInfo, 
			String category
	)
	{
		
		// Split the string using the string ", " as a delimiter
		// then return the information as an ArrayList.
		String[] separatedString = categoryInfo.split(", ");
		ArrayList<String> categoryInfoList = 
				new ArrayList<String>(Arrays.asList(separatedString));
		
		//---------------------------------------------------------------|
		// TODO: Remove these statements later.
		//---------------------------------------------------------------|
		System.out.println("-----------------------------------------------------------------");
		System.out.println("InfoExtractor.splitCategoryInfoString(): " + category + "...");
		for(String entity : categoryInfoList){
			System.out.println(entity);
		}
		System.out.println("");
		
		return categoryInfoList;
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
	public ArrayList<GeneralSearchResult> extractGeneralSearchResults(String apiResponse) {
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(apiResponse).getAsJsonObject();
		JsonArray results = object.get("Search").getAsJsonArray();
		
		int numResults = results.size();
		
		ArrayList<GeneralSearchResult> listResults = 
				new ArrayList<GeneralSearchResult>();
		
		for(int i = 0; i < numResults; i++) {
			JsonObject thisResult = results.get(i).getAsJsonObject();
			
			String title = thisResult.get("Title").toString();
			String year = thisResult.get("Year").toString();
			String imdbId = thisResult.get("imdbID").toString();
			String poster = thisResult.get("Poster").toString();
			
			GeneralSearchResult result =
					new GeneralSearchResult(title, year, imdbId, poster);
			listResults.add(result);
		}
		
		return listResults;
	}
	
	
	/**
	 * This method deals with episode list results. It converts the results we
	 * get from the API into an array of JSON objects that we can further
	 * parse to get information about each result.
	 * 
	 * @param apiResponse	The JSON information we receive from OMDb
	 * @return ArrayList	An ArrayList of SearchResult objects
	 */
	public ArrayList<EpisodeListResult> extractEpisodeListResults(String apiResponse) {
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(apiResponse).getAsJsonObject();
		JsonArray results = object.get("Episodes").getAsJsonArray();
		
		int numResults = results.size();
		
		ArrayList<EpisodeListResult> listResults = 
				new ArrayList<EpisodeListResult>();
		
		for(int i = 0; i < numResults; i++) {
			JsonObject thisResult = results.get(i).getAsJsonObject();
			
			String title = thisResult.get("Title").toString();
			int episodeNumber = 
					Integer.parseInt(thisResult.get("Episode").toString().replace("\"", ""));
			String releaseDate = thisResult.get("Released").toString();
			String imdbId = thisResult.get("imdbID").toString();
			double imdbRating = 
					Double.parseDouble(thisResult.get("imdbRating").toString().replace("\"", ""));
			
			EpisodeListResult result =
					new EpisodeListResult(title, episodeNumber, releaseDate, imdbId, imdbRating);
			
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
					"Type SEARCH to perform a search. Type EPISODE LIST to get a list of "
					+ "episodes. Enter nothing to quit.");
			
			String requestType = scan.nextLine();
			if(requestType.isEmpty()){
				break;
			}
			boolean isMovieRequest = requestType.equals(MOVIE_REQUEST);
			boolean isEpisodeListRequest = requestType.equals(EPISODE_LIST_REQUEST);
			boolean isSearchRequest = requestType.equals(SEARCH_REQUEST);
			
			System.out.println("Enter a title: ");
			String title = scan.nextLine();
			if(title.isEmpty()){
				break;
			}
			
			System.out.println("If doing a specific movie search, you can"
					+ "optionally specify the year the movie came out. If "
					+ "doing a search, enter nothing: ");
			String year = scan.nextLine();
			
			String response = "";
			if(isMovieRequest) {
				
				response = connector.requestMovie(title, year);
				Map<String, String> infoMap = extractMovieInfo(response);
				printJson(infoMap);
				
				splitCategoryInfoString(infoMap.get("Genre"), "Genre");
				splitCategoryInfoString(infoMap.get("Director"), "Director");
				splitCategoryInfoString(infoMap.get("Writer"), "Writer");
				splitCategoryInfoString(infoMap.get("Actors"), "Actors");
				
			} else if(isSearchRequest){
				
				response = connector.requestSearch(title, year);
				ArrayList<GeneralSearchResult> results = 
						extractGeneralSearchResults(response);
				
				// TODO: Remove this later.
				for(GeneralSearchResult result : results) {
					System.out.println("");
					System.out.println("Title: " + result.getTitle());
					System.out.println("Year: " + result.getYear());
					System.out.println("Poster: " + result.getPoster());
					System.out.println("IMDB ID: " + result.getImdbId());
				}
				
			} else if(isEpisodeListRequest) {
				System.out.println("Please enter a season number: " );
				String seasonNumber = scan.nextLine();
				
				response = connector.requestEpisodeList(title, seasonNumber);
				ArrayList<EpisodeListResult> results = 
						extractEpisodeListResults(response);
				
				// TODO: Remove this later.
				for(EpisodeListResult result : results) {
					System.out.println("");
					System.out.println("Title: " + result.getTitle());
					System.out.println("Release date: " + result.getReleaseDate());
					System.out.println("Episode number: " + result.getEpisodeNumber());
					System.out.println("IMDB Rating: " + result.getImdbId());
					System.out.println("IMDB ID: " + result.getImdbId());
				}
				
			} else {
				System.out.println("Invalid option. Exiting...");
				break;
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

