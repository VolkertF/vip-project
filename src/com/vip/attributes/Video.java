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

	private boolean hasChanged = false;

	/**
	 * Used to instantiate Video with a file path
	 * 
	 * @param path
	 */
	public Video(String path) {
		this.filePath = path;
		String[] temp;
		if (path.contains("\\")) {
			temp = path.split(Pattern.quote("\\"));
		} else {
			temp = path.split(Pattern.quote("/"));
		}
		this.title = temp[temp.length - 1];
		this.infoFetched = false;
	}

	/**
	 * Default constructor
	 */
	public Video() {
	}

	/**
	 * 
	 * @return filePath
	 */
	public String getFilePath() {
		if (filePath != null) {
			return filePath;
		} else {
			// well...how would this happen? But for completion's sake...
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 
	 * @return title
	 */
	public String getTitle() {
		if (title != null) {
			return title;
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return releaseDate
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getReleaseDateString() {
		if (releaseDate != null) {
			return releaseDate.toString();
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param date
	 */
	public void setReleaseDate(Date date) {
		this.releaseDate = date;
	}

	/**
	 * 
	 * @return genre
	 */
	public ArrayList<String> getGenre() {
		return genre;
	}

	/**
	 * 
	 * @return genreString
	 */
	public String getGenreString() {
		if (genre != null) {
			return genre.toString();
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param genre
	 */
	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}

	/**
	 * 
	 * @return director
	 */
	public String getDirector() {
		if (director != null) {
			return director;
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param director
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * 
	 * @return cast
	 */
	public ArrayList<String> getCast() {
		return cast;
	}

	/**
	 * 
	 * @return castString
	 */
	public String getCastString() {
		if (cast != null) {
			return cast.toString();
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param cast
	 */
	public void setCast(ArrayList<String> cast) {
		this.cast = cast;
	}

	/**
	 * 
	 * @return writers
	 */
	public ArrayList<String> getWriters() {
		return writers;
	}

	/**
	 * 
	 * @return writersString
	 */
	public String getWritersString() {
		if (writers != null) {
			return writers.toString();
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param writers
	 */
	public void setWriters(ArrayList<String> writers) {
		this.writers = writers;
	}

	/**
	 * 
	 * @return plotSummary
	 */
	public String getPlotSummary() {
		if (plotSummary != null) {
			return plotSummary;
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param plotSummary
	 */
	public void setPlotSummary(String plotSummary) {
		this.plotSummary = plotSummary;
	}

	/**
	 * 
	 * @return country
	 */
	public String getCountry() {
		if (country != null) {
			return country;
		} else {
			return "Unknown";
		}
	}

	/**
	 * 
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @return imdbRating
	 */
	public double getImdbRating() {
		return imdbRating;
	}

	/**
	 * 
	 * @param rating
	 */
	public void setImdbRating(double rating) {
		this.imdbRating = rating;
	}

	/**
	 * 
	 * @return personalRating
	 */
	public double getPersonalRating() {
		return personalRating;
	}

	/**
	 * 
	 * @param personalRating
	 */
	public void setPersonalRating(double personalRating) {
		if (personalRating < 0) {
			this.personalRating = 0;
		} else if (personalRating > 20) {
			this.personalRating = 20;
		} else {
			this.personalRating = personalRating;
		}
	}

	/**
	 * 
	 * @return season number
	 */
	public int getSeason() {
		return season;
	}

	/**
	 * 
	 * @param season
	 */
	public void setSeason(int season) {
		this.season = season;
	}

	/**
	 * 
	 * @return episode number
	 */
	public int getEpisode() {
		return episode;
	}

	/**
	 * 
	 * @param episode
	 */
	public void setEpisode(int episode) {
		this.episode = episode;
	}

	/**
	 * 
	 * @return isInfoFetched
	 */
	public boolean isInfoFetched() {
		return infoFetched;
	}

	/**
	 * 
	 * @param infoFetched
	 */
	public void setInfoFetched(boolean infoFetched) {
		this.infoFetched = infoFetched;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setChanged(boolean newState) {
		hasChanged = newState;
	}

	/**
	 * Sets all attributes of video to another video
	 * 
	 * @param vid
	 */
	public void setAsVideo(Video vid) {
		this.setTitle(vid.getTitle());
		this.setReleaseDate(vid.getReleaseDate());
		this.setGenre(vid.getGenre());
		this.setDirector(vid.getDirector());
		this.setCast(vid.getCast());
		this.setWriters(vid.getWriters());
		this.setPlotSummary(vid.getPlotSummary());
		this.setCountry(vid.getCountry());
		this.setImdbRating(vid.getImdbRating());
		this.setPersonalRating(vid.getPersonalRating());
		this.setSeason(vid.getSeason());
		this.setEpisode(vid.getEpisode());
		this.setInfoFetched(vid.isInfoFetched());
	}

	/**
	 * To string override for generic object
	 */
	public String toString() {

		return this.getTitle();
	}

	/**
	 * The toString method used for searching every part of the video
	 * 
	 * @return searchString
	 */
	public String toStringSearch() {
		String string = "" + "\n" + this.getFilePath() + "\n" + this.getTitle() + "\n" + this.getReleaseDate() + "\n"
		        + this.getGenre() + "\n" + this.getDirector() + "\n" + this.getCast() + "\n" + this.getWriters() + "\n"
		        + this.getPlotSummary() + "\n" + this.getCountry() + "\n" + this.getImdbRating() + "\n"
		        + this.getPersonalRating() + "\n" + this.getSeason() + "\n" + this.getEpisode() + "\n"
		        + this.isInfoFetched();
		return string;
	}

	/**
	 * The toString used for displaying the video in the GUI
	 * 
	 * @return fullString
	 */
	public String toStringFull() {
		String string = "File Path: " + this.getFilePath() + "\n\nTitle: " + this.getTitle() + "\n\nRelease Date: " + this.getReleaseDateString()
		        + "\t\t\tCountry: " + this.getCountry() + "\n\nGenre: "  + this.getGenreString() + "\t\t\tDirector: " + this.getDirector() + "\n\nCast: " + this.getCastString()
		        + "\n\nWriters: " + this.getWritersString() + "\n\nIMDb Rating: " + this.getImdbRating()
		        + "\t\t\tPersonal Rating: " + this.getPersonalRating() / 2 + "\n\nSeason: " + this.getSeason()
		        + "\t\t\tEpisode: " + this.getEpisode() + "\n\nPlot Summary: " + this.getPlotSummary();

		return string;
	}
}
