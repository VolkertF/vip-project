package com.vip.window;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageUtil {
	public static final int DEST_WIDTH = 90;
	public static final int DEST_HEIGHT = 75;
	public static final double ASPECT_RATIO = (double) DEST_WIDTH / DEST_HEIGHT;
	
	public static BufferedImage createScaledImage(BufferedImage original) {
		double originalAspectRatio = (double) original.getWidth() / original.getHeight();
		double scale = originalAspectRatio > ASPECT_RATIO ? (double) DEST_WIDTH / original.getWidth() : (double) DEST_HEIGHT / original.getHeight();
		int newHeight = (int) (original.getHeight() * scale);
		int newWidth = (int) (original.getWidth() * scale);
		BufferedImage img = new BufferedImage(DEST_WIDTH, DEST_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(original, 0, 0, newWidth, newHeight, null);
		g.dispose();
		return img;
	}
}
