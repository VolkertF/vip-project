package com.vip.controllers;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.vip.attributes.Video;
import com.vip.database.VideoTable;

/**
 * This class handles all actions that the program want to make, considering any
 * database stuff, including saving all movies in the database, updating
 * information in the database and loading already stored informations from the
 * database.
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
		database = new VideoTable();
		database.createTable();
	}

	/**
	 * Method used for loading the videos from the database into the program
	 */
	public void loadVideos() {

		ArrayList<Video> videos = database.loadVideos();
		SearchSortController.getInstance().loadDatabaseMovies(videos);
	}

	/**
	 * Method calling to save any video file to the database
	 * 
	 * @param video
	 *            The Video that ought to be saved.
	 */
	public void save(Video video) {
		if (video == null) {
			System.out.println("Video object is null");
		}

		database.saveVideo(video);
	}

	/**
	 * An alternative to the save()-Method. It takes more than one object to
	 * save all of them.
	 * 
	 * @param videos
	 *            An ArrayList of Video-Objects, that should be saved by the
	 *            method.
	 */
	public void saveAll(ArrayList<Video> videos) {
		File databaseFile = new File("test.db");
		if (databaseFile.exists() && !databaseFile.isDirectory()) {
			for (Video video : videos) {
				save(video);
			}
		} else {
			JOptionPane.showMessageDialog(null,
				"An Error occured during saving the Videos. No database exists. The program will now generate a new one just for you!",
				"Database missing", JOptionPane.ERROR_MESSAGE);
			database = new VideoTable();
			database.createTable();
		}
	}

	/**
	 * Method which should be called before closing the program, so it can be
	 * assured, that every video has information from OMDb fetched.
	 */
	public void checkVideoFetchedInformation() {
		for (Video temp : SearchSortController.getInstance().getMovies()) {
			if (!temp.isInfoFetched()) {
				SearchSortController.getInstance().fetchVideoInformation(temp);
			}
		}
	}
}
