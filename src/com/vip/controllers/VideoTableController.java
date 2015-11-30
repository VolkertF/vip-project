package com.vip.controllers;

import java.util.ArrayList;

import com.vip.attributes.Video;
import com.vip.database.VideoTable;

public class VideoTableController {
	
	

	public ArrayList<Video> startProgram(){
		
		VideoTable table = new VideoTable();
		
		table.createTable();
		
		ArrayList<Video> videos = table.loadVideos();
		
		return videos;
	}

	
	public void save(Video video){
		
		VideoTable table = new VideoTable();
		
		table.saveVideo(video);
	}
	
	public void saveAll(ArrayList<Video> videos){
		
		VideoTable table = new VideoTable();
		
		for(Video video:videos){
			table.saveVideo(video);
		}
	}
	
	public static void main(String[] args){
		
		VideoTableController controller = new VideoTableController();
		
		ArrayList<Video> videos = controller.startProgram();
		
		for(Video vid:videos){
			System.out.println(vid.toString());
		}
		
		Video video = new Video("/this/works.avi");
		video.setDirector("Whomever");
		video.setCountry("USA");
		
		controller.save(video);
		
		Video video2 = new Video("/for/the/loveofgod.mp4");
		video2.setImdbRating(3.3);
		video2.setDirector("Tarantino?");
		
		controller.save(video2);
	}
}
