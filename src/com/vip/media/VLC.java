package com.vip.media;

import java.awt.image.BufferedImage;

import com.vip.attributes.Video;
import com.vip.window.MoviePanel;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

/**
 * VLC Class. Controls media playback.
 */
public class VLC {

	private final double ASPECT_RATIO = 16.0 / 9.0;

	/** Minimum volume supported (0 is least) **/
	private static final int MIN_VOLUME = 0;

	/** Maximum volume supported (200 is VLC max) **/
	private static final int MAX_VOLUME = 200;

	/** Rate of volume change when pressing a shortcut **/
	private static final int VOLUME_STEPS = 10;

	/**
	 * Percentage of how far a jump in the movie will proceed, must be between 0
	 * and 1
	 **/
	private static final double JUMP_PERCENTAGE = 0.05;

	private static boolean vlcFound;

	private static VLC instance;

	private int lastVolume = (MAX_VOLUME + MIN_VOLUME) / 2;

	private DirectMediaPlayerComponent directMediaPlayerComponent;

	/** The mediaplayer that is responsible for playback **/
	private DirectMediaPlayer directMediaPlayer;

	/**
	 * True on Initialization of a media
	 */
	private boolean initMedia = true;

	private boolean isMuted = false;

	private Video currentVideo;

	private BufferedImage currentImage;

	private MoviePanel currentPanel;

	private boolean isPlaying = false;

	private VLC() {
		vlcFound = new NativeDiscovery().discover();
		// If VLC cannot be found, we will inform the user of manual
		// possibilities

		if (!vlcFound) {
			// TODO VLC not found, open JDialog and give hint to manually add
			// the path
		}
	}

	public static VLC getInstance() {
		if (instance == null) {
			VLC.instance = new VLC();
		}
		return instance;
	}

	public Video getCurrentVideo() {
		return currentVideo;
	}

	/**
	 * Returns wether or not the GUI needs to rebuild for a new media to play
	 * 
	 * @return <code>true</code> if the media should be initialized<br />
	 *         <code>false</code> if the media is not to be inizialized
	 */
	public boolean shouldInitProgressBar() {
		return initMedia;
	}

	public boolean isVLCInstalled() {
		return vlcFound;
	}

	public void setProgressBarInitState(boolean newInit) {
		initMedia = newInit;
	}

	/**
	 * Getter method of maximum supported volume
	 * 
	 * @return the maximum supported volume
	 */
	public static int getMaxVolume() {
		return MAX_VOLUME;
	}

	/**
	 * Getter method of minimum supported volume
	 * 
	 * @return the minimum supported volume
	 */
	public static int getMinVolume() {
		return MIN_VOLUME;
	}

	/**
	 * Getter method of the mediaplayer
	 * 
	 * @return the mediaplayer
	 */
	public DirectMediaPlayer getMediaPlayer() {
		if (vlcFound) {
			return directMediaPlayer;
		} else {
			return null;
		}

	}

	/**
	 * Loads a media file into the mediaplayer
	 * 
	 * @param media_path
	 *            Path to the media file to be loaded
	 */
	private void loadMedia(Video videoInstance) {
		if (vlcFound && directMediaPlayer != null) {
			String mediaPath = videoInstance.getFilePath();
			directMediaPlayer.prepareMedia(mediaPath);
			setProgressBarInitState(true);
		}
	}

	public void switchMediaFile(Video videoInstance) {
		stopMedia();
		loadMedia(videoInstance);
		playMedia();
		currentVideo = videoInstance;
	}

	/**
	 * Toggles media playback
	 */
	public void toggleMediaPlayback() {
		if (vlcFound && directMediaPlayer != null) {
			if (getMediaPlayer().isPlaying()) {
				pauseMedia();
			} else {
				playMedia();
			}
		}
	}

	/**
	 * Starts media playback
	 */
	public void playMedia() {
		if (vlcFound && directMediaPlayer != null) {
			directMediaPlayer.play();
			isPlaying = true;
		}

	}

	/**
	 * Pauses media playback
	 */
	public void pauseMedia() {
		if (vlcFound && directMediaPlayer != null) {
			directMediaPlayer.pause();
			isPlaying = false;
		}

	}

	/**
	 * Stops media playback
	 */
	public void stopMedia() {
		if (vlcFound && directMediaPlayer != null) {
			directMediaPlayer.stop();
			isPlaying = false;
		}

	}

	/**
	 * Jumps to the next chapter
	 */
	public void nextChapter() {
		if (vlcFound && directMediaPlayer != null)
			if (directMediaPlayer.getChapterCount() == 0) {
				jumpForward();
			} else {
				directMediaPlayer.nextChapter();
			}
	}

	/**
	 * Jumps to the previous Chapter
	 */
	public void previousChapter() {
		if (vlcFound && directMediaPlayer != null)
			if (directMediaPlayer.getChapterCount() == 0) {
				jumpBack();
			} else {
				directMediaPlayer.previousChapter();
			}

	}

	/**
	 * Jumps forward in the media file a given percentage
	 */
	public void jumpForward() {
		if (vlcFound && directMediaPlayer != null && directMediaPlayer.getLength() != -1) {
			if (JUMP_PERCENTAGE >= 0 && JUMP_PERCENTAGE <= 1) {
				int changeRate = (int) (directMediaPlayer.getLength() * JUMP_PERCENTAGE);
				int newTime = (int) (directMediaPlayer.getTime() + changeRate);
				if (newTime >= directMediaPlayer.getLength()) {
					stopMedia();
				} else {
					directMediaPlayer.setTime(newTime);

				}
			}
		}
	}

