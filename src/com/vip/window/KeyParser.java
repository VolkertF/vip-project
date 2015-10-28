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
	private static final int EXIT_PROGRAM = 5;

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
		// When no textfield is focused, input will be processed
		if (!(ke.getSource() instanceof JTextField)) {
			if (keyState == isPressed && currentKey == shortcutList[TOGGLE_PLAYBACK]) {
				VLC.toggleMoviePlayback();
			}
			// if (state == KeyEvent.KEY_PRESSED && (id == KeyEvent.VK_PLUS ||
			// id == KeyEvent.VK_ADD)) {
			// vipFrame.updateVolume(VLC.getIncreasedVolume());
			// }
			// if (state == KeyEvent.KEY_PRESSED && (id == KeyEvent.VK_MINUS ||
			// id == KeyEvent.VK_SUBTRACT)) {
			// vipFrame.updateVolume(VLC.getDecreasedVolume());
			// }
			// // When CTRL+F is pressed, the search textfield will be focussed
			// if (state == pressed && id == KeyEvent.VK_F && (ke.getModifiers()
			// & KeyEvent.CTRL_MASK) != 0)
			// vipFrame.get_jtfSearch().requestFocus();
		}
		return false;
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
			case "A":
				shortcutList[index] = KeyEvent.VK_A;
				break;
			case "B":
				shortcutList[index] = KeyEvent.VK_B;
				break;
			case "C":
				shortcutList[index] = KeyEvent.VK_C;
				break;
			case "D":
				shortcutList[index] = KeyEvent.VK_D;
				break;
			case "E":
				shortcutList[index] = KeyEvent.VK_E;
				break;
			case "F":
				shortcutList[index] = KeyEvent.VK_F;
				break;
			case "G":
				shortcutList[index] = KeyEvent.VK_G;
				break;
			case "H":
				shortcutList[index] = KeyEvent.VK_H;
				break;
			case "I":
				shortcutList[index] = KeyEvent.VK_I;
				break;
			case "J":
				shortcutList[index] = KeyEvent.VK_J;
				break;
			case "K":
				shortcutList[index] = KeyEvent.VK_K;
				break;
			case "L":
				shortcutList[index] = KeyEvent.VK_L;
				break;
			case "M":
				shortcutList[index] = KeyEvent.VK_M;
				break;
			case "N":
				shortcutList[index] = KeyEvent.VK_N;
				break;
			case "O":
				shortcutList[index] = KeyEvent.VK_O;
				break;
			case "P":
				shortcutList[index] = KeyEvent.VK_P;
				break;
			case "Q":
				shortcutList[index] = KeyEvent.VK_Q;
				break;
			case "R":
				shortcutList[index] = KeyEvent.VK_R;
				break;
			case "S":
				shortcutList[index] = KeyEvent.VK_S;
				break;
			case "T":
				shortcutList[index] = KeyEvent.VK_T;
				break;
			case "U":
				shortcutList[index] = KeyEvent.VK_U;
				break;
			case "V":
				shortcutList[index] = KeyEvent.VK_V;
				break;
			case "W":
				shortcutList[index] = KeyEvent.VK_W;
				break;
			case "X":
				shortcutList[index] = KeyEvent.VK_X;
				break;
			case "Y":
				shortcutList[index] = KeyEvent.VK_Y;
				break;
			case "Z":
				shortcutList[index] = KeyEvent.VK_Z;
				break;
			case "SPACE":
				shortcutList[index] = KeyEvent.VK_SPACE;
				break;
			case "NUM PLUS":
				shortcutList[index] = KeyEvent.VK_ADD;
				break;
			case "NUM MINUS":
				shortcutList[index] = KeyEvent.VK_SUBTRACT;
				break;
			default:
				shortcutList[index] = -1;
				break;
			}
		}
	}

}
