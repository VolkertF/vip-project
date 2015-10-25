package com.vip.extractor;

/**
 * This class is used to hold information about API search results.
 * Search results may be a movie or a series.
 * 
 * @author Cyril Casapao
 */
public class GeneralSearchResult extends SearchResult {
	
	protected String year;
	protected String poster;
	
	
	/**
	 * Constructor method.
	 * 
	 * @param title			The title of the movie/series
	 * @param year			The year the movie came out or the years the
	 * 						series was active
	 * @param imdbId		The IMDB ID that can be used for a more
	 * 						specific query
	 * @param poster		The URL of an image of the movie/series poster
	 */
	public GeneralSearchResult(String title, 
			String year, 
			String imdbId, 
			String poster
	) 
	{
		super(title, imdbId);
		this.year = year;
		this.poster = poster;
	}


	/**
	 * Getter methods.
	 */
	public String getYear() {
		return year;
	}

	public String getPoster() {
		return poster;
	}
}
