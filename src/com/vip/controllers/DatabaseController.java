package com.vip.controllers;

import java.util.ArrayList;

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
	private VideoTable database;
	
	/**
	 * Public DatabaseController Constructor
	 */
	public DatabaseController() {
		VideoTable database = new VideoTable();
		database.createTable();
	}
	
	public void save(Video video){
		
		database.saveVideo(video);
	}
	
	public void saveAll(ArrayList<Video> videos){
		
		for(Video video:videos){
			database.saveVideo(video);
		}
	}
	
	public void checkVideoFetchedInformation() {
		for(Video temp : SearchSortController.getInstance().getMovies()) {
			if(!temp.isInfoFetched()) {
				SearchSortController.getInstance().fetchVideoInformation(temp);
			}
		}
	}
	
}


