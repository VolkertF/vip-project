package com.vip.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.vip.attributes.Video;
import com.vip.window.OmdbRequest;

/**
 * This controller stores information about the movie-list and the
 * DefaultListModel, that display the list in the GUI.
 * 
 * It also delivers a variety of methods for searching and sorting the movie
 * list and assigning information from the OMDbController.
 * 
 * @author Johannes Licht
 */
public class SearchSortController {

	/**
	 * This ArrayList stores all movie files that was added to the program.
	 */
	private ArrayList<Video> movies;

	/**
	 * With this object the JList should show every movie in the right way and
	 * order. Mainly this object is for sorting and searching algorithms.
	 */
	private DefaultListModel<Video> defaultJList;

	/**
	 * A Singleton instance of this class
	 */
	private static SearchSortController instance;

	/** Instance of the application's controller **/
	private Controller controller;

	/**
	 * A private Constructor that can only be called once.
	 */
	private SearchSortController() {
		defaultJList = new DefaultListModel<Video>();
		movies = new ArrayList<Video>();
	}

	/**
	 * The method, that connects the SearchSortController to the outer world.
	 * Yay!
	 * 
	 * @return An initialized instance of this class
	 */
	public static SearchSortController getInstance() {
		if (instance == null) {
			SearchSortController.instance = new SearchSortController();
		}
		return SearchSortController.instance;
	}

	/**
	 * Method for getting the movies ArrayList
	 * 
	 * @return The ArrayList of movies stored locally
	 */
	public ArrayList<Video> getMovies() {
		return movies;
	}

	/**
	 * Method for adding a Video to the movies ArrayList
	 * 
	 * @param vid
	 *            Video that will be added
	 */
	public void addMovieToList(Video vid) {
		movies.add(vid);
	}

	/**
	 * Loads the movies from the database into the list of movies open in the
	 * program
	 * 
	 * @param dbMovies
	 *            The movies from the database
	 */
	public void loadDatabaseMovies(ArrayList<Video> dbMovies){
			for(Video dbVid: dbMovies){
				movies.add(dbVid);
			}
			updateList(movies);
	}

	/**
	 * Method for updating the ListModel, so the List in the JFrame is actually
	 * showing the newest version of all movies stored in the movies ArrayList
	 */
	public void updateList(ArrayList<Video> currentList) {
		defaultJList.add(0, null);
		defaultJList.clear();
		for (Video temp : currentList) {
			defaultJList.addElement(temp);
		}
	}

	/**
	 * Getter for returning the ListModel, for keeping it stick to the JList in
	 * the Swing application
	 * 
	 * @return The ListModel which contains every title of every movie in the
	 *         movies ArrayList
	 */
	public DefaultListModel<Video> getList() {
		return defaultJList;
	}

	/**
	 * A method that returns a certain Video from the movies ArrayList by index
	 * 
	 * @param index
	 *            The index of the Video that should be returned
	 * @return The Video at the index of 'index'
	 */
	public Video getVideoByIndex(int index) {
		return defaultJList.getElementAt(index);
	}

	public void setController(Controller newController) {
		this.controller = newController;
	}

	/**
	 * Method for making a Search on the OMDb based on a string that the user
	 * enter for the movie.
	 * 
	 * @param vid
	 *            The video object the information should be fetched to.
	 */
	public void fetchVideoInformation(Video vid) {
		String searchKey = (String) JOptionPane.showInputDialog(
		        "Please enter the name of the Video that is searched for! (Previous name: " + vid.getTitle());
		OmdbRequest window = new OmdbRequest(OMDBController.getInstance().searchApi(searchKey), vid, controller);
		window.setEnabled(true);
		window.setVisible(true);
	}

	/**
	 * This method takes the Map of key-value pairs that the OMDb API delivers
	 * and assign it to a Video- object.
	 * 
	 * It currently contains Date, which is deprecated right now
	 * 
	 * @param map
	 *            A map of the OMDb-data
	 * @param vid
	 *            The Video Obj. the information should be fetched to.
	 */
	@SuppressWarnings("deprecation")
	public void assignMapToVideo(Map<String, String> map, Video vid) {
		String type = map.get("Type");
		if (type.equals("movie")) {
			vid.setTitle(map.get("Title"));
			vid.setReleaseDate(new Date(95, 9, 21));
			;
			vid.setGenre(new ArrayList<String>(Arrays.asList(map.get("Genre").split(","))));
			vid.setDirector(map.get("Director"));
			vid.setCast(new ArrayList<String>(Arrays.asList(map.get("Actors").split(","))));
			vid.setWriters(new ArrayList<String>(Arrays.asList(map.get("Writer").split(","))));
			vid.setPlotSummary(map.get("Plot"));
			vid.setCountry(map.get("Country"));
			vid.setImdbRating(Double.parseDouble(map.get("imdbRating")));
			vid.setInfoFetched(true);
		} else if (type.equals("episode"))
			;
	}

