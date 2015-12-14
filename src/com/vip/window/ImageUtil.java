package com.vip.window;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * This is a helping class for resizing the Images
 * of the posters for an OMDb-Request.
 * It basically delivers a dynamic way to resize
 * pictures to a unique size.
 * @author Johannes
 *
 */
public class ImageUtil {
	
	/**
	 * Final int to determine which width the
	 * pictures should have
	 */
	public static final int DEST_WIDTH = 250;
	
	/**
	 * Final int to determine which height the
	 * pictures should have
	 */
	public static final int DEST_HEIGHT = 200;
	
	/**
	 * Final double that displays the aspect ratio
	 * based on the given height and width above.
	 */
	public static final double ASPECT_RATIO = (double) DEST_WIDTH / DEST_HEIGHT;
	
	/**
	 * This method does all the resize stuff
	 * @param original
	 * 		The unresized picture
	 * @return
	 * 		The newly resized picture with new dimensions
	 */
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
