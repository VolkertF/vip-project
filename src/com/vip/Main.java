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

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(10);
						f.update_timeline();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		t.start();

		return;
	}
}
