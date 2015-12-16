package com.vip.window;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.vip.media.VLC;

/**
 * A Panel that is used as Canvas for movie rendering. The Panel is able to draw
 * an overlay based on the played movie and calculate avrg FPS.
 * 
 * @author Fabian
 *
 */
@SuppressWarnings("serial")
public class MoviePanel extends JPanel {

	/** Reference to the vlc instance **/
	private VLC vlcInstance;

	/** How long the Overlay should be shown **/
	private final int OVERLAY_DISPLAY_TIME = 3;
	/** The Time that is left until the overlay will be hidden **/
	private int overlayTimer = OVERLAY_DISPLAY_TIME;

	/** a BufferedImage that the movie is rendered on **/
	private BufferedImage currentImage;

	/**
	 * Constructor for the MoviePanel. In general just creates a panel and sets
	 * its background
	 * 
	 * @param vlc
	 *            instance of the vlc class that contains playback information
	 */
	public MoviePanel(VLC vlc) {
		vlcInstance = vlc;
		this.setBackground(Color.BLACK);
	}

	/** Indicates wether or not the overlay should be drawn **/
	private boolean shouldDrawOverlay = false;
	/** Indicates wether or not the volume level should be drawn **/
	private boolean drawVolume = true;
	/** Indicates wether or not the current movie time should be drawn **/
	private boolean drawTime = true;
	/** Indicates wether or not the average FPS should be drawn **/
	private boolean drawFps = false;
	/** Indicates wether or not the movie's title should be drawn **/
	private boolean drawTitle = true;
	/** Indicates wether or not this panel is active **/
	private boolean isActive = false;

	/**
	 * Sets this Panel active
	 */
	public void setActive() {
		isActive = true;
	}

	/**
	 * Sets this panel inactive
	 */
	public void setInactive() {
		isActive = false;
	}

	/**
	 * Give back, wether or not this panel is currently active
	 * 
	 * @return the active state of this panel
	 */
	public boolean isActive() {
		return isActive;
	}

	/** Font that is used for overlay information drawing **/
	private final Font font = new Font("Tahoma", Font.BOLD, 40);

	long lastUpdateTime = System.currentTimeMillis();
	int updatedTicks = 0;
	int averageFps = 0;

