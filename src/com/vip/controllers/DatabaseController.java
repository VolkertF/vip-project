package com.vip.controllers;

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
	 * Public DatabaseController Constructor
	 */
	public DatabaseController() {
		VideoTable database = new VideoTable();
		database.createTable();
	}
	
	public void saveVideos() {
		/**for(Video temp : movies) {
			database.saveVideo(temp);
		}**/
		//Doesn't work for videos which has not already fetched imbd-information
		System.out.println("Saved All!");
	}
}


