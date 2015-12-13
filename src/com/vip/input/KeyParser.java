package com.vip.input;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import com.vip.attributes.Video;
import com.vip.media.VLC;
import com.vip.window.VipFrame;

/**
 * The Key_parser translates user keyboard input into reasonable commands.
 * 
 * @author Fabian Volkert
 *
 */
public class KeyParser implements KeyEventDispatcher {

	private VipFrame vipFrame;

	private int NrOfShortcuts = 13;

	// Index-helpers, defines order in config file:
	public static final int TOGGLE_PLAYBACK = 0;
	public static final int NEXT_MOVIE = 1;
	public static final int PREVIOUS_MOVIE = 2;
	public static final int NEXT_CHAPTER = 3;
	public static final int PREVIOUS_CHAPTER = 4;
	public static final int JUMP_FORWARD = 5;
	public static final int JUMP_BACKWARD = 6;
	public static final int MUTE_VOLUME = 7;
	public static final int VOLUME_UP = 8;
	public static final int VOLUME_DOWN = 9;
	public static final int OPEN_PREFERENCES = 10;
	public static final int FULLSCREEN_TOGGLE = 11;
	public static final int SEARCH = 12;

	private int[] shortcutList = new int[NrOfShortcuts];
	private boolean[] shortcutCTRLmask = new boolean[NrOfShortcuts];
	private boolean[] shortcutSHIFTmask = new boolean[NrOfShortcuts];

	public int[] getShortcutList() {
		return shortcutList;
	}

	public KeyParser() {
		// Initialize arrays with default values
		for (int i = 0; i < NrOfShortcuts; i++) {
			shortcutList[i] = -1;
			shortcutCTRLmask[i] = false;
			shortcutSHIFTmask[i] = false;
		}
	}

	public KeyParser(VipFrame newVipFrame) {
		this();
		vipFrame = newVipFrame;
	}

	public void setVipFrame(VipFrame newVipFrame) {
		vipFrame = newVipFrame;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent ke) {
		int keyState = ke.getID();
		int isReleased = KeyEvent.KEY_RELEASED;
		int currentKey = ke.getKeyCode();
		// When no textfield is focused and the key is pressed, input will be
		// processed
		if (keyState == isReleased && !(ke.getSource() instanceof JTextField)) {
			VLC vlc = vipFrame.getController().getVLC();
			if (vlc.isFullscreen()) {
				vlc.getVideoSurface().setDrawOverlay(true);
				vlc.getVideoSurface().setDisplayStates(true, true, false, false);
			}
			if (currentKey == shortcutList[TOGGLE_PLAYBACK]) {
				if (isValidInput(ke, TOGGLE_PLAYBACK))
					vlc.toggleMediaPlayback();
			}
			if (currentKey == shortcutList[NEXT_CHAPTER]) {
				if (isValidInput(ke, NEXT_CHAPTER))
					vlc.nextChapter();
			}
			if (currentKey == shortcutList[PREVIOUS_CHAPTER]) {
				if (isValidInput(ke, PREVIOUS_CHAPTER))
					vlc.previousChapter();
			}
			if (currentKey == shortcutList[VOLUME_UP]) {
				if (isValidInput(ke, VOLUME_UP)) {
					vlc.getVideoSurface().setDisplayStates(true, false, false, false);
					vlc.setVolume(vlc.getVolume() + vlc.getVolumeSteps());
				}
			}
			if (currentKey == shortcutList[VOLUME_DOWN]) {
				if (isValidInput(ke, VOLUME_DOWN)) {
					vlc.getVideoSurface().setDisplayStates(true, false, false, false);
					vlc.setVolume(vlc.getVolume() - vlc.getVolumeSteps());
				}
			}
			if (currentKey == shortcutList[SEARCH]) {
				if (isValidInput(ke, SEARCH))
					vipFrame.get_jtfSearch().requestFocus();
			}
			if (currentKey == shortcutList[NEXT_MOVIE]) {
				if (isValidInput(ke, NEXT_MOVIE)) {
					int oldIndex = vipFrame.getFileList().getSelectedIndex();
					int newIndex = oldIndex + 1;
					// If reached end of list
					if (newIndex >= (vipFrame.getFileList().getModel().getSize())) {
						newIndex = 0;
					}
					vipFrame.getFileList().setSelectedIndex(newIndex);
					Video videoInstance = com.vip.controllers.SearchSortController.getInstance()
					        .getVideoByIndex(newIndex);
					vipFrame.updateIntel(videoInstance);
					vlc.switchMediaFile(videoInstance.getFilePath());
				}
			}
			if (currentKey == shortcutList[PREVIOUS_MOVIE]) {
				if (isValidInput(ke, PREVIOUS_MOVIE)) {
					int oldIndex = vipFrame.getFileList().getSelectedIndex();
					int newIndex = oldIndex - 1;
					// If reached pre-beginning of list
					if (newIndex < 0) {
						newIndex = (vipFrame.getFileList().getModel().getSize() - 1);
						vipFrame.getFileList().setSelectedIndex(newIndex);
					}
					vipFrame.getFileList().setSelectedIndex(newIndex);
					Video videoInstance = com.vip.controllers.SearchSortController.getInstance()
					        .getVideoByIndex(newIndex);
					vipFrame.updateIntel(videoInstance);
					vlc.switchMediaFile(videoInstance.getFilePath());
				}
			}
			if (currentKey == shortcutList[JUMP_FORWARD]) {
				if (isValidInput(ke, JUMP_FORWARD)) {
					vlc.jumpForward();
				}
			}
			if (currentKey == shortcutList[JUMP_BACKWARD]) {
				if (isValidInput(ke, JUMP_BACKWARD)) {
					vlc.jumpBack();
				}
			}
			if (currentKey == shortcutList[MUTE_VOLUME]) {
				if (isValidInput(ke, MUTE_VOLUME)) {
					vlc.toggleMuted();
				}
			}
			if (currentKey == shortcutList[FULLSCREEN_TOGGLE]) {
				if (isValidInput(ke, FULLSCREEN_TOGGLE)) {
					vipFrame.getController().toggleFullscreen();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks a given keyEvent for a shortcut in the array against its CTRL and
	 * its SHIFT mask.
	 * 
	 * @param ke
	 *            current keyEvent that was fired
	 * @param index
	 *            index in the shortcut array to look for
	 * @return <code>true</code> if the input is a valid shortcut <br />
	 *         <code>false</code> if the input didn't match the requierements
	 */
	public boolean isValidInput(KeyEvent ke, int index) {
		int modifiers = ke.getModifiers();
		// Not Valid if CTRL should be pressed but is not
		if (shortcutCTRLmask[index] && ((modifiers & KeyEvent.CTRL_MASK) != KeyEvent.CTRL_MASK)) {
			return false;
		} else if (!shortcutCTRLmask[index] && ((modifiers & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK)) {
			return false;
		}
		if (shortcutSHIFTmask[index] && ((modifiers & KeyEvent.SHIFT_MASK) != KeyEvent.SHIFT_MASK)) {
			return false;
		} else if (!shortcutSHIFTmask[index] && ((modifiers & KeyEvent.SHIFT_MASK) == KeyEvent.SHIFT_MASK)) {

		}
		return true;
	}

	public int[] getShortcutArray() {
		return shortcutList;
	}

	public boolean[] getCTRLMaskArray() {
		return shortcutCTRLmask;
	}

	public boolean[] getSHIFTMaskArray() {
		return shortcutSHIFTmask;
	}

	public int getNumberOfShortcuts() {
		return NrOfShortcuts;
	}

}
