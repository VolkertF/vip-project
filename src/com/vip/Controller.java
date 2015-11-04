package com.vip;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.vip.input.KeyParser;
import com.vip.media.VLC;
import com.vip.window.ButtonParser;
import com.vip.window.VipFrame;

/**
 * TODO comment
 * 
 * @author Fabian
 *
 */
public class Controller {

	/** Responsible for parsing keyboard input into actions **/
	private KeyParser keyParser = new KeyParser();

	/** Responsible for parsing button input into actions **/
	private ButtonParser buttonParser = new ButtonParser();

	/** **/
	private VipFrame vipFrame;

	/**
	 * Phrase the reader will look for as an entry point in the config.ini file
	 * for general information acquiring
	 **/
	private static final String GENERAL_ENTRY_POINT = "[GENERAL]";

	/**
	 * Phrase the reader will look for as an entry point in the config.ini file
	 * for shortcut acquiring
	 **/
	private static final String SHORTCUT_ENTRY_POINT = "[SHORTCUTS]";

	public Controller(VipFrame newVipFrame) {
		vipFrame = newVipFrame;
	}

	/**
	 * 
	 */
	public void initConfiguration() {
		// locate expected path of config.ini
		String configPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		configPath = configPath.concat("config.ini");
		File configFile = new File(configPath);
		if (configFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(configFile));
				initGeneralInformation(br, configFile);
				initKeyboard(br, configFile);

				br.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error on reading in the configuration file. Terminating");
				System.exit(-1);
			}
		} else {
			createDefaultConfigurationFile(configFile);
			initConfiguration();
		}

	}

	private void createDefaultConfigurationFile(File configFile) {
		BufferedWriter bw = null;
		try {
			configFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(configFile));
			PrintWriter pw = new PrintWriter(bw);

			writeDefaultGeneralInformation(pw);
			writeDefaultKeyboard(pw);

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error on creating default configuration file. Terminating.");
			System.exit(-1);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(
					        "Could not close BufferedWriter while creating default configuration. Terminating.");
					System.exit(-1);
				}
			}

		}
	}

	private void writeDefaultGeneralInformation(PrintWriter pw) {
		pw.println("//Contains general application information");
		pw.println(GENERAL_ENTRY_POINT);
		pw.println("//If true last movie watched will automatically start playing");
		pw.println("AUTO_PLAYBACK_ON_START=true");
		pw.println("//ID of the movie last watched, will be loaded on application start");
		pw.println("RESUME_TIMESTAMP=00:00:00");
		pw.println("//ID of the movie last watched, will be loaded on application start");
		pw.println("LAST_MOVIE_WATCHED_ID=1");
		pw.println("//VOLUME_DEFAULT between 0 and 200");
		pw.println("VOLUME_DEFAULT=100");
		pw.println();
	}

	private void writeDefaultKeyboard(PrintWriter pw) {
		pw.println("//Contains information about shortcuts");
		pw.println(SHORTCUT_ENTRY_POINT);
		pw.println("//Do NOT change order of items\n//It would kill an innocent kitten");
		pw.println("TOGGLE_PLAYBACK=SPACE");
		pw.println("NEXT_CHAPTER=N");
		pw.println("PREVIOUS_CHAPTER=P");
		pw.println("VOLUME_UP=NUM_PLUS");
		pw.println("VOLUME_DOWN=NUM_MINUS");
		pw.println("SEARCH=CTRL F");
		pw.println();

	}

	/**
	 * 
	 * @param br
	 * @throws IOException
	 */
	private void initGeneralInformation(BufferedReader br, File configFile) throws IOException {
		String information = null;
		// First we will need to find the entry point for our general
		// information
		do {
			information = readLine(br);
		} while (information != null && !GENERAL_ENTRY_POINT.equals(information));
		if (information != null) {
			// Here we can read information in
		} else {
			System.out.println("General-section not found. Restoring defaults!");
			br.close();
			configFile.delete();
			initConfiguration();
		}

	}

	/**
	 * Initializes shortcuts from the given configuration file.
	 * 
	 * @param configFile
	 *            the File that is to be searched for shortcuts.
	 * @throws IOException
	 */
	public void initKeyboard(BufferedReader br, File configFile) throws IOException {
		String information = null;
		do {
			information = readLine(br);
		} while (information != null && !SHORTCUT_ENTRY_POINT.equals(information));
		if (information != null) {
			System.out.println(information);
			for (int index = 0; index < keyParser.getNumberOfShortcuts(); index++) {
				information = extractInformation(readLine(br));
				System.out.println("At " + index + ": " + information);
				if (information != null && !information.trim().isEmpty()) {
					// TODO parse shortcut information into keycodes.
					parseStringToKeyCodeArray(index, information);
				} else {
					if (index < keyParser.getNumberOfShortcuts()) {
						System.out.println(
						        "Unexpected end of file or useless information. Corrupted configuration file. Restoring defaults!");
						br.close();
						configFile.delete();
						initConfiguration();
						break;
					}
				}

			}

			keyParser.setVipFrame(vipFrame);
			KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			manager.addKeyEventDispatcher(keyParser);

		} else {
			System.out.println("Shortcut-section not found");
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
	private void parseStringToKeyCodeArray(int index, String information) {
		if (information.contains("CTRL")) {
			keyParser.getCTRLMaskArray()[index] = true;
		}
		if (information.contains("SHIFT")) {
			keyParser.getSHIFTMaskArray()[index] = true;
		}
		String[] tokens;
		tokens = information.split("\\s+");
		information = tokens[tokens.length - 1];
		// Making sure every char is upper-case for following parse
		information = information.toUpperCase();
		switch (information) {
		case "SPACE":
			keyParser.getShortcutArray()[index] = KeyEvent.VK_SPACE;
			break;
		case "NUM_PLUS":
			keyParser.getShortcutArray()[index] = KeyEvent.VK_ADD;
			break;
		case "NUM_MINUS":
			keyParser.getShortcutArray()[index] = KeyEvent.VK_SUBTRACT;
			break;
		default:
			keyParser.getShortcutArray()[index] = KeyEvent.getExtendedKeyCodeForChar(information.charAt(0));
			break;
		}

	}

	/**
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private String readLine(BufferedReader br) throws IOException {
		String line = null;
		do {
			line = br.readLine();
		} while (line != null && (line.startsWith("//") || line.trim().isEmpty()));
		return line;
	}

	private String extractInformation(String line) {
		if (line != null && line.contains("=")) {
			line = line.trim().split("=")[1];
		}
		return line;
	}

	/**
	 * 
	 * @return
	 */
	public ButtonParser getButtonParser() {
		return buttonParser;
	}

}
