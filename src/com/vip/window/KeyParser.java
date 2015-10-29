package com.vip.window;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextField;

import com.vip.media.VLC;

/**
 * The Key_parser translates user keyboard input into reasonable commands.
 * 
 * @author Fabian Volkert
 *
 */
public class KeyParser implements KeyEventDispatcher {

	private VipFrame vipFrame;

	private int NrOfShortcuts = 6;

	/**
	 * Phrase the reader will look for as an entry point in the config.ini file
	 * for shortcut acquiring
	 **/
	private static final String SHORTCUT_ENTRY_POINT = "[SHORTCUTS]";

	/** Exit point for the reader, marking the end of shortcut input **/
	private static final String SHORTCUT_EXIT_POINT = "[/SHORTCUTS]";

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

	public KeyParser() {
		// Initialize arrays with default values
		for (int i = 0; i < NrOfShortcuts; i++) {
			shortcutList[i] = -1;
			shortcutCTRLmask[i] = false;
			shortcutSHIFTmask[i] = false;
		}
	}

	public void setVipFrame(VipFrame newVipFrame) {
		vipFrame = newVipFrame;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent ke) {
		int keyState = ke.getID();
		int isPressed = KeyEvent.KEY_PRESSED;
		int currentKey = ke.getKeyCode();
		// When no textfield is focused and the key is pressed, input will be
		// processed
		if (keyState == isPressed && !(ke.getSource() instanceof JTextField)) {
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

	public void initShortcuts(File configFile) {
		// Contains one line of file data
		String line;
		// Index to save the shortcut to
		int index = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(configFile));
			do {
				line = br.readLine();
			} while (line != null && !SHORTCUT_ENTRY_POINT.equals(line.trim()));
			// If entry point is missing
			if (line == null) {
				// TODO fall back on default config
			} else {
				do {
					line = br.readLine();
					// Lines starting with '//' are ignored
					if (!line.startsWith("//")) {
						// Line must match formatting
						if (line.contains("=")) {
							// Isolate relevant data
							line = (line.split("="))[1].trim();
							parseShortcutToArray(index, line);
							index++;
						}
					}
				} while (line != null && !SHORTCUT_EXIT_POINT.equals(line.trim()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Parses a String to a keycode and saves it at its respective index in the
	 * shortcut array.
	 * 
	 * @param index
	 *            The index in the shortcut array the data will be parsed to
	 * @param data
	 *            The data that needs to be parsed to a keycode
	 */
	private void parseShortcutToArray(int index, String data) {
		int keyCode = -1;
		if (data.isEmpty()) {
			shortcutList[index] = keyCode;
			return;
		} else {
			if (data.contains("CTRL")) {
				shortcutCTRLmask[index] = true;
			}
			if (data.contains("SHIFT")) {
				shortcutSHIFTmask[index] = true;
			}
			String[] tokens;
			tokens = data.split("\\s+");
			data = tokens[tokens.length - 1];
			// Making sure every char is upper-case for following parse
			data = data.toUpperCase();
			switch (data) {
			case "SPACE":
				shortcutList[index] = KeyEvent.VK_SPACE;
				break;
			case "NUM_PLUS":
				shortcutList[index] = KeyEvent.VK_ADD;
				break;
			case "NUM_MINUS":
				shortcutList[index] = KeyEvent.VK_SUBTRACT;
				break;
			default:
				shortcutList[index] = KeyEvent.getExtendedKeyCodeForChar(data.charAt(0));
				break;
			}
		}
	}

}
