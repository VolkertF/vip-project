package com.vip.extractor;


/**
 * This class is used to hold information about episodes that appear in
 * an API episode search.
 * 
 * @author Cyril Casapao
 */
public class EpisodeListResult extends SearchResult {

	protected int episodeNumber;
	protected String releaseDate;
	protected double imdbRating;
	
	
	/**
	 * Constructor method.
	 * 
	 * @param title				The title of the series
	 * @param episodeNumber		The number of this episode in the season
	 * 							it came out
	 * @param releaseDate		The date this episode first aired
	 * @param imdbId			The IMDB ID that can be used for a more
	 * 							specific query
	 * @param imdbRating		The rating IMDB gave this episode
	 */
	public EpisodeListResult(
			String title, 
			int episodeNumber,
			String releaseDate,
			String imdbId,
			double imdbRating
	)
	{
		super(title, imdbId);
		this.episodeNumber = episodeNumber;
		this.releaseDate = releaseDate;
		this.imdbRating = imdbRating;
	}

	
	/**
	 * Getter methods.
	 */

	public int getEpisodeNumber() {
		return episodeNumber;
	}


	public String getReleaseDate() {
		return releaseDate;
	}


	public double getImdbRating() {
		return imdbRating;
	}

}