	@Override
	public void paintComponent(Graphics g) {
		if (isActive) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			updatedTicks++;
			long time = System.currentTimeMillis();
			// Every second some checks are done, like:
			if (time - lastUpdateTime >= 1000) {
				// Calculate average FPS
				lastUpdateTime = time;
				averageFps = updatedTicks;
				updatedTicks = 0;
				// Count overlay timer down, if active
				if (shouldDrawOverlay) {
					overlayTimer--;
					if (overlayTimer <= 0) {
						shouldDrawOverlay = false;
						overlayTimer = OVERLAY_DISPLAY_TIME;
					}
				}
			}
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			// We draw the movie if possible
			if (currentImage != null) {
				g2.drawImage(currentImage, null, (this.getWidth() - currentImage.getWidth()) / 2, 0);
			}
			// We check for different overlay flags and draw them if set
			if (shouldDrawOverlay) {
				g2.setFont(font);
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				String toDisplay = "";
				if (vlcInstance.getCurrentVideo() != null) {
					if (drawTitle) {
						toDisplay += String.format("\"%.40s\" ", vlcInstance.getCurrentVideo().getTitle());
					}
					if (drawTime) {
						String tmpString = vlcInstance.getFormattedTimeToString();
						tmpString = tmpString.substring(0, tmpString.lastIndexOf(" "));
						toDisplay += tmpString + " ";
					}
					if (drawVolume) {
						int volume = vlcInstance.getMediaPlayer().getVolume();
						if (volume < 0) {
							volume = 0;
						}
						toDisplay += String.format("Vol.: %03d", volume) + "% ";
					}
					if (drawFps) {
						toDisplay += String.format("%03dFps ", averageFps);
					}
					if (currentImage != null) {
						drawOutlineText(g2, toDisplay, currentImage.getWidth(), currentImage.getHeight());
					}
				}
			}
		}
	}

	/**
	 * Is Responsible for setting the next frame to be drawn
	 * 
	 * @param newImage
	 *            the new Image that will be drawn next
	 */
	public void setCurrentImage(BufferedImage newImage) {
		currentImage = newImage;
	}

	/**
	 * Sets the overlay state
	 * 
	 * @param state
	 *            Wether or not the overlay will be drawn
	 */
	public void setDrawOverlay(boolean state) {
		shouldDrawOverlay = state;
		overlayTimer = OVERLAY_DISPLAY_TIME;
	}

	/**
	 * The Overlay state
	 * 
	 * @return Wether or not the overlay is drawn
	 */
	public boolean getDrawOverlay() {
		return shouldDrawOverlay;
	}

	public void setVolumeDisplayState(boolean state) {
		drawVolume = state;
	}

	public void setTitleDisplayState(boolean state) {
		drawTitle = state;
	}

	public void setFpsDisplayState(boolean state) {
		drawFps = state;
	}

	public void setTimeDisplayState(boolean state) {
		drawTime = state;
	}

	public void setDisplayStates(boolean stateVolume, boolean stateTime, boolean stateFps, boolean stateTitle) {
		drawVolume = stateVolume;
		drawTime = stateTime;
		drawFps = stateFps;
		drawTitle = stateTitle;
	}

	/**
	 * Returns rectangle bounds to a given text.
	 * 
	 * @param g2
	 *            the graphics object that calculates the boundaries
	 * @param text
	 *            the text, which's boundaries are to be calculated
	 * @return A rectangle of the size of the text
	 */
	public Rectangle getRectFromString(Graphics2D g2, String text) {
		FontRenderContext fontRendContext = g2.getFontRenderContext();
		TextLayout textLayout = new TextLayout(text, font, fontRendContext);

		Shape shape = textLayout.getOutline(null);
		Rectangle rect = shape.getBounds();
		return rect;
	}

	/**
	 * Draws an outlined text on a graphics object onto the bottom line with
	 * right alignment
	 * 
	 * @param g2
	 *            the graphics object that is drawn on
	 * @param text
	 *            the text to be drawn
	 * @param imageWidth
	 *            Width of the image that is drawn on
	 * @param imageHeight
	 *            Height of the image that is drawn on
	 */
	public void drawOutlineText(Graphics2D g2, String text, int imageWidth, int imageHeight) {
		Rectangle rect = getRectFromString(g2, text);
		drawOutlineText(g2, text, imageWidth, imageHeight, (int) (imageWidth - rect.getWidth() * 1.1),
		        (int) (imageHeight - rect.getHeight() * 1.1));
	}

	/**
	 * Draws an outlined text on a graphics object at given coordinates with
	 * right alignment
	 * 
	 * @param g2
	 *            the graphics object that is drawn on
	 * @param text
	 *            the text to be drawn
	 * @param imageWidth
	 *            Width of the image that is drawn on
	 * @param imageHeight
	 *            Height of the image that is drawn on
	 * @param x
	 *            Most left coordinate on the image that will be drawn on
	 * @param y
	 *            Most up coordinate on the image that will be drawn on
	 */
	public void drawOutlineText(Graphics2D g2, String text, int imageWidth, int imageHeight, int x, int y) {
		FontRenderContext fontRendContext = g2.getFontRenderContext();
		TextLayout textLayout = new TextLayout(text, font, fontRendContext);

		Shape shape = textLayout.getOutline(null);

		g2.setStroke(new BasicStroke(2.0f));
		AffineTransform affineTransform = new AffineTransform();
		affineTransform = g2.getTransform();
		affineTransform.translate(x, y);
		g2.transform(affineTransform);
		g2.setColor(Color.WHITE);
		g2.fill(shape);
		g2.setColor(Color.BLACK);
		g2.draw(shape);
	}
}
