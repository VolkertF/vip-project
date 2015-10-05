package com.vip.attributes;

import java.util.ArrayList;

public class Video {


	private String title;
	
	private String rating;
	
	private String director;
	
	private ArrayList<String> cast;
	
	private String plotSummary;
	
	private String country;
	
	private String imdbRating;
	
	private String personalRating;

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public void setCast(ArrayList<String> cast) {
		this.cast = cast;
	}

	public String getPlotSummary() {
		return plotSummary;
	}

	public void setPlotSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getPersonalRating() {
		return personalRating;
	}

	public void setPersonalRating(String personalRating) {
		this.personalRating = personalRating;
	}
	
	

}
