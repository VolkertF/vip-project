package com.extractor;

//import com.vip.attributes.*;
import com.vip.omdb.OMDBConnector;
import com.google.gson.Gson;
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
 * @TODO 	Remember that genre, writers, and other information fields
 * 			could have multiple pieces of info. For instance, Up's genre
 * 			consists of the string "Animation, Adventure, Comedy" meaning
 * 			we should have separate values for each of these. We still need
 * 			to write code to parse this information and put it into (probably)
 * 			an ArrayList.
 * 		
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
		}
	}
	
	
	/**
	 * This method takes the information received from an OMDb request
	 * and creates a new Movie object.
	 * 
	 * @param Map		The Map holding the information from a request
	 * 
	 * @TODO We will need to change how Movie objects store information.
	 * 		 In addition, we'll need to remove these print statements.
	 */
	public void extract(Map<String, String> infoMap) {
		
		int yearReleased = Integer.parseInt(infoMap.get("Year"));
		
		ArrayList<String> directors = this.toArrayList(infoMap.get("Director"));
		ArrayList<String> writers = this.toArrayList(infoMap.get("Writer"));
		ArrayList<String> genres = this.toArrayList(infoMap.get("Genre"));
		
		//---------------------------------------------------------------|
		// TODO: Remove these statements later.
		//---------------------------------------------------------------|
		System.out.println("-----------------------------------------------------------------");
		System.out.println("InfoExtractor.extract(): This movie was released in " + yearReleased);
		
		System.out.println("");
		System.out.println("InfoExtractor.extract(): Directors...");
		for(String entity : directors){
			System.out.println(entity);
		}

		System.out.println("");
		System.out.println("InfoExtractor.extract(): Writers...");
		for(String entity : writers){
			System.out.println(entity);
		}
		
		System.out.println("");
		System.out.println("InfoExtractor.extract: Genres...");
		for(String entity : genres){
			System.out.println(entity);
		}
		//---------------------------------------------------------------|
		// TODO: End of section to remove.
		//---------------------------------------------------------------|
		
//		Movie movie = new Movie();
		
//		movie.setYearReleased(Integer.parseInt(infoMap.get("Year")));
//		movie.setDirector(infoMap.get("Director"));
//		movie.setWriter(infoMap.get("Writer"));
//		movie.setGenre(infoMap.get("Genre"));
	}
	
	
	/**
	 * This method converts a String representing comma-separated
	 * entities into an ArrayList holding each entity.
	 * 
	 * For example, if the genre information is held in the String
	 * "Action, Adventure, Comedy" we need to split it into 
	 * "Action", "Adventure", and "Comedy". This way our movie
	 * object can have multiple genres instead of one list of
	 * genres.
	 * 
	 * @param info			The String to convert
	 * @return ArrayList	An ArrayList containing the information
	 * 						in separated Strings
	 */
	private ArrayList<String> toArrayList(String info) {
		String[] splitString = info.split(", ");
		ArrayList<String> entities = new ArrayList<String>(Arrays.asList(splitString));
		return entities;
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
	
	
	/**
	 * This runs the same OMDb connection test as OMDBConnector used
	 * to run.
	 * 
	 * @TODO Remove this method later.
	 */
	public void runTest() throws IOException {
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
			
			String response = connector.makeApiRequest(title, year);
			Map<String, String> information = deserializeJson(response);
			this.printJson(information);
			this.extract(information);
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
