package com.vip.media;

import java.awt.Canvas;
import java.awt.Color;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * VLC Class. Holds a canvas to display on the movie panel and controls video
 * playback.
 */
public abstract class VLC {

	/** Rate of volume change when pressing a shortcut **/
	private static final int VOLUME_STEPS = 10;

	/** Maximum volume supported (200 is VLC max) **/
	private static final int MAX_VOLUME = 200;

	/** Minimum volume supported (0 is least) **/
	private static final int MIN_VOLUME = 0;

	/**
	 * Percentage of how far a jump in the movie will proceed, must be between 0
	 * and 1
	 **/
	private static final double JUMP_PERCENTAGE = 0.05;

	/** The mediaplayer that is responsible for playback **/
	private static EmbeddedMediaPlayer mediaPlayerComponent;

	/** Canvas on which the mediaplayer plays media on **/
	private static Canvas canvas;

	/**
	 * Initializes vlc plugin,finds vlc installation, sets canvas up.
	 */
	public static void init() {
		boolean found = new NativeDiscovery().discover();
		System.out.println(found);
		System.out.println(LibVlc.INSTANCE.libvlc_get_version());

		canvas = new Canvas();
		canvas.setBackground(Color.BLACK);

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayerComponent = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayerComponent.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
	}

	/**
	 * Getter method of the class' canvas.
	 * 
	 * @return the canvas that displays the Movie
	 */
	public static Canvas getCanvas() {
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
	public static EmbeddedMediaPlayer getMediaPlayer() {
		return mediaPlayerComponent;
	}

	/**
	 * Loads a media file into the mediaplayer
	 * 
	 * @param media_path
	 *            Path to the media file to be loaded
	 */
	public static void loadMedia(String media_path) {
		if (mediaPlayerComponent != null)
			mediaPlayerComponent.prepareMedia(media_path);
	}

	public static void switchMediaFile(String newFile) {
		stopMedia();
		loadMedia(newFile);
		playMedia();
	}

	/**
	 * Toggles movie playback
	 */
	public static void toggleMoviePlayback() {
		if (mediaPlayerComponent != null) {
			if (VLC.getMediaPlayer().isPlaying()) {
				VLC.pauseMedia();
			} else {
				VLC.playMedia();
			}
		}
	}

	/**
	 * Starts media playback
	 */
	public static void playMedia() {
		mediaPlayerComponent.play();
	}

	/**
	 * Pauses media playback
	 */
	public static void pauseMedia() {
		mediaPlayerComponent.pause();
	}

	/**
	 * Stops media playback
	 */
	public static void stopMedia() {
		mediaPlayerComponent.stop();
	}

	/**
	 * Jumps to the next chapter
	 */
	public static void nextChapter() {
		mediaPlayerComponent.nextChapter();
	}

	/**
	 * Jumps to the previous Chapter
	 */
	public static void previousChapter() {
		mediaPlayerComponent.previousChapter();
	}

	/**
	 * Jumps forward in the media file a given percentage
	 */
	public static void jumpForward() {
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
	public static void jumpBack() {
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

	/**
	 * Increases volume by set rate
	 */
	public static void volumeUp() {
		if (mediaPlayerComponent != null && mediaPlayerComponent.getLength() != -1) {
			if (mediaPlayerComponent.getVolume() <= MAX_VOLUME - VOLUME_STEPS) {
				mediaPlayerComponent.setVolume(mediaPlayerComponent.getVolume() + VOLUME_STEPS);
				System.out.println(mediaPlayerComponent.getVolume());
			} else {
				mediaPlayerComponent.setVolume(MAX_VOLUME);
			}
		}

	}

	/**
	 * Decreases volume by set rate
	 */
	public static void volumeDown() {
		if (mediaPlayerComponent != null && mediaPlayerComponent.getLength() != -1) {
			if (mediaPlayerComponent.getVolume() >= MIN_VOLUME + VOLUME_STEPS) {
				mediaPlayerComponent.setVolume(mediaPlayerComponent.getVolume() - VOLUME_STEPS);
				System.out.println(mediaPlayerComponent.getVolume());
			} else {
				mediaPlayerComponent.setVolume(MIN_VOLUME);
			}
		}

	}

	/**
	 * Increases volume by set rate and returns it new value
	 * 
	 * @return the new volume level
	 */
	public static int getIncreasedVolume() {
		int newVolume = (MAX_VOLUME + MIN_VOLUME) / 2;
		if (mediaPlayerComponent != null && mediaPlayerComponent.getLength() != -1) {
			if (mediaPlayerComponent.getVolume() <= MAX_VOLUME - VOLUME_STEPS) {
				newVolume = mediaPlayerComponent.getVolume() + VOLUME_STEPS;
			} else {
				newVolume = MAX_VOLUME;
			}
		}
		return newVolume;
	}

	/**
	 * Decreases volume by set rate and returns it new value
	 * 
	 * @return the new volume level
	 */
	public static int getDecreasedVolume() {
		int newVolume = (MAX_VOLUME + MIN_VOLUME) / 2;
		if (mediaPlayerComponent != null && mediaPlayerComponent.getLength() != -1) {
			if (mediaPlayerComponent.getVolume() >= MIN_VOLUME + VOLUME_STEPS) {
				newVolume = mediaPlayerComponent.getVolume() - VOLUME_STEPS;
			} else {
				newVolume = MIN_VOLUME;
			}
		}
		return newVolume;
	}
}