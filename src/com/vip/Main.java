package com.vip;

import com.vip.window.VipFrame;

public class Main {

	/** ms rate on which the timeline slider is updated **/
	private static final int UPDATE_RATE = 250;

	public static void main(String[] args) {
		final VipFrame f = new VipFrame();
		f.setVisible(true);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(UPDATE_RATE);
						if (f.getController().getVLC().getMediaPlayer() != null
		                        && f.getController().getVLC().getMediaPlayer().getLength() != -1) {
							f.updateGUI();
						}
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
