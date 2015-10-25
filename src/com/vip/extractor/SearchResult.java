package com.vip.extractor;

/**
 * This class holds information shared between all kinds of search results.
 * Different search types have different fields. For instance, a general
 * search (like searching for Star Wars) will have a poster property
 * but a search for a list of episodes will not have that property.
 * 
 * @author Cyril Casapao
 */
public class SearchResult {
	
	protected String title;
	protected String imdbId;
	
	public SearchResult(String title, String imdbId) {
		this.title = title;
		this.imdbId = imdbId;
	}
	
	/**
	 * Getter methods.
	 */
	public String getTitle() {
		return title;
	}

	
	public String getImdbId() {
		return imdbId;
	}
}
