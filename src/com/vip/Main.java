package com.vip;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import com.vip.window.VipFrame;

public class Main {

	public static void main(String[] args) {
		final VipFrame f = new VipFrame();
		URL url = Main.class.getResource("/icon.png");
		if (url != null) {
			Image iconImage = Toolkit.getDefaultToolkit().createImage(url);
			f.setIconImage(iconImage);
		}
	}
}
