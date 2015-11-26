package com.vip.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.vip.omdb.OMDBConnector;
import com.vip.extractor.*;

/**
 * This controller allows the GUI to interact with OMDb. 
 * 
 * To use it, you should first make call the appropriate makeApiRequest() method
 * to get a map of information. Then you should call the appropriate get methods
 * to extract information from this request.
 * 
 * For example, if you want genre information about a movie you would call...
 * 
 *    Map<String, String> movieInfo = makeApiMovieRequest(movieTitle, optionalYear);
 *    ArrayList<String> genreList = getGenreList(movieInfo);
 *    
 * ...which would store each individual genre in genreList.
 *
 * @TODO: Figure out how to handle cases when a search fails.
 *    
 * @author Cyril Casapao
 */
public class OMDBController {
	
	private OMDBConnector connector;
	private InfoExtractor extractor;
	
	/**
	 * This method initializes the controller that allows the GUI to
	 * deal with OMDb.
	 */
	public OMDBController() {
		connector = new OMDBConnector();
		extractor = new InfoExtractor();
	}
	
	
	/**
	 * This method requests movie information from OMDb. Note that the response is
	 * in JSON so the map must be parsed out to use information. That is why we
	 * must call the separate get methods for each field.
	 * 
	 * @param title
	 * 		The title of the movie
	 * @param year
	 * 		An optional parameter that specifies the year the movie came out
	 * @return
	 * 		A map associating categories (keys) to lists (values). For instance,
	 * 		the "genre" key 
	 */
	public Map<String, String> makeApiMovieRequest(String title, String year) {
		
		String apiResponse = "";
		try {
			apiResponse = connector.requestMovie(title, year);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return extractor.extractMovieInfo(apiResponse);
	} 
	
	
	/**
	 * This method searches the API. Its results are held in SearchResults
	 * objects. These objects hold basic information about each result
	 * including an IMDB ID that can be used to perform a more specific
	 * search.
	 * 
	 * The results from this method can be used as parameters to other
	 * methods in the OMDB controller to extract data from them.
	 * 
	 * @param searchKey
	 * 		The item to search for
	 * @return
	 * 		A list of SearchResult objects holding information about
	 * 		each result
	 */
	public ArrayList<SearchResult> searchApi(String searchKey) {
		
		String response = "";
		try {
			response = connector.requestSearch(searchKey, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return extractor.extractSearchResults(response);
	}
	
	
	/**
	 * This method gets a list of episodes for a certain season of a specified
	 * TV series. It returns a list of SearchResult objects that can be used
	 * in other methods to get information from. For example, the IMDB ID can
	 * be extracted to perform a more general search.
	 * 
	 * @param title
	 * 		The series to search for
	 * @param seasonNumber
	 * 		The season we want info about
	 * @return
	 * 		A list of SearchResult objects holding information about each
	 * 		result (episode)
	 */
	public ArrayList<SearchResult> getEpisodeList(String title, String seasonNumber) {
		
		String response = "";
		try {
			response = connector.requestEpisodeList(title, seasonNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return extractor.extractSearchResults(response);
	}
	
	
	/**
	 * This method returns a list of a movie's genres as listed by OMDb.
	 * 
	 * @param infoMap
	 * 		A map holding information about a movie
	 * @return
	 * 		A list of this movie's genres
	 */
	public ArrayList<String> getGenreList(Map<String, String> infoMap) {
		return extractor.splitCategoryInfoString(infoMap.get("Genre"), "Genre");
	}
	
	
	/**
	 * This method returns a list of a movie's genres as listed by OMDb.
	 * 
	 * @param infoMap
	 * 		A map holding information about a movie
	 * @return
	 * 		A list of this movie's directors
	 */
	public ArrayList<String> getDirectorList(Map<String, String> infoMap) {
		return extractor.splitCategoryInfoString(infoMap.get("Director"), "Director");
	}
	
	
	/**
	 * This method returns a list of a movie's genres as listed by OMDb.
	 * 
	 * @param infoMap
	 * 		A map holding information about a movie
	 * @return
	 * 		A list of this movie's writers
	 */
	public ArrayList<String> getWriterList(Map<String, String> infoMap) {
		return extractor.splitCategoryInfoString(infoMap.get("Writer"), "Writer");
	}
	
	
	/**
	 * This method returns a list of a movie's genres as listed by OMDb.
	 * 
	 * @param infoMap
	 * 		A map holding information about a movie
	 * @return
	 * 		A list of this movie's actors
	 */
	public ArrayList<String> getActorsList(Map<String, String> infoMap) {
		return extractor.splitCategoryInfoString(infoMap.get("Actors"), "Actors");
	}

	
	/**
	 * This method closes the connector and should be called on application
	 * shutdown.
	 * 
	 * @TODO: Do we even need this method?
	 */
	public void closeConnector() {
		try {
			connector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
