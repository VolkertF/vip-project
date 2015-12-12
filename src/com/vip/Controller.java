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

import com.vip.input.ButtonParser;
import com.vip.input.KeyParser;
import com.vip.media.VLC;
import com.vip.window.VipFrame;

/**
 * Controller class. Contains diverse methods for data processing and forms an
 * interface between GUI and persistence layer. Resonsible for configuration
 * loadout
 * 
 * @author Fabian
 *
 */
public class Controller {

	/** Responsible for parsing keyboard input into actions **/
	private KeyParser keyParser = new KeyParser();

	/** Responsible for parsing button input into actions **/
	private ButtonParser buttonParser;

	/** VLC media player. **/
	private VLC vlcInstance = new VLC();

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
		buttonParser = new ButtonParser(vlcInstance, vipFrame);
	}

	/**
	 * Responsible for initializse the configuration file. If it is missing the
	 * method will automatically create a default configuration. It will also do
	 * so, if the config file seems to be broken.
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

	/**
	 * Creates a new configuration file in the workspace folder and fills it
	 * with default information.
	 * 
	 * @param configFile
	 *            the file, that will be created
	 */
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

	/**
	 * Writes some default general information into the given file.
	 * 
	 * @param pw
	 *            PrintWriter to the file that information will be written to
	 */
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

	/**
	 * Writes some default shortcut information into the given file.
	 * 
	 * @param pw
	 *            PrintWriter to the file that information will be written to
	 */
	private void writeDefaultKeyboard(PrintWriter pw) {
		pw.println("//Contains information about shortcuts");
		pw.println(SHORTCUT_ENTRY_POINT);
		pw.println("//Do NOT change order of items\n//It would kill an innocent kitten");
		pw.println("TOGGLE_PLAYBACK=SPACE");
		pw.println("NEXT_MOVIE=N");
		pw.println("PREVIOUS_MOVIE=B");
		pw.println("NEXT_CHAPTER=N");
		pw.println("PREVIOUS_CHAPTER=P");
		pw.println("JUMP_FORWARD=CTRL J");
		pw.println("JUMP_BACKWARD=CTRL H");
		pw.println("MUTE_VOLUME=M");
		pw.println("VOLUME_UP=NUM_PLUS");
		pw.println("VOLUME_DOWN=NUM_MINUS");
		pw.println("OPEN_PREFERENCES=CTRL P");
		pw.println("FULLSCREEN_TOGGLE=F");
		pw.println("SEARCH=CTRL F");
		pw.println();

	}

	/**
	 * Reads from a reader to a file and tries to extract general information
	 * from it. if it fails the file will be deleted and newly created with
	 * default information.
	 * 
	 * @param br
	 *            Reader to the configuration file
	 * @param configFile
	 *            the file that might needs to be deleted
	 * @throws IOException
	 *             Is thrown if something with file input goes wrong
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
	 * Reads from a reader to a file and tries to extract general information
	 * from it. if it fails the file will be deleted and newly created with
	 * default information.
	 * 
	 * @param br
	 *            Reader to the configuration file
	 * @param configFile
	 *            the file that might needs to be deleted
	 * @throws IOException
	 *             Is thrown if something with file input goes wrong
	 */
	public void initKeyboard(BufferedReader br, File configFile) throws IOException {
		String information = null;
		do {
			information = readLine(br);
		} while (information != null && !SHORTCUT_ENTRY_POINT.equals(information));
		if (information != null) {
			for (int index = 0; index < keyParser.getNumberOfShortcuts(); index++) {
				information = extractInformation(readLine(br));
				if (information != null && !information.trim().isEmpty()) {
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

			// Disables default keyboard input and puts this manager in charge
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
	 * Reads lines from the given file, until it finds a relevant line. Relevant
	 * data is considered to be a line not being empty or starting with "//".
	 * 
	 * @param br
	 *            Reader to the file that is read from
	 * @return The String containing a relevant line of data from the file
	 * @throws IOException
	 *             Is thrown if input goes wrong
	 */
	private String readLine(BufferedReader br) throws IOException {
		String line = null;
		do {
			line = br.readLine();
		} while (line != null && (line.startsWith("//") || line.trim().isEmpty()));
		return line;
	}

	/**
	 * Tries to extract relevant data from a given string. Relevant data has to
	 * be on the right of an "=" sign.
	 * 
	 * @param line
	 *            The String the method has a look at
	 * @return The relevant data extracted or the unchanged string, if not found
	 */
	private String extractInformation(String line) {
		if (line != null && line.contains("=")) {
			line = line.trim().split("=")[1];
		}
		return line;
	}

	/**
	 * Getter method for the buttonParser object.
	 * 
	 * @return The Controller's buttonParser
	 */
	public ButtonParser getButtonParser() {
		return buttonParser;
	}

	public VLC getVLC() {
		return vlcInstance;
	}

	public void setVLC(VLC vlcInstance) {
		this.vlcInstance = vlcInstance;
	}

	public void toggleFullscreen() {
		if (getVLC().isFullscreen()) {
			vipFrame.disposeFullscreen();
		} else {
			vipFrame.createFullscreen();
		}
	}

	public KeyParser getKeyParser() {
		return keyParser;
	}

}
