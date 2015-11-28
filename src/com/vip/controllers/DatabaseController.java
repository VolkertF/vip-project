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
	
	private VideoTable database;
	
	private ArrayList<Video> movies;
	
	private DefaultListModel<String> defaultJList;
	
	public DatabaseController() {
		VideoTable database = new VideoTable();
		database.createTable();
		defaultJList = new DefaultListModel<String>();
		movies = new ArrayList<Video>();
	}
	
	public void saveVideos() {
		for(Video temp : movies) {
			database.insertVideo(temp);
		}
	}
	
	public ArrayList<Video> getMovies() {
		return movies;
	}
	
	public void addMovieToList(Video vid) {
		movies.add(vid);
	}
	
	public void updateList() {
		defaultJList.add(0, null);
		defaultJList.clear();
		for (Video temp : movies) {
			defaultJList.addElement(temp.getTitle());
		}
	}
	
	public DefaultListModel<String> getList() {
		return defaultJList;
	}
	
	public Video getVideoByIndex(int index) {
		return movies.get(index);
	}
}


