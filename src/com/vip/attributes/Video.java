package com.vip.attributes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Video {
	

	private String filePath;
	
	private String title;
	
	private Date releaseDate;
	
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
	
	private boolean infoFetched;
	
	public Video(String path) {
		this.filePath = path;
		String[] temp = path.split(Pattern.quote("\\"));
		this.title = temp[temp.length-1];
		this.infoFetched = false;
	}
	
	public Video(){ }

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

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date date) {
		this.releaseDate = date;
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
	
	public boolean isInfoFetched() {
		return infoFetched;
	}
	
	public void setInfoFetched(boolean infoFetched) {
		this.infoFetched = infoFetched;
	}
	
	public String toString(){
		
		String string = ""
				+ "\n" + this.getFilePath()
				+ "\n" + this.getTitle()
				+ "\n" + this.getReleaseDate()
				+ "\n" + this.getGenre()
				+ "\n" + this.getDirector()
				+ "\n" + this.getCast()
				+ "\n" + this.getWriters()
				+ "\n" + this.getPlotSummary()
				+ "\n" + this.getCountry()
				+ "\n" + this.getImdbRating()
				+ "\n" + this.getPersonalRating()
				+ "\n" + this.getSeason()
				+ "\n" + this.getEpisode()
				+ "\n" + this.isInfoFetched();
		
		
		return string;
	}
}
