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
 * 
 * @author Fabian Volkert
 *
 */
public abstract class VLC {

	/** Rate of volume change when pressing a shortcut **/
	private static final int VOLUME_STEPS = 10;

	/** Maximum volume supported (200 is VLC max) **/
	private static final int MAX_VOLUME = 200;

	/** Minimum volume supported (0 is least) **/
	private static final int MIN_VOLUME = 0;

	/** The mediaplayer that is responsible for playback **/
	private static EmbeddedMediaPlayer mediaPlayerComponent;

	/** Canvas on which the mediaplayer plays media on **/
	private static Canvas canvas;

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
	public static void loadMovie(String media_path) {
		mediaPlayerComponent.prepareMedia(media_path);
	}

	/**
	 * Toggles movie playback
	 */
	public static void toggleMoviePlayback() {
		if (VLC.getMediaPlayer().isPlaying()) {
			VLC.pauseMovie();
		} else {
			VLC.playMovie();
		}
	}

	/**
	 * Starts media playback
	 */
	public static void playMovie() {
		mediaPlayerComponent.play();
	}

	/**
	 * Pauses media playback
	 */
	public static void pauseMovie() {
		mediaPlayerComponent.pause();
	}

	/**
	 * Stops media playback
	 */
	public static void stopMovie() {
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
	 * Increases volume by set rate
	 */
	public static void volumeUp() {
		if (mediaPlayerComponent.getVolume() <= MAX_VOLUME - VOLUME_STEPS) {
			mediaPlayerComponent.setVolume(mediaPlayerComponent.getVolume() + VOLUME_STEPS);
			System.out.println(mediaPlayerComponent.getVolume());
		}
	}

	/**
	 * Decreases volume by set rate
	 */
	public static void volumeDown() {
		if (mediaPlayerComponent.getVolume() >= MIN_VOLUME + VOLUME_STEPS) {
			mediaPlayerComponent.setVolume(mediaPlayerComponent.getVolume() - VOLUME_STEPS);
			System.out.println(mediaPlayerComponent.getVolume());
		}
	}

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
}