	/**
	 * Sorts list by title
	 */
	public void sortByTitle() {
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
	public void sortByCountry() {
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
	public void sortByPersonalRating() {
		Collections.sort(movies, new Comparator<Video>() {
			public int compare(Video one, Video other) {
				return Double.compare(other.getPersonalRating(), one.getPersonalRating());
			}
		});
		updateList(movies);
	}

	/**
	 * Sorts list by imdb rating
	 */
	public void sortByImdbRating() {

		Collections.sort(movies, new Comparator<Video>() {
			public int compare(Video one, Video other) {
				return Double.compare(other.getImdbRating(), one.getImdbRating());
			}
		});
		updateList(movies);
	}

	/**
	 * Sorts list by release date
	 */
	public void sortByReleaseDate() {

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
	public void searchAll(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();

		for (Video vid : movies) {
			if (vid.toStringSearch().toLowerCase().contains(searchText.toLowerCase())) {
				results.add(vid);
			}
		}
		updateList(results);
	}

	/**
	 * Search by title and update the list being displayed.
	 * 
	 * @param searchText
	 *            A String for initializing the search
	 */
	public void searchByTitle(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();

		for (Video vid : movies) {
			if (vid.getTitle() != null) {
				if (vid.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
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
	 *            A String for initializing the search
	 */
	public void searchByDirector(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();

		for (Video vid : movies) {
			if (vid.getDirector() != null) {
				if (vid.getDirector().toLowerCase().contains(searchText.toLowerCase())) {
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
	 *            A String for initializing the search
	 */
	public void searchByCountry(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();
		for (Video vid : movies) {
			if (vid.getCountry() != null) {
				if (vid.getCountry().toLowerCase().contains(searchText.toLowerCase())) {
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
	 *            A String for initializing the search
	 */
	public void searchByCast(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();
		for (Video vid : movies) {
			if (vid.getCast() != null) {
				for (String cast : vid.getCast()) {
					if (cast.toLowerCase().contains(searchText.toLowerCase())) {
						results.add(vid);
					}
				}
			}
		}
		updateList(results);
	}

	/**
	 * Search by genre and update the list being displayed.
	 * 
	 * @param searchText
	 *            A String for initializing the search
	 */
	public void searchByGenre(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();
		for (Video vid : movies) {
			if (vid.getGenre() != null) {
				for (String genre : vid.getGenre()) {
					if (genre.toLowerCase().contains(searchText.toLowerCase())) {
						results.add(vid);
					}
				}
			}
		}
		updateList(results);
	}

	/**
	 * Search by writers and update the list being displayed.
	 * 
	 * @param searchText
	 *            A String for initializing the search
	 */
	public void searchByWriters(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();
		for (Video vid : movies) {
			if (vid.getWriters() != null) {
				for (String writer : vid.getWriters()) {
					if (writer.toLowerCase().contains(searchText.toLowerCase())) {
						results.add(vid);
					}
				}
			}
		}
		updateList(results);
	}

	/**
	 * Search by release date and update the list being displayed.
	 * 
	 * @param searchText
	 *            A String for initializing the search
	 */
	public void searchByReleaseDate(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();
		for (Video vid : movies) {
			if (vid.getReleaseDate() != null) {
				if (vid.getReleaseDate().toString().contains(searchText)) {
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
	 *            A String for initializing the search
	 */
	public void searchByPlot(String searchText) {
		ArrayList<Video> results = new ArrayList<Video>();
		for (Video vid : movies) {
			if (vid.getPlotSummary() != null) {
				if (vid.getPlotSummary().toLowerCase().contains(searchText.toLowerCase())) {
					results.add(vid);
				}
			}
		}
		updateList(results);
	}
}
