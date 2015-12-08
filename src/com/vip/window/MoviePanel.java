package com.vip.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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

	private MoviePanel thisPanel = this;

	private int width = 720;
	private int height = 360;

	private BufferedImage image;

	private VLC vlcInstance;

	public MoviePanel(VLC vlc) {
		this.setBackground(Color.BLACK);
		vlcInstance = vlc;
		image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
		        .createCompatibleImage(width, height);
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
			// System.out.println("BufferFormat: " + width + " x " + height);
			return new RV32BufferFormat(width, height);
		}
	};

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
			super(new int[width * height]);
			// System.out.println("Buffersize: " + width + " x " + height);
		}

		@Override
		protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
			image.setRGB(0, 0, width, height, rgbBuffer, 0, width);
			thisPanel.repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.drawImage(image, null, 0, 0);
	}

	public void updateVideoSurface() {
		if (this.getWidth() != width && this.getHeight() != height) {
			// System.out.println("Res: " + width + " x " + height);
			width = this.getWidth();
			height = this.getHeight();
			image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			        .createCompatibleImage(width, height);
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