	/**
	 * Jumps back in the media file a given percentage
	 */
	public void jumpBack() {
		if (vlcFound && directMediaPlayer != null && directMediaPlayer.getLength() != -1) {
			if (JUMP_PERCENTAGE >= 0 && JUMP_PERCENTAGE <= 1) {
				int changeRate = (int) (directMediaPlayer.getLength() * JUMP_PERCENTAGE);
				int newTime = (int) (directMediaPlayer.getTime() - changeRate);
				if (newTime < 0)
					newTime = 0;
				directMediaPlayer.setTime(newTime);
			}
		}
	}

	public boolean isMuted() {
		return isMuted;
	}

	public void toggleMuted() {
		if (isMuted) {
			if (vlcFound && directMediaPlayer != null) {
				isMuted = false;
				directMediaPlayer.setVolume(lastVolume);
			}
		} else {
			if (vlcFound && directMediaPlayer != null) {
				isMuted = true;
				directMediaPlayer.setVolume(0);
			}
		}
	}

	public int getlastVolume() {
		return lastVolume;
	}

	public void setVolume(int newVolume) {
		if (vlcFound && directMediaPlayer != null && directMediaPlayer.getLength() != -1) {
			if (newVolume > MAX_VOLUME) {
				newVolume = MAX_VOLUME;
			} else if (newVolume < MIN_VOLUME) {
				newVolume = MIN_VOLUME;
			}
			if (newVolume > MIN_VOLUME) {
				isMuted = false;
				lastVolume = newVolume;
			} else {
				isMuted = true;
			}
			directMediaPlayer.setVolume(newVolume);
		}
	}

	public int getVolume() {
		if (vlcFound && directMediaPlayer != null) {
			return directMediaPlayer.getVolume();
		} else {
			return 0;
		}
	}

	public int getVolumeSteps() {
		return VOLUME_STEPS;
	}

	public String getFormattedTimeToString() {
		String strUpdatedTime = null;
		Double procentualProgress = ((double) directMediaPlayer.getTime() / directMediaPlayer.getLength()) * 100;
		// is newTime is not a valid Number, we display a default Text
		int hoursPassed = 0;
		int minutesPassed = 0;
		int secondsPassed = 0;
		int hoursTotal = 0;
		int minutesTotal = 0;
		int secondsTotal = 0;
		hoursPassed = (int) (directMediaPlayer.getTime() / 3600000);
		minutesPassed = (int) (directMediaPlayer.getTime() / 60000 % 60);
		secondsPassed = (int) (directMediaPlayer.getTime() / 1000 % 60);
		hoursTotal = (int) (directMediaPlayer.getLength() / 3600000);
		minutesTotal = (int) (directMediaPlayer.getLength() / 60000 % 60);
		secondsTotal = (int) (directMediaPlayer.getLength() / 1000 % 60);
		if (procentualProgress.isNaN() || procentualProgress.isInfinite()) {
			strUpdatedTime = String.format("%02d:%02d:%02d / %02d:%02d:%02d   000,0", hoursPassed, minutesPassed,
			        secondsPassed, hoursTotal, minutesTotal, secondsTotal, procentualProgress);
		} else {
			if (procentualProgress > 100) {
				procentualProgress = 100.0;
			}
			strUpdatedTime = String.format("%02d:%02d:%02d / %02d:%02d:%02d   %4.1f", hoursPassed, minutesPassed,
			        secondsPassed, hoursTotal, minutesTotal, secondsTotal, procentualProgress);
		}
		return strUpdatedTime;
	}

	private int imageWidth;
	private int imageHeight;

	public void switchSurface(MoviePanel newSurface, boolean safeTime) {
		long time = 0;
		if (directMediaPlayer != null) {
			if (safeTime) {
				time = directMediaPlayer.getTime();
			}
			directMediaPlayer.stop();
		}
		if (currentPanel != null) {
			currentPanel.setInactive();
		}
		currentPanel = newSurface;
		if (currentPanel != null) {
			currentPanel.setActive();
			calcAspectRatio();
			currentImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
			directMediaPlayerComponent = new DirectMediaPlayerComponent(new MovieBufferFormatCallback()) {
				@Override
				protected RenderCallback onGetRenderCallback() {
					return new MovieRenderCallbackAdapter();
				}
			};
			directMediaPlayer = directMediaPlayerComponent.getMediaPlayer();
			if (currentVideo != null) {
				switchMediaFile(currentVideo);
				directMediaPlayer.setTime(time);
			}
		}
	}

	private class MovieBufferFormatCallback implements BufferFormatCallback {
		@Override
		public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
			return new RV32BufferFormat(imageWidth, imageHeight);
		}
	};

	private class MovieRenderCallbackAdapter extends RenderCallbackAdapter {

		private MovieRenderCallbackAdapter() {
			super(new int[imageWidth * imageHeight]);
		}

		@Override
		protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
			currentImage.setRGB(0, 0, imageWidth, imageHeight, rgbBuffer, 0, imageWidth);
			currentPanel.setCurrentImage(currentImage);
			currentPanel.repaint();
		}
	}

	public void calcAspectRatio() {
		imageWidth = currentPanel.getWidth();
		imageHeight = currentPanel.getHeight();
		if (imageWidth > imageHeight * ASPECT_RATIO) {
			imageWidth = (int) Math.rint(imageHeight * ASPECT_RATIO / 10) * 10;

		} else {
			imageHeight = (int) Math.rint((imageWidth / ASPECT_RATIO) / 10) * 10;
		}
	}
}
