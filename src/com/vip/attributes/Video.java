package com.vip.attributes;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.vip.window.ContextVideoMenu;

public class Video {

	public Video(String path, String title) {
		this.setPath(path);
		this.title = title;
	}
	
	private ContextVideoMenu contVideoMenu = new ContextVideoMenu(this);
	
	private String path;
	
	private String title;
	
	private String rating;
	
	private String director;
	
	private ArrayList<String> cast;
	
	private String plotSummary;
	
	private String country;
	
	private String imdbRating;
	
	private String personalRating;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setContextVideoMenu(MouseEvent ev) {
		this.contVideoMenu.setLocation(ev.getLocationOnScreen());
		this.contVideoMenu.setVisible(true);
	}

}
