package com.vip.media;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.vip.attributes.Video;
import com.vip.window.MoviePanel;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;

/**
 * VLC Class. Controls media playback. Holds the media player and information
 * about general playback behaviour.
 */
public class VLC {

	/**
	 * When not run in fullscreen Mode, the movie will be displayed considering
	 * this aspect ratio
	 **/
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

	/**
	 * Indicates wether or not a valid VLC installation was found on
	 * initialization
	 **/
	private static boolean vlcFound;

	/** Reference to this vlc instance **/
	private static VLC instance;

	/** Saves the state of the last valid volume level **/
	private int lastVolume = (MAX_VOLUME + MIN_VOLUME) / 2;

	/**
	 * Reference to the media player component that is responsible for rendering
	 * the movie
	 **/
	private DirectMediaPlayerComponent directMediaPlayerComponent;

	/** The mediaplayer that is responsible for playback **/
	private DirectMediaPlayer directMediaPlayer;

	/**
	 * True on Initialization of a new movie
	 */
	private boolean initMedia = true;

	/** Indicates wether or not the volume is muted **/
	private boolean isMuted = false;
	/** Reference to the currently loaded movie **/
	private Video currentVideo;
	/** Image that the rendering is done on **/
	private BufferedImage currentImage;
	/** References to the currently as canvas used panel **/
	private MoviePanel currentPanel;

	/**
	 * Contructor method for the vlc instance. Searches for a valid installation
	 * of VLC player
	 */
	private VLC() {
		vlcFound = new NativeDiscovery().discover();
		// If VLC cannot be found, we will inform the user of manual
		// possibilities

		if (!vlcFound) {
			// TODO VLC not found, open JDialog and give hint to manually add
			// the path
		}
	}

	/**
	 * Getter method for the vlc instance
	 * 
	 * @return a valid instance of vlc
	 */
	public static VLC getInstance() {
		if (instance == null) {
			VLC.instance = new VLC();
		}
		return instance;
	}

	/**
	 * Getter method for the currently loaded movie.
	 * 
	 * @return the current movie
	 */
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

	/**
	 * Returns wether or not VLC is installed on this machine
	 * 
	 * @return true if vlc was found, false if vlc was not found
	 */
	public boolean isVLCInstalled() {
		return vlcFound;
	}

	/**
	 * Updates the state of the movie's timeline
	 * 
	 * @param newInit
	 *            If true, the timeline will be revalidated, iIf false it will
	 *            not
	 */
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

	/**
	 * Stops current media playback and switches the loaded file with a new
	 * movie
	 * 
	 * @param videoInstance
	 *            the new video to be loaded
	 */
	public void switchMediaFile(Video videoInstance) {
		stopMedia();
		currentVideo = videoInstance;
		if(currentVideo == null){
			switchSurface(currentPanel, false);
		}else{
			loadMedia(videoInstance);
			playMedia();
		}
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
		}

	}

	/**
	 * Pauses media playback
	 */
	public void pauseMedia() {
		if (vlcFound && directMediaPlayer != null) {
			directMediaPlayer.pause();
		}

	}

	/**
	 * Stops media playback
	 */
	public void stopMedia() {
		if (vlcFound && directMediaPlayer != null) {
			directMediaPlayer.stop();
			currentPanel.setCurrentImage(null);
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

	/**
	 * Returns wether or not the volume is muted
	 * 
	 * @return true if the volume is muted, false if the volume is not muted
	 */
	public boolean isMuted() {
		return isMuted;
	}

	/**
	 * Toggles the volume's state between last valid value and 0
	 */
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

	/**
	 * Getter method for the last valid volume value
	 * 
	 * @return the last saved volume level
	 */
	public int getlastVolume() {
		return lastVolume;
	}

	/**
	 * Sets the volume to a new value considering min and max possible values
	 * and saves it as valid
	 * 
	 * @param newVolume
	 *            the new volume level
	 */
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

	/**
	 * Getter method for the current volume level
	 * 
	 * @return the current volume the movie is played at
	 */
	public int getVolume() {
		if (vlcFound && directMediaPlayer != null) {
			return directMediaPlayer.getVolume();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the tick spacing used on volume in- and de-crease
	 * 
	 * @return the size of the tick between to volume levels
	 */
	public int getVolumeSteps() {
		return VOLUME_STEPS;
	}

	/**
	 * Converts the current playback time and the total length of the movie file
	 * into a formatted String of type 00:00:00 / 00:00:00 000,0 with the last 4
	 * digits beeing a procentual representation of progress in playback
	 * 
	 * @return
	 */
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

	/** The width of the image that is used for video rendering* */
	private int imageWidth;
	/** The height of the image that is used for video rendering* */
	private int imageHeight;

	/**
	 * Switches the current canvas panel to a new version that is passed to this
	 * method and reinitializes the media player with new image resolution
	 * values used for rendering the movie
	 * 
	 * @param newSurface
	 *            The new panel that will be used as canvas
	 * @param safeTime
	 *            If true, the current playback time will be saved and jumped to
	 *            after reloading the canvas. If false, playback will start at
	 *            the beginning of the movie file
	 */
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
			currentImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_BGR);
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
			BufferFormat format = new BufferFormat("RGBA", imageWidth, imageHeight, new int[] { imageWidth * 4 },
			        new int[] { imageHeight });

			return format;
		}
	};

	private class MovieRenderCallbackAdapter extends RenderCallbackAdapter {

		private MovieRenderCallbackAdapter() {
			super(((DataBufferInt) currentImage.getRaster().getDataBuffer()).getData());
		}

		@Override
		protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(rgbBuffer.length * 4).order(ByteOrder.nativeOrder());
			IntBuffer intBuffer = byteBuffer.asIntBuffer();
			intBuffer.put(rgbBuffer);
			currentPanel.setCurrentImage(currentImage);
			currentPanel.repaint();
		}
	}

	/**
	 * Based on the saved Aspect ratio in this class the ideal image resolution
	 * is calculated. Always smaller than the canvas panel and always in
	 * expected ratio.
	 */
	public void calcAspectRatio() {
		imageWidth = currentPanel.getWidth();
		imageHeight = currentPanel.getHeight();
		if (imageWidth >= ((imageHeight * ASPECT_RATIO / 10) * 10)) {
			imageWidth = (int) Math.rint(imageHeight * ASPECT_RATIO / 10) * 10;

		} else {
			imageHeight = (int) Math.rint((imageWidth / ASPECT_RATIO) / 10) * 10;
		}
	}
}
