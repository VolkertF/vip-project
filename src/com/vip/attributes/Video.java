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
		String[] temp;
		if(path.contains("\\")) {
			temp = path.split(Pattern.quote("\\"));
		} else {
			temp = path.split(Pattern.quote("/"));
		}
		this.title = temp[temp.length - 1];
		this.infoFetched = false;
	}

	public Video() {
	}

	public String getFilePath() {
		if (filePath != null) {
			return filePath;
		} else {
			// well...how would this happen? But for completion's sake...
			return "Unknown";
		}
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTitle() {
		if (title != null) {
			return title;
		} else {
			return "Unknown";
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public String getReleaseDateString() {
		if (releaseDate != null) {
			return releaseDate.toString();
		} else {
			return "Unknown";
		}
	}

	public void setReleaseDate(Date date) {
		this.releaseDate = date;
	}

	public ArrayList<String> getGenre() {
		return genre;
	}

	public String getGenreString() {
		if (genre != null) {
			return genre.toString();
		} else {
			return "Unknown";
		}
	}

	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}

	public String getDirector() {
		if (director != null) {
			return director;
		} else {
			return "Unknown";
		}
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public String getCastString() {
		if (cast != null) {
			return cast.toString();
		} else {
			return "Unknown";
		}
	}

	public void setCast(ArrayList<String> cast) {
		this.cast = cast;
	}

	public ArrayList<String> getWriters() {
		return writers;
	}

	public String getWritersString() {
		if (writers != null) {
			return writers.toString();
		} else {
			return "Unknown";
		}
	}

	public void setWriters(ArrayList<String> writers) {
		this.writers = writers;
	}

	public String getPlotSummary() {
		if (plotSummary != null) {
			return plotSummary;
		} else {
			return "Unknown";
		}
	}

	public void setPlotSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	public String getCountry() {
		if (country != null) {
			return country;
		} else {
			return "Unknown";
		}
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

	public String toString() {

		String string = "File Path: " + this.getFilePath() + "\nTitle: " + this.getTitle() + "\nRelease Date: "
		        + this.getReleaseDateString() + "\nGenre: " + this.getGenreString() + "\nDirector: "
		        + this.getDirector() + "\nCast: " + this.getCastString() + "\nWriters: " + this.getWritersString()
		        + "\nPlot Summary: " + this.getPlotSummary() + "\nCountry: " + this.getCountry() + "\nIMDb Rating: "
		        + this.getImdbRating() + "\nPersonal Rating: " + this.getPersonalRating() / 2 + "\nSeason: "
		        + this.getSeason() + "\nEpisode: " + this.getEpisode() + "\nInfo Fetched: " + this.isInfoFetched();

		return string;
	}
}
