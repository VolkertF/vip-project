package com.vip.media;

import java.util.ArrayList;

public class Movie {
	private String name;
	private String path;
	private int rating;
	private ArrayList<String> cast;
	private String description;
	//more to come
	
	public Movie(String path) {
		this.path = path;
		//OMDb asking for the name and the year or so and adding the rest of the data.
	}
}
