package com.vip.attributes;

import java.util.ArrayList;

public class Movie extends Video{

	private int yearReleased;
	
	private String director;
	
	private ArrayList<String> writers;
	
	private String genre;

	public int getYearReleased() {
		return yearReleased;
	}

	public void setYearReleased(int yearReleased) {
		this.yearReleased = yearReleased;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public ArrayList<String> getWriter() {
		return writers;
	}

	public void setWriter(ArrayList<String> writer) {
		this.writers = writer;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}
