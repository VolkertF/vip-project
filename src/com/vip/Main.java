package com.vip;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import com.vip.window.VipFrame;

public class Main {

	/** ms rate on which the timeline slider is updated **/
	private static final int UPDATE_RATE = 100;

	public static void main(String[] args) {
		final VipFrame f = new VipFrame();
		URL url = Main.class.getResource("/icon.png");
		if (url != null) {
			Image iconImage = Toolkit.getDefaultToolkit().createImage(url);
			f.setIconImage(iconImage);
		}

		// Update GUI on a regular basis
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(UPDATE_RATE);
						f.updateGUI();
					}
				} catch (InterruptedException e) {
					// TODO Restart Thread, maybe outsource code snippet into
		            // new method
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}
