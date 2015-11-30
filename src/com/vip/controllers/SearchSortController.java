package com.vip.controllers;

import java.util.ArrayList;

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
	
	public void fetchVideoInformation(Video vid) {
		//System.out.println("Fetching information for " + vid.getTitle());
		String searchKey = (String) JOptionPane.showInputDialog("Please enter the name of the Video that is searched for!");
		OmdbRequest window = new OmdbRequest(OMDBController.getInstance().searchApi(searchKey));
		window.setEnabled(true);
		window.setVisible(true);
	}
}
