package com.vip.attributes;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class Video {
	
	private int ID;

	private String filePath;
	
	private String title;
	
	private DateTime releaseDate;
	
	private ArrayList<String> genre;
	
	private String director;
	
	private ArrayList<String> cast;
	
	private ArrayList<String> writers;
	
	private String plotSummary;
	
	private String country;
	
	private double imdbRating;
	
	private double personalRating;
	
	private int season;
	
	private int episode;
	
	
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DateTime getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(DateTime releaseDate) {
		this.releaseDate = releaseDate;
	}


	public ArrayList<String> getGenre() {
		return genre;
	}

	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
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

	public ArrayList<String> getWriters() {
		return writers;
	}

	public void setWriters(ArrayList<String> writers) {
		this.writers = writers;
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

	public double getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(double imdbRating) {
		this.imdbRating = imdbRating;
	}

	public double getPersonalRating() {
		return personalRating;
	}

	public void setPersonalRating(double personalRating) {
		this.personalRating = personalRating;
	}
	
	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getEpisode() {
		return episode;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}
	
}
