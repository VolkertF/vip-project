package com.vip.window;

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

	// TODO What if a JDialog is opened? Add more conditions!
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

	/**
	 * Initializes shortcuts from the given configuration file.
	 * 
	 * @param configFile
	 *            the File that is to be searched for shortcuts.
	 */
	public void initShortcuts(File configFile) {
		// Contains one line of file data
		String line;
		// Index to save the shortcut to
		int index = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(configFile));
			// Search for the Entry point, might find end of file
			do {
				line = br.readLine();
			} while (line != null && !SHORTCUT_ENTRY_POINT.equals(line.trim()));
			// If entry point is missing we recreate the configuration with
			// default shortcuts
			if (line == null) {
				appendAndLoadDefaultShortcutConfig(configFile);
			} else {
				// Read every line in that matches pattern
				do {
					line = br.readLine();
					line = extractDataFromLine(line);
					//
					if (line != null && !line.isEmpty() && !line.startsWith("//")) {
						// If the config is broken, the application falls back
						// on the default configuration and breaks out of the
						// loop
						if ("Default".equals(line)) {
							appendAndLoadDefaultShortcutConfig(configFile);
							break;
						}
						// Tries to parse shortcuts into the array.
						try {
							parseShortcutToArray(index, line);
						} catch (ArrayIndexOutOfBoundsException oob) {
							// If it fails(for whatever reason) falls back on
							// default configuration
							appendAndLoadDefaultShortcutConfig(configFile);
						}
						index++;
					}
				} while (line != null && !SHORTCUT_EXIT_POINT.equals(line.trim()));
			}
			br.close();
		} catch (IOException ioe) {
			// FNF shouldn't be happening, general information initialization
			// will create default config file if missing on system
			// TODO Create FileNotReadableException handling
			System.out.println("Couldn't read configuration file. Does it exist? Terminating.");
			System.exit(-1);
		}

	}

	/**
	 * Extracts the relevant shortcut data to a string and returns it.
	 * 
	 * @param line
	 *            String that may contains Data that will be processed <br />
	 *            into a shortcut
	 * @return String with relevant Data. <br />
	 *         String that is Empty. <br />
	 *         String that is null <br />
	 */
	private String extractDataFromLine(String line) {
		// Null String remains null for loop break condition
		if (line == null) {
			return null;
		}
		// Lines starting with '//'(comments) will ignored
		if (!line.startsWith("//")) {
			// Line must match formatting
			if (line.contains("=")) {
				// Isolate relevant data
				line = (line.split("="))[1].trim();
			} else {
				// Otherwise config is broken
				line = "Default";
			}
		}
		return line;
	}

	/**
	 * Appends an existing configuration file with a default shortcut setup. It
	 * is called, when the entry point could not be found.
	 * 
	 * @param configFile
	 *            The configuration file that needs to be appended.
	 */
	private void appendAndLoadDefaultShortcutConfig(File configFile) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(configFile, true));
			PrintWriter pw = new PrintWriter(bw);
			String tmpDataContainer;
			pw.println();
			pw.println("[SHORTCUTS]");
			pw.println("//Do NOT change order of items\n//It would kill an innocent kitten");

			tmpDataContainer = "TOGGLE_PLAYBACK=SPACE";
			pw.println(tmpDataContainer);
			tmpDataContainer = extractDataFromLine(tmpDataContainer);
			parseShortcutToArray(TOGGLE_PLAYBACK, tmpDataContainer);

			tmpDataContainer = "NEXT_CHAPTER=N";
			pw.println(tmpDataContainer);
			tmpDataContainer = extractDataFromLine(tmpDataContainer);
			parseShortcutToArray(NEXT_CHAPTER, tmpDataContainer);

			tmpDataContainer = "PREVIOUS_CHAPTER=P";
			pw.println(tmpDataContainer);
			tmpDataContainer = extractDataFromLine(tmpDataContainer);
			parseShortcutToArray(PREVIOUS_CHAPTER, tmpDataContainer);

			tmpDataContainer = "VOLUME_UP=NUM_PLUS";
			pw.println(tmpDataContainer);
			tmpDataContainer = extractDataFromLine(tmpDataContainer);
			parseShortcutToArray(VOLUME_UP, tmpDataContainer);

			tmpDataContainer = "VOLUME_DOWN=NUM_MINUS";
			pw.println(tmpDataContainer);
			tmpDataContainer = extractDataFromLine(tmpDataContainer);
			parseShortcutToArray(VOLUME_DOWN, tmpDataContainer);

			tmpDataContainer = "SEARCH=CTRL F";
			pw.println(tmpDataContainer);
			tmpDataContainer = extractDataFromLine(tmpDataContainer);
			parseShortcutToArray(SEARCH, tmpDataContainer);

			pw.println("[/SHORTCUTS]");
			pw.close();
			bw.close();
		} catch (IOException ioe) {
			// TODO Create FileNotWritableException handling
			System.out.println("Couldn't write to configuration file. Terminating.");
			System.exit(-1);
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
	private void parseShortcutToArray(int index, String data) throws ArrayIndexOutOfBoundsException {
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
