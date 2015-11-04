package com.vip.input;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JTextField;

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

	private int NrOfShortcuts = 6;

	// Index-helpers, defines order in config file:
	private static final int TOGGLE_PLAYBACK = 0;
	private static final int NEXT_CHAPTER = 1;
	private static final int PREVIOUS_CHAPTER = 2;
	private static final int VOLUME_UP = 3;
	private static final int VOLUME_DOWN = 4;
	private static final int SEARCH = 5;

	private int[] shortcutList = new int[NrOfShortcuts];
	private boolean[] shortcutCTRLmask = new boolean[NrOfShortcuts];
	private boolean[] shortcutSHIFTmask = new boolean[NrOfShortcuts];

	/**
	 * TODO comment
	 */
	public KeyParser() {
		// Initialize arrays with default values
		for (int i = 0; i < NrOfShortcuts; i++) {
			shortcutList[i] = -1;
			shortcutCTRLmask[i] = false;
			shortcutSHIFTmask[i] = false;
		}
	}

	/**
	 * TODO comment
	 * 
	 * @param newVipFrame
	 */
	public KeyParser(VipFrame newVipFrame) {
		this();
		vipFrame = newVipFrame;
	}

	public void setVipFrame(VipFrame newVipFrame) {
		vipFrame = newVipFrame;
	}

	// (TODO What if a JDialog is opened? Add more conditions! -> frame focused)
	@Override
	public boolean dispatchKeyEvent(KeyEvent ke) {
		int keyState = ke.getID();
		int isPressed = KeyEvent.KEY_PRESSED;
		int currentKey = ke.getKeyCode();
		// When no textfield is focused and the key is pressed, input will be
		// processed
		if (keyState == isPressed && !(ke.getSource() instanceof JTextField) && vipFrame.isFocused()) {
			if (currentKey == shortcutList[TOGGLE_PLAYBACK]) {
				if (isValidInput(ke, TOGGLE_PLAYBACK))
					VLC.toggleMoviePlayback();
			}
			if (currentKey == shortcutList[NEXT_CHAPTER]) {
				if (isValidInput(ke, NEXT_CHAPTER))
					VLC.nextChapter();
			}
			if (currentKey == shortcutList[PREVIOUS_CHAPTER]) {
				if (isValidInput(ke, PREVIOUS_CHAPTER))
					VLC.previousChapter();
			}
			if (currentKey == shortcutList[VOLUME_UP]) {
				if (isValidInput(ke, VOLUME_UP))
					VLC.volumeUp();
			}
			if (currentKey == shortcutList[VOLUME_DOWN]) {
				if (isValidInput(ke, VOLUME_DOWN))
					VLC.volumeDown();
			}
			if (currentKey == shortcutList[SEARCH]) {
				if (isValidInput(ke, SEARCH))
					vipFrame.get_jtfSearch().requestFocus();
			}
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
	private boolean isValidInput(KeyEvent ke, int index) {
		// Not Valid if CTRL should be pressed but is not
		if (shortcutCTRLmask[index] && (ke.getModifiers() & KeyEvent.CTRL_MASK) == 0) {
			return false;
		} else if (shortcutCTRLmask[index] && (ke.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
		}
		// Not Valid if SHIFT should be pressed but is not
		if (shortcutSHIFTmask[index] && (ke.getModifiers() & KeyEvent.SHIFT_MASK) == 0) {
			return false;
		} else if (shortcutSHIFTmask[index] && (ke.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
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
