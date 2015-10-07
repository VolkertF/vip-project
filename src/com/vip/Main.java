package com.vip;

import com.vip.media.VLC;
import com.vip.window.VipFrame;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		final VipFrame f = new VipFrame();
		String loaded_movie_file = "G:\\Videos\\Filme\\Fanboys.avi";
		VLC.load_movie(loaded_movie_file);
		f.setVisible(true);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(250);	//Fourth a second is enough for the timeline to update with a visual difference.
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
