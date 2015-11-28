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
* This class extracts information from an OMDb request. It works together with
* the OMDBConnector class to make API requests and get relevant information.
* Please use the OMDBController to make requests instead of this class.
* 
* @author Cyril Casapao
* 
* @TODO
* Currently, this class uses terminal input and print statements to make
* API requests. Eventually, this code will be used within a GUI so the 
* user can look up info about a specified movie. When we implement that,
* we will need to remove the terminal-style interaction here.
*/
public class InfoExtractor {
	
	Scanner scan;
	
	// Constants used to specify request type
	private final String MOVIE_REQUEST;
	private final String SEARCH_REQUEST;
	private final String EPISODE_LIST_REQUEST;
	private final String ID_REQUEST;
	
	/**
	 * Constructor method that initializes an InfoExtractor object
	 */
	public InfoExtractor() {
		scan = new Scanner(System.in);
		MOVIE_REQUEST = "movie";
		SEARCH_REQUEST = "search";
		EPISODE_LIST_REQUEST = "episode list";
		ID_REQUEST = "id";
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
	 * @param Map
	 * 		The Map holding the information from a request
	 * @param category
	 * 		The type of information to extract from the API response 
	 * 		(e.g., genre, director, cast, etc.)
	 * @return
	 * 		An ArrayList holding each piece of information
	 * 		in a separate index.
	 * 
	 * @TODO
	 * Remove these print statements. They are only used in debugging
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
		System.out.println("---------------------------------------------");
		System.out.println("InfoExtractor.splitCategoryInfoString(): " +
				category + "...");
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
	 * @param jsonString
	 * 		The JSON information we receive from OMDb
	 * 
	 * @return
	 * 		A map holding key-value pairs of movie info
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
	 * This method gets information from a search result. A search result
	 * includes a general search (searching OMDb for Star Wars) and an
	 * episode list request (getting a list of episodes for season 2 of
	 * Gravity Falls).
	 * 
	 * We handle these with the same method because OMDb treats an episode
	 * list request as a search. 
	 * 
	 * @param apiResponse
	 * 		A string representing the response from OMDb
	 * @return
	 * 		The list of SearchResult objects
	 */
	public ArrayList<SearchResult> extractSearchResults(String apiResponse) {
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(apiResponse).getAsJsonObject();
		JsonArray results;
		
		ArrayList<SearchResult> listOfResults = new ArrayList<SearchResult>();
		String searchType = "";
		
		// Find out what kind of search we're dealing with
		if(object.has("Search")) {
			searchType = SEARCH_REQUEST;
			results = object.get("Search").getAsJsonArray();
		} else if(object.has("Episodes")) {
			searchType = EPISODE_LIST_REQUEST;
			results = object.get("Episodes").getAsJsonArray();
		} else {
			System.out.println("Bad request type.");
			return listOfResults;
		}
		
		// Create the proper object for each result in the search
		int numResults = results.size();
		for(int i = 0; i < numResults; i++) {
			if(searchType.equals(SEARCH_REQUEST)) {
				JsonObject thisResult = results.get(i).getAsJsonObject();
				MediaSearchResult mediaObject = 
						toMediaSearchResultObject(thisResult);
				listOfResults.add(mediaObject);
			} else if(searchType.equals(EPISODE_LIST_REQUEST)) {
				JsonObject thisResult = results.get(i).getAsJsonObject();
				EpisodeListResult episodeObject
					= toEpisodeListResultObject(thisResult);
				listOfResults.add(episodeObject);
				
			} else {
				System.out.println("Bad request type.");
				break;
			}
		}
		
		return listOfResults;
	}
	

	/**
	 * This method creates a MediaSearchResult object representing one
	 * result from a general search.
	 * 
	 * @param thisResult
	 * 		A JsonObject containing fields describing a search result
	 * 
	 * @return
	 * 		An object describing a search result
	 */
	private MediaSearchResult toMediaSearchResultObject(JsonObject thisResult) {
		String title = thisResult.get("Title").toString();
		String year = thisResult.get("Year").toString();
		String imdbId = thisResult.get("imdbID").toString();
		String poster = thisResult.get("Poster").toString();
		
		MediaSearchResult result =
				new MediaSearchResult(title, year, imdbId, poster);
		
		return result;
	}
	
	
	/**
	 * This method creates an EpisodeListResult object representing one
	 * result from an episode list request.
	 * 
	 * @param thisResult
	 * 		A JsonObject containing fields describing a search result
	 * 
	 * @return
	 * 		An object describing a search result
	 */
	private EpisodeListResult toEpisodeListResultObject(JsonObject thisResult) {
		String title = thisResult.get("Title").toString();
		
		int episodeNumber = 
				Integer.parseInt(
						thisResult.get("Episode").toString().replace("\"", "")
		);
		String releaseDate = thisResult.get("Released").toString();
		String imdbId = thisResult.get("imdbID").toString();
		double imdbRating = 
				Double.parseDouble(
						thisResult.get("imdbRating").toString().replace("\"", "")
		);
		
		EpisodeListResult result =
				new EpisodeListResult(
						title, episodeNumber, releaseDate, imdbId, imdbRating
		);
		
		return result;
	}
	
	
	/**
	 * This runs the same OMDb connection test as OMDBConnector used
	 * to run.
	 * 
	 * @TODO Remove this method later.
	 */
	private void runTest() throws IOException {
		OMDBConnector connector = new OMDBConnector();
		
		System.out.println("Welcome to the OMDb API tester!");
		
		while(true) {
			
			System.out.println("Type MOVIE to search for a specific movie. " +
					"Type SEARCH to perform a search. Type EPISODE LIST to " +
					" get a list of episodes. Type ID to search by ID. " +
					"Enter nothing to quit.");
			
			String requestType = scan.nextLine();
			if(requestType.isEmpty()){
				break;
			}
			
			boolean isMovieRequest =
					requestType.equals(MOVIE_REQUEST);
			boolean isEpisodeListRequest =
					requestType.equals(EPISODE_LIST_REQUEST);
			boolean isSearchRequest =
					requestType.equals(SEARCH_REQUEST);
			boolean isIdRequest =
					requestType.equals(ID_REQUEST);
			
			System.out.println("Enter a title: ");
			String title = scan.nextLine();
			if(title.isEmpty()){
				break;
			}
			
			System.out.println("If doing a specific movie search, you can"
					+ "optionally specify the year the movie came out. If "
					+ "doing a search (with keywords or ID), enter nothing: ");
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
				ArrayList<SearchResult> results = 
						extractSearchResults(response);
				
				// TODO: Remove this later.
				for(SearchResult result : results) {
					MediaSearchResult mediaResult = (MediaSearchResult) result;
					
					System.out.println("");
					System.out.println("Title: " + mediaResult.getTitle());
					System.out.println("Year: " + mediaResult.getYear());
					System.out.println("Poster: " + mediaResult.getPoster());
					System.out.println("IMDB ID: " + mediaResult.getImdbId());
				}
				
			} else if(isEpisodeListRequest) {
				System.out.println("Please enter a season number: " );
				String seasonNumber = scan.nextLine();
				
				response = connector.requestEpisodeList(title, seasonNumber);
				ArrayList<SearchResult> results = 
						extractSearchResults(response);
				
				// TODO: Remove this later.
				for(SearchResult result : results) {
					EpisodeListResult episodeResult = (EpisodeListResult) result;
					
					System.out.println("");
					System.out.println("Title: " + episodeResult.getTitle());
					System.out.println("Release date: " + episodeResult.getReleaseDate());
					System.out.println("Episode number: " + episodeResult.getEpisodeNumber());
					System.out.println("IMDB Rating: " + episodeResult.getImdbRating());
					System.out.println("IMDB ID: " + episodeResult.getImdbId());
				}
				
			} else if(isIdRequest) {
				response = connector.requestById(title);
				Map<String, String> infoMap = extractMovieInfo(response);
				printJson(infoMap);
				
				splitCategoryInfoString(infoMap.get("Genre"), "Genre");
				splitCategoryInfoString(infoMap.get("Director"), "Director");
				splitCategoryInfoString(infoMap.get("Writer"), "Writer");
				splitCategoryInfoString(infoMap.get("Actors"), "Actors");
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
	 * @param information
	 * 		The map representing the JSON response
	 * 
	 * @TODO Remove this method when we implement user OMDb queries
	 */
	private void printJson(Map<String, String> information) {
		for(Map.Entry<String, String> entry : information.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue();
			System.out.println(key + ": " + value);
		}
	}
}