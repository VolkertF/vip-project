package com.vip.media;

import java.awt.Canvas;
import java.awt.Color;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * VLC Class. Controls media playback.
 */
public class VLC {

	/** Rate of volume change when pressing a shortcut **/
	private static final int VOLUME_STEPS = 10;

	/** Maximum volume supported (200 is VLC max) **/
	private static final int MAX_VOLUME = 200;

	/** Minimum volume supported (0 is least) **/
	private static final int MIN_VOLUME = 0;

	private boolean isMuted = false;

	private int lastVolume = (MAX_VOLUME + MIN_VOLUME) / 2;

	/**
	 * Percentage of how far a jump in the movie will proceed, must be between 0
	 * and 1
	 **/
	private static final double JUMP_PERCENTAGE = 0.05;

	/** The mediaplayer that is responsible for playback **/
	private EmbeddedMediaPlayer mediaPlayerComponent;

	private MediaPlayerFactory mediaPlayerFactory;

	/** Canvas on which the mediaplayer plays media on **/
	private Canvas canvas;

	/**
	 * True on Initialization of a media
	 */
	private boolean initMedia = true;

	/**
	 * Initializes vlc plugin,finds vlc installation, sets canvas up.
	 */
	public VLC() {
		boolean found = new NativeDiscovery().discover();
		// If VLC cannot be found, we will inform the user of manual
		// possibilities
		if (!found) {
			// TODO VLC not found, open JDialog and give hint to manually add
			// the path
		}

		canvas = new Canvas();
		canvas.setBackground(Color.BLACK);
		mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayerComponent = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayerComponent.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
	}

	/**
	 * Returns wether or not the GUI needs to rebuild for a new media to play
	 * 
	 * @return <code>true</code> if the media should be initialized<br />
	 *         <code>false</code> if the media is not to be inizialized
	 */
	public boolean shouldInitMedia() {
		return initMedia;
	}

	public void setMediaInitState(boolean newInit) {
		initMedia = newInit;
	}

	/**
	 * Getter method of the class' canvas.
	 * 
	 * @return the canvas that displays the Movie
	 */
	public Canvas getCanvas() {
		return canvas;
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
	public EmbeddedMediaPlayer getMediaPlayer() {
		return mediaPlayerComponent;
	}

	public MediaPlayerFactory getMediaPlayerFactory() {
		return mediaPlayerFactory;
	}

	/**
	 * Loads a media file into the mediaplayer
	 * 
	 * @param media_path
	 *            Path to the media file to be loaded
	 */
	public void loadMedia(String Media_path) {
		if (mediaPlayerComponent != null) {
			mediaPlayerComponent.prepareMedia(Media_path);
			setMediaInitState(true);
		}
	}

	public void switchMediaFile(String newFile) {
		stopMedia();
		loadMedia(newFile);
		playMedia();
	}

	/**
	 * Toggles media playback
	 */
	public void toggleMediaPlayback() {
		if (mediaPlayerComponent != null) {
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
		mediaPlayerComponent.play();
	}

	/**
	 * Pauses media playback
	 */
	public void pauseMedia() {
		mediaPlayerComponent.pause();
	}

	/**
	 * Stops media playback
	 */
	public void stopMedia() {
		mediaPlayerComponent.stop();
	}

	/**
	 * Jumps to the next chapter
	 */
	public void nextChapter() {
		mediaPlayerComponent.nextChapter();
	}

	/**
	 * Jumps to the previous Chapter
	 */
	public void previousChapter() {
		mediaPlayerComponent.previousChapter();
	}

	/**
	 * Jumps forward in the media file a given percentage
	 */
	public void jumpForward() {
		if (mediaPlayerComponent != null && mediaPlayerComponent.getLength() != -1) {
			if (JUMP_PERCENTAGE >= 0 && JUMP_PERCENTAGE <= 1) {
				int changeRate = (int) (mediaPlayerComponent.getLength() * JUMP_PERCENTAGE);
				int newTime = (int) (mediaPlayerComponent.getTime() + changeRate);
				if (newTime >= mediaPlayerComponent.getLength()) {
					stopMedia();
				} else {
					mediaPlayerComponent.setTime(newTime);

				}
			}
		}
	}

	/**
	 * Jumps back in the media file a given percentage
	 */
	public void jumpBack() {
		if (mediaPlayerComponent != null && mediaPlayerComponent.getLength() != -1) {
			if (JUMP_PERCENTAGE >= 0 && JUMP_PERCENTAGE <= 1) {
				int changeRate = (int) (mediaPlayerComponent.getLength() * JUMP_PERCENTAGE);
				int newTime = (int) (mediaPlayerComponent.getTime() - changeRate);
				if (newTime < 0)
					newTime = 0;
				mediaPlayerComponent.setTime(newTime);
			}
		}
	}

	public boolean isMuted() {
		return isMuted;
	}

	public void toggleMuted() {
		if (isMuted) {
			isMuted = false;
		} else {
			isMuted = true;
		}
	}

	public int getlastVolume() {
		return lastVolume;
	}

	public void setVolume(int newVolume) {
		if (mediaPlayerComponent != null && mediaPlayerComponent.getLength() != -1) {
			if (newVolume > MAX_VOLUME) {
				newVolume = MAX_VOLUME;
			} else if (newVolume < MIN_VOLUME) {
				newVolume = MIN_VOLUME;
			}
			if (newVolume != 0) {
				isMuted = false;
			} else {
				isMuted = true;
			}
			mediaPlayerComponent.setVolume(newVolume);
			lastVolume = newVolume;
		}
	}

	public int getVolume() {
		return mediaPlayerComponent.getVolume();
	}

	public int getVolumeSteps() {
		return VOLUME_STEPS;
	}
}
