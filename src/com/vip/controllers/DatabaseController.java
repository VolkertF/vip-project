package com.vip.controllers;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import com.vip.attributes.Video;
import com.vip.database.VideoTable;


/**
 * This class handles all actions that the program want to make,
 * considering any database stuff, including saving all movies in
 * the database, updating information in the database and loading
 * already stored informations from the database.
 * 
 * @author Johannes Licht
 */
public class DatabaseController {
	/**
	 * Private instance of the database object
	 */
	@SuppressWarnings("unused")
	private VideoTable database;
	
	/**
	 * Here all local Video objects are stored in
	 */
	private ArrayList<Video> movies;
	
	/**
	 * This ListModel works together with the ArrayList movies
	 * and is responsible for displaying everything right.
	 */
	private DefaultListModel<String> defaultJList;
	
	/**
	 * Public DatabaseController Constructor
	 */
	public DatabaseController() {
		VideoTable database = new VideoTable();
		database.createTable();
		defaultJList = new DefaultListModel<String>();
		movies = new ArrayList<Video>();
	}
	
	public void saveVideos() {
		/**for(Video temp : movies) {
			database.saveVideo(temp);
		}**/
		//Doesn't work for videos which has not already fetched imbd-information
		System.out.println("Saved All!");
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
	public void updateList() {
		defaultJList.add(0, null);
		defaultJList.clear();
		for (Video temp : movies) {
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
}


