package com.vip.window;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.vip.media.VLC;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

public class MoviePanel extends JPanel implements ComponentListener {

	// just approx.!
	private int overlayTimeSecond = 0;

	private final double ASPECT_RATIO = 16.0 / 9.0;

	private MoviePanel thisPanel = this;

	private int imageWidth = 720;
	private int imageHeight = 360;

	private BufferedImage image;

	private VLC vlcInstance;

	public MoviePanel(VLC vlc) {
		this.setBackground(Color.BLACK);
		vlcInstance = vlc;
		calcAspectRatio();
		image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
		        .createCompatibleImage(imageWidth, imageHeight);
	}

	public MoviePanel(VLC vlc, long time, String mediaPath, boolean shouldPlay) {
		this(vlc);
		lastTime = time;
		currentPath = mediaPath;
		this.shouldPlay = shouldPlay;
	}

	private class MovieBufferFormatCallback implements BufferFormatCallback {
		@Override
		public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
			System.out.println("Ratio of Panel: " + imageWidth + " x " + imageHeight);
			calcAspectRatio();
			System.out.println("Ratio used: " + imageWidth + " x " + imageHeight);
			return new RV32BufferFormat(imageWidth, imageHeight);
		}
	};

	public void calcAspectRatio() {
		if (!vlcInstance.isFullscreen()) {
			if (imageWidth > imageHeight * ASPECT_RATIO) {
				imageWidth = (int) Math.rint(imageHeight * ASPECT_RATIO / 10) * 10;

			} else {
				imageHeight = (int) Math.rint((imageWidth / ASPECT_RATIO) / 10) * 10;
			}
		}
	}

	public DirectMediaPlayerComponent createMediaPlayer() {
		DirectMediaPlayerComponent mediaPlayerComponent = new DirectMediaPlayerComponent(
		        new MovieBufferFormatCallback()) {
			@Override
			protected RenderCallback onGetRenderCallback() {
				return new MovieRenderCallbackAdapter();
			}
		};
		return mediaPlayerComponent;
	}

	private class MovieRenderCallbackAdapter extends RenderCallbackAdapter {

		private MovieRenderCallbackAdapter() {
			super(new int[imageWidth * imageHeight]);
		}

		@Override
		protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
			image.setRGB(0, 0, imageWidth, imageHeight, rgbBuffer, 0, imageWidth);
			thisPanel.repaint();
		}
	}

	private boolean shouldDrawOverlay = true;
	private final Font font = new Font("Arial", Font.BOLD, 40);

	long lastUpdateTime = System.currentTimeMillis();
	int updatedTicks = 0;
	int averageFps = 0;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		updatedTicks++;
		long time = System.currentTimeMillis();
		if (time - lastUpdateTime >= 1000) {
			lastUpdateTime = time;
			averageFps = updatedTicks;
			updatedTicks = 0;
			overlayTimeSecond--;
		}

		if (overlayTimeSecond <= 0) {
			// shouldDrawOverlay = false;
		}

		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.drawImage(image, null, (this.getWidth() - imageWidth) / 2, 0);

		if (shouldDrawOverlay) {
			g2.setFont(font);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			int volume = vlcInstance.getMediaPlayer().getVolume();
			if (volume < 0) {
				volume = 0;
			}
			String strVolume = String.format("Volume: %03d", volume) + " %" + String.format("  %03d", averageFps);
			drawOutlineText(g2, strVolume, image.getWidth(), image.getHeight());
		}
	}

	public void setDrawOverlay(boolean state) {
		shouldDrawOverlay = state;
	}

	public boolean getDrawOverlay() {
		return shouldDrawOverlay;
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
		System.out.println("Coords: " + x + " x " + y);
		g2.transform(affineTransform);
		g2.setColor(Color.WHITE);
		g2.fill(shape);
		g2.setColor(Color.BLACK);
		g2.draw(shape);
	}

	public void updateVideoSurface() {
		if (this.getWidth() != imageWidth && this.getHeight() != imageHeight) {
			// System.out.println("Res: " + imageWidth + " x " + imageHeight);
			imageWidth = this.getWidth();
			imageHeight = this.getHeight();
			image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			        .createCompatibleImage(imageWidth, imageHeight);
			// System.out.println("Image res: " + image.getWidth() + " x " +
			// image.getHeight());
			vlcInstance.invokeMediaPlayerCreation();
		}

	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	private long lastTime = 0;
	private String currentPath = null;
	private boolean shouldPlay = false;

	public void setCurrentMediaPath(String path) {
		currentPath = path;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (vlcInstance.getMediaPlayer() != null && vlcInstance.getCurrentPlaybackPath() != null) {
			// System.out.println("Saved is:" + currentPath + ".\nCorrect is: "
			// + vlcInstance.getCurrentPlaybackPath());
			if (currentPath != vlcInstance.getCurrentPlaybackPath()) {
				currentPath = vlcInstance.getCurrentPlaybackPath();
				lastTime = 0;
				shouldPlay = true;
			} else {
				long currentTime = vlcInstance.getMediaPlayer().getTime();
				if (currentTime > 0L) {
					// System.out.println("Saved is:" + lastTime + ".\nCorrect
					// is: " + currentTime);
					lastTime = currentTime;
				}
				shouldPlay = vlcInstance.getMediaPlayer().isPlaying();
			}

			vlcInstance.stopMedia();
		}
		updateVideoSurface();

		if (currentPath != null) {
			vlcInstance.loadMedia(currentPath);
			vlcInstance.playMedia();
			vlcInstance.getMediaPlayer().setTime(lastTime);
			if (!shouldPlay) {
				vlcInstance.pauseMedia();
			}
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
}
