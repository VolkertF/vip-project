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

	private static final int VOLUME_STEPS = 10;
	private static final int MAX_VOLUME = 200;
	private static final int MIN_VOLUME = 0;

	private static Canvas canvas;

	private static EmbeddedMediaPlayer mediaPlayerComponent;

	/**
	 * Getter method of the class' canvas.
	 * 
	 * @return the canvas that displays the Movie
	 */
	public static Canvas get_canvas() {
		return canvas;
	}
	
	public static int get_max_volume(){
		return MAX_VOLUME;
	}
	
	public static int get_min_volume(){
		return MIN_VOLUME;
	}

	public static EmbeddedMediaPlayer get_media_player() {
		return mediaPlayerComponent;
	}

	/**
	 * Starts video playback
	 * 
	 */

	public static void load_movie(String movie_path) {
		mediaPlayerComponent.prepareMedia(movie_path);
	}

	public static void toggle_movie_playback() {
		if (VLC.get_media_player().isPlaying()) {
			VLC.pause_movie();
		} else {
			VLC.play_movie();
		}
	}
	
	public static void play_movie() {
		mediaPlayerComponent.play();
	}

	public static void pause_movie() {
		mediaPlayerComponent.pause();
	}

	public static void stop_movie() {
		mediaPlayerComponent.stop();
	}

	public static void next_chapter() {
		mediaPlayerComponent.nextChapter();
	}

	public static void previous_chapter() {
		mediaPlayerComponent.previousChapter();
	}

	public static void volume_up() {
		if(mediaPlayerComponent.getVolume()<=MAX_VOLUME -VOLUME_STEPS){
				mediaPlayerComponent.setVolume(mediaPlayerComponent.getVolume() + VOLUME_STEPS);
				System.out.println(mediaPlayerComponent.getVolume());
		}
	}

	public static void volume_down() {
		if(mediaPlayerComponent.getVolume()>=MIN_VOLUME+VOLUME_STEPS){
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
