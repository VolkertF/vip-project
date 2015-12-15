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

@SuppressWarnings("serial")
public class MoviePanel extends JPanel {

	private VLC vlcInstance;

	private final int OVERLAY_DISPLAY_TIME = 3;
	private int overlayTimer = OVERLAY_DISPLAY_TIME;

	private BufferedImage currentImage;

	public MoviePanel(VLC vlc) {
		vlcInstance = vlc;
		this.setBackground(Color.BLACK);
	}

	// public MoviePanel(VLC vlc, long time, String mediaPath, boolean
	// shouldPlay) {
	// this(vlc);
	// lastTime = time;
	// currentPath = mediaPath;
	// this.shouldPlay = shouldPlay;
	// }

	private boolean shouldDrawOverlay = false;
	private boolean drawVolume = false;
	private boolean drawTime = false;
	private boolean drawFps = false;
	private boolean drawTitle = false;
	private boolean isActive = false;

	public void setActive() {
		isActive = true;
	}

	public void setInactive() {
		isActive = false;
	}

	public boolean isActive() {
		return isActive;
	}

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
			if (time - lastUpdateTime >= 1000) {
				lastUpdateTime = time;
				averageFps = updatedTicks;
				updatedTicks = 0;
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
			if (currentImage != null) {
				g2.drawImage(currentImage, null, (this.getWidth() - currentImage.getWidth()) / 2, 0);
			}
			if (shouldDrawOverlay) {
				g2.setFont(font);
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				String toDisplay = "";
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

	public void setCurrentImage(BufferedImage newImage) {
		currentImage = newImage;
	}

	public void setDrawOverlay(boolean state) {
		shouldDrawOverlay = state;
		overlayTimer = OVERLAY_DISPLAY_TIME;
	}

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

	public Rectangle getRectFromString(Graphics2D g2, String text) {
		FontRenderContext fontRendContext = g2.getFontRenderContext();
		TextLayout textLayout = new TextLayout(text, font, fontRendContext);

		Shape shape = textLayout.getOutline(null);
		Rectangle rect = shape.getBounds();
		return rect;
	}

	public void drawOutlineText(Graphics2D g2, String text, int imageWidth, int imageHeight) {
		Rectangle rect = getRectFromString(g2, text);
		drawOutlineText(g2, text, imageWidth, imageHeight, (int) (imageWidth - rect.getWidth() * 1.1),
		        (int) (imageHeight - rect.getHeight() * 1.1));
	}

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
