package com.vip;

import com.vip.media.VLC;
import com.vip.window.VipFrame;

public class Main {

	/** ms rate on which the timeline slider is updated **/
	private static final int UPDATE_RATE = 250;

	public static void main(String[] args) {
		final VipFrame f = new VipFrame();
		// String loaded_movie_file = "G:\\Videos\\Filme\\Fanboys.avi";
		String loaded_movie_file = "F:\\Dji. Death Sails-HD.mp4";
		VLC.loadMedia(loaded_movie_file);
		f.setVisible(true);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(UPDATE_RATE);
						if (VLC.getMediaPlayer() != null && VLC.getMediaPlayer().getLength() != -1) {
							f.updateTimeline();
						}
					}
				} catch (InterruptedException e) {
					// TODO Restart Thread, maybe outsource code snippet into new method
					e.printStackTrace();
				}

			}
		});
		t.start();

		return;
	}
}
