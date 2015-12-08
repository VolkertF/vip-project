package com.vip;

import java.awt.event.ComponentListener;

import com.vip.window.VipFrame;

public class Main {

	/** ms rate on which the timeline slider is updated **/
	private static final int UPDATE_RATE = 100;

	public static void main(String[] args) {
		final VipFrame f = new VipFrame();
		f.setVisible(true);
		// Add Resize-listener to video surface
		f.getController().getVLC().getVideoSurface()
		        .addComponentListener((ComponentListener) f.getController().getVLC().getVideoSurface());
		// Update the canvas size on start-up
		f.getController().getVLC().getVideoSurface().updateVideoSurface();

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
