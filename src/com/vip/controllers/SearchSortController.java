package com.vip.controllers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.vip.attributes.Video;
import com.vip.window.OmdbRequest;

/**
 * This class stores information about the Videos in the program,
 * and with this class all the sort and search algorithms are executed.
 * 
 * @author Johannes Licht
 */
public class SearchSortController {
	
	private ArrayList<Video> movies;
	
	private DefaultListModel<String> defaultJList;
	
	private static SearchSortController instance;
	
	private SearchSortController() {
		defaultJList = new DefaultListModel<String>();
		movies = new ArrayList<Video>();
	}
	
	public static SearchSortController getInstance() {
		if(instance == null) {
			SearchSortController.instance = new SearchSortController();
		}
		return SearchSortController.instance;
	}
	
	/**
	 * Method for getting the movies ArrayList
	 * @return The ArrayList of movies stored locally
	 */
	public ArrayList<Video> getMovies() {
		return movies;
	}
	
	/**
	 * Method for adding a Video to the movies ArrayList
	 * @param vid 
	 * 			Video that will be added
	 */
	public void addMovieToList(Video vid) {
		movies.add(vid);
	}
	
	/**
	 * Method for updating the ListModel, so the List in the JFrame 
	 * is actually showing the newest version of all movies stored 
	 * in the movies ArrayList
	 */
	public void updateList(ArrayList<Video> currentList) {
		defaultJList.add(0, null);
		defaultJList.clear();
		for (Video temp : currentList) {
			defaultJList.addElement(temp.getTitle());
		}
	}
	
	/**
	 * Getter for returning the ListModel, for keeping it stick
	 * to the JList in the Swing application
	 * @return The ListModel which contains every title of every movie in the movies ArrayList 
	 */
	public DefaultListModel<String> getList() {
		return defaultJList;
	}
	
	/**
	 * A method that returns a certain Video from the movies ArrayList
	 * by index
	 * @param index
	 * 			The index of the Video that should be returned
	 * @return The Video at the index of 'index'
	 */
	public Video getVideoByIndex(int index) {
		return movies.get(index);
	}
	
	
	public void fetchVideoInformation(Video vid) {
		//System.out.println("Fetching information for " + vid.getTitle());
		String searchKey = (String) JOptionPane.showInputDialog("Please enter the name of the Video that is searched for!");
		OmdbRequest window = new OmdbRequest(OMDBController.getInstance().searchApi(searchKey));
		window.setEnabled(true);
		window.setVisible(true);
	}
	
	
	/**
	 * Sorts list by title
	 */
	public void sortByTitle(){
		
		Collections.sort(movies, new Comparator<Video>() {
			    public int compare(Video one, Video other) {
			        return one.getTitle().compareTo(other.getTitle());
			    }
		});
		updateList(movies);
	}
	
	/**
	 * Sorts list by country
	 */
	public void sortByCountry(){
		
		Collections.sort(movies, new Comparator<Video>() {
			    public int compare(Video one, Video other) {
			        return one.getCountry().compareTo(other.getCountry());
			    }
		});
		updateList(movies);
	}
	
	/**
	 * Sorts list by personal rating
	 */
	public void sortByPersonalRating(){
		
		Collections.sort(movies, new Comparator<Video>() {
			    public int compare(Video one, Video other) {
			        return Double.compare(one.getPersonalRating(), other.getPersonalRating());
			    }
		});
		updateList(movies);
	}
	
	/**
	 * Sorts list by imdb rating
	 */
	public void sortByImdbRating(){
		
		Collections.sort(movies, new Comparator<Video>() {
			    public int compare(Video one, Video other) {
			        return Double.compare(one.getImdbRating(), other.getImdbRating());
			    }
		});
		updateList(movies);
	}
	
	/**
	 * Sorts list by release date
	 */
	public void sortByReleaseDate(){
		
		Collections.sort(movies, new Comparator<Video>() {
			    public int compare(Video one, Video other) {
			        return one.getReleaseDate().compareTo(other.getReleaseDate());
			    }
		});
		updateList(movies);
	}
	
	
	/**
	 * Search by anything in the object and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchAll(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.toString().contains(searchText)){
				results.add(vid);
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by title and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByTitle(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getTitle() != null){
				if(vid.getTitle().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by director and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByDirector(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getDirector() != null){
				if(vid.getDirector().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by country and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByCountry(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getCountry() != null){
				if(vid.getCountry().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by cast and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByCast(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getCast() != null){
				if(vid.getTitle().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by genre and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByGenre(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getGenre() != null){
				if(vid.getGenre().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by writers and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByWriters(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getWriters() != null){
				if(vid.getWriters().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by release date and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByReleaseDate(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getReleaseDate() != null){
				if(vid.getReleaseDate().toString().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
	
	/**
	 * Search by plot summary and update the list being displayed.
	 * 
	 * @param searchText
	 */
	public void searchByPlot(String searchText){
		ArrayList<Video> results = new ArrayList<Video>();
		
		for(Video vid:movies){
			if(vid.getPlotSummary() != null){
				if(vid.getPlotSummary().contains(searchText)){
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
}
