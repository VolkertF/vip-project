package com.vip.extractor;

/**
 * This class is used to hold information about API search results.
 * Search results may be a movie or a series.
 * 
 * @author Cyril Casapao
 */
public class SearchResult {
	
	private String title;
	private String year;
	private String id;
	private String poster;
	
	
	/**
	 * Constructor method.
	 * 
	 * @param title		The title of the movie/series
	 * @param year		The year the movie came out or the years the
	 * 					series was active
	 * @param id		The IMDB ID that can be used for a more
	 * 					specific query
	 * @param poster	The URL of an image of the movie/series poster
	 */
	public SearchResult(String title, String year, String id, String poster) {
		this.title = title;
		this.year = year;
		this.id = id;
		this.poster = poster;
	}
	
	
	/**
	 * Getter methods.
	 */
	public String getTitle() {
		return title;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPoster() {
		return poster;
	}
}
