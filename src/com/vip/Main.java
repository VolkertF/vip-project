package com.vip;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import com.vip.window.VipFrame;

public class Main {

	/** ms rate on which the timeline slider is updated **/
	private static final int UPDATE_RATE = 100;

	public static void main(String[] args) {
		URL url = Main.class.getResource("/Icon.png");
		Image iconImage = Toolkit.getDefaultToolkit().createImage(url);
		final VipFrame f = new VipFrame();
		f.setIconImage(iconImage);
		// Update the canvas size on start-up

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
