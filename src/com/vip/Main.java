package com.vip;

import com.vip.media.VLC;
import com.vip.window.VipFrame;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		VipFrame f = new VipFrame();
		String loaded_movie_file = "F:\\Dji. Death Sails-HD.mp4";
		VLC.load_movie(loaded_movie_file);
		f.setVisible(true);
		return;
	}
}
