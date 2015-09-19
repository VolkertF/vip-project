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

	private static String loaded_movie_file = "F:\\Dji. Death Sails-HD.mp4";

	private static Canvas canvas;

	private static EmbeddedMediaPlayer mediaPlayerComponent;

	/**
	 * Getter method of the class' canvas.
	 * 
	 * @return the canvas that displays the Movie
	 */
	public static Canvas getCanvas() {
		return canvas;
	}

	/**
	 * Starts video playback
	 * 
	 */
	public static void playMovie() {
		mediaPlayerComponent.prepareMedia(loaded_movie_file);
		mediaPlayerComponent.play();

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
