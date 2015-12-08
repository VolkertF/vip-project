package com.vip.media;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JPanel;

import com.vip.attributes.Video;
import com.vip.window.MoviePanel;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * VLC Class. Controls media playback.
 */
public class VLC {

	private final boolean vlcFound;

	/** Rate of volume change when pressing a shortcut **/
	private static final int VOLUME_STEPS = 10;

	/** Maximum volume supported (200 is VLC max) **/
	private static final int MAX_VOLUME = 200;

	/** Minimum volume supported (0 is least) **/
	private static final int MIN_VOLUME = 0;

	private boolean isMuted = false;

	private int lastVolume = (MAX_VOLUME + MIN_VOLUME) / 2;

	private String currentPathPlaying = null;

	public String getCurrentPlaybackPath() {
		return currentPathPlaying;
	}

	/**
	 * Percentage of how far a jump in the movie will proceed, must be between 0
	 * and 1
	 **/
	private static final double JUMP_PERCENTAGE = 0.05;

	/** The mediaplayer that is responsible for playback **/
	private DirectMediaPlayer directMediaPlayerComponent;

	/** Canvas on which the mediaplayer plays media on **/
	// private Canvas canvas;

	private MoviePanel jpnlVideoSurface = new MoviePanel(this);

	/**
	 * True on Initialization of a media
	 */
	private boolean initMedia = true;

	/**
	 * Initializes vlc plugin,finds vlc installation, sets canvas up.
	 */
	public VLC() {
		vlcFound = new NativeDiscovery().discover();
		// If VLC cannot be found, we will inform the user of manual
		// possibilities

//		 vlcFound = false;

		if (vlcFound) {
			invokeMediaPlayerCreation();
		} else {
			// TODO VLC not found, open JDialog and give hint to manually add
			// the path
		}
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

	public boolean isVLCInstalled() {
		return vlcFound;
	}

	public void setMediaInitState(boolean newInit) {
		initMedia = newInit;
	}

	/**
	 * Getter method of the class' canvas.
	 * 
	 * @return the canvas that displays the Movie
	 */
	public MoviePanel getVideoSurface() {
		return jpnlVideoSurface;
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
			return directMediaPlayerComponent;
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
	public void loadMedia(String mediaPath) {
		if (vlcFound && directMediaPlayerComponent != null) {
			directMediaPlayerComponent.prepareMedia(mediaPath);
			setMediaInitState(true);
			currentPathPlaying = mediaPath;
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
		if (vlcFound && directMediaPlayerComponent != null) {
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
		if (vlcFound && directMediaPlayerComponent != null)
			directMediaPlayerComponent.play();
	}

	/**
	 * Pauses media playback
	 */
	public void pauseMedia() {
		if (vlcFound && directMediaPlayerComponent != null)
			directMediaPlayerComponent.pause();
	}

	/**
	 * Stops media playback
	 */
	public void stopMedia() {
		if (vlcFound && directMediaPlayerComponent != null)
			directMediaPlayerComponent.stop();
	}

	/**
	 * Jumps to the next chapter
	 */
	public void nextChapter() {
		if (vlcFound && directMediaPlayerComponent != null)
			directMediaPlayerComponent.nextChapter();
	}

	/**
	 * Jumps to the previous Chapter
	 */
	public void previousChapter() {
		if (vlcFound && directMediaPlayerComponent != null)
			directMediaPlayerComponent.previousChapter();
	}

	/**
	 * Jumps forward in the media file a given percentage
	 */
	public void jumpForward() {
		if (vlcFound && directMediaPlayerComponent != null && directMediaPlayerComponent.getLength() != -1) {
			if (JUMP_PERCENTAGE >= 0 && JUMP_PERCENTAGE <= 1) {
				int changeRate = (int) (directMediaPlayerComponent.getLength() * JUMP_PERCENTAGE);
				int newTime = (int) (directMediaPlayerComponent.getTime() + changeRate);
				if (newTime >= directMediaPlayerComponent.getLength()) {
					stopMedia();
				} else {
					directMediaPlayerComponent.setTime(newTime);

				}
			}
		}
	}

	/**
	 * Jumps back in the media file a given percentage
	 */
	public void jumpBack() {
		if (vlcFound && directMediaPlayerComponent != null && directMediaPlayerComponent.getLength() != -1) {
			if (JUMP_PERCENTAGE >= 0 && JUMP_PERCENTAGE <= 1) {
				int changeRate = (int) (directMediaPlayerComponent.getLength() * JUMP_PERCENTAGE);
				int newTime = (int) (directMediaPlayerComponent.getTime() - changeRate);
				if (newTime < 0)
					newTime = 0;
				directMediaPlayerComponent.setTime(newTime);
			}
		}
	}

	public boolean isMuted() {
		return isMuted;
	}

	public void toggleMuted() {
		if (isMuted) {
			if (vlcFound && directMediaPlayerComponent != null) {
				isMuted = false;
				directMediaPlayerComponent.setVolume(lastVolume);
			}
		} else {
			if (vlcFound && directMediaPlayerComponent != null) {
				isMuted = true;
				directMediaPlayerComponent.setVolume(0);
			}
		}
	}

	public int getlastVolume() {
		return lastVolume;
	}

	public void setVolume(int newVolume) {
		if (vlcFound && directMediaPlayerComponent != null && directMediaPlayerComponent.getLength() != -1) {
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
			directMediaPlayerComponent.setVolume(newVolume);
		}
	}

	public int getVolume() {
		if (vlcFound && directMediaPlayerComponent != null) {
			return directMediaPlayerComponent.getVolume();
		} else {
			return 0;
		}
	}

	public int getVolumeSteps() {
		return VOLUME_STEPS;
	}

	public void invokeMediaPlayerCreation() {
		directMediaPlayerComponent = jpnlVideoSurface.createMediaPlayer().getMediaPlayer();
	}
}
