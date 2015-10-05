package com.vip.attributes;

import java.util.ArrayList;

import org.joda.time.LocalDate;

public class Episode {

	private int season;
	
	private int episode;
	
	private String director;
	
	private ArrayList<String> writers;
	
	private LocalDate firstAired;

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

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public ArrayList<String> getWriters() {
		return writers;
	}

	public void setWriters(ArrayList<String> writers) {
		this.writers = writers;
	}

	public LocalDate getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(LocalDate firstAired) {
		this.firstAired = firstAired;
	}
	
	
	
}
