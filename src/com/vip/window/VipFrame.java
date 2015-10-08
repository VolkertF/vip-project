package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.glass.events.KeyEvent;
import com.vip.media.VLC;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

@SuppressWarnings("serial")
public class VipFrame extends JFrame {

	private ButtonParser button_parser = new ButtonParser();

	/**
	 * Constructor for building the frame and initialize all event handlers.
	 */
	public VipFrame() {
		super("VipFrame");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		defaultInsets = new Insets(2, 2, 2, 2);

		// Tries to change the look and feel of Java to Nimbus, a
		// cross-platform GUI that comes with Java 6 update 10
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not found, it will be the default look and feel
		}

		KeyParser keyParser = new KeyParser();
		keyParser.set_vip_frame(this);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(keyParser);

		VLC.init();
		buildPanels();
		buildExplorerGUI();
		buildMovieGUI();
		buildIntelGUI();
		buildMenuBar();
		pack();
		requestFocus();
	}

	/**
	 * ArrayList<String> to display all movies in a datastructure
	 */
	private ArrayList<String> movies;

	/**
	 * The JPanel that represents the Explorer and do file-searching stuff and
	 * listing all the videos in a JList, so the user is able to select one and
	 * play it
	 */
	private JPanel jpnlExplorer;

	/**
	 * The JPanel that represents the movie been played by the VLC plugin
	 */
	private JPanel jpnlMovie;

	/**
	 * The JPanel that represents the information section of the program, where
	 * intel about the currently selected movie can be clicked on so the program
	 * do a search for this one, and various information about the title can be
	 * viewed.
	 */
	private JPanel jpnlIntel;

	/**
	 * Standard insets for creating the GridBagLayout
	 */
	private Insets defaultInsets;

	/**
	 * Main JPanel, that holds all the other panels
	 */
	JPanel jpnlMain;

	/**
	 * Helping routine for creating components and adding them to a
	 * GridBagLayout The parameters are constraints when they are added
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 * @param width
	 *            Width of the cell
	 * @param height
	 *            Height of the cell
	 * @param weightx
	 *            Weight vertically
	 * @param weighty
	 *            Weight horizontally
	 * @param cont
	 *            Container
	 * @param comp
	 *            Component that will be added
	 * @param insets
	 *            Distances round the component that is added
	 */
	private static void addComponent(int x, int y, int width, int height, double weightx, double weighty,
	        Container cont, Component comp, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		cont.add(comp, gbc);
	}

	/**
	 * Helping routine for creating ActionListeners and add them to menuItems
	 * based on opening the standard browser and open a URL. Uses URI and a
	 * menuItem.
	 * 
	 * @param menuItem
	 *            The Item that the URL should be added to
	 * @param url
	 *            The URL that should open when you click the MenuItem
	 * @throws URISyntaxException
	 */
	private static void addURLActionListenerToMenuBarItem(JMenuItem menuItem, final URI url) throws URISyntaxException {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		menuItem.addActionListener(al);
	}

	/**
	 * Create Subpanels Have to be called before any other building-methods
	 */
	private void buildPanels() {
		jpnlMain = new JPanel();
		jpnlMain.setLayout(new GridBagLayout());
		add(jpnlMain);

		jpnlExplorer = new JPanel();
		jpnlExplorer.setLayout(new FlowLayout());
		jpnlExplorer.setBorder(BorderFactory.createTitledBorder("Explorer"));
		jpnlExplorer.setPreferredSize(new Dimension(250, 800));

		jpnlMovie = new JPanel();
		jpnlMovie.setLayout(new BorderLayout());
		jpnlMovie.setBorder(BorderFactory.createTitledBorder("Movie"));

		jpnlIntel = new JPanel();
		jpnlIntel.setLayout(new BorderLayout());
		jpnlIntel.setBorder(BorderFactory.createTitledBorder("Intel"));
		jpnlIntel.setPreferredSize(new Dimension(1200, 150));

		addComponent(0, 0, 1, 2, 0.0, 0.0, jpnlMain, jpnlExplorer, defaultInsets);
		addComponent(1, 0, 1, 1, 1.0, 1.0, jpnlMain, jpnlMovie, defaultInsets);
		addComponent(1, 1, 1, 1, 0.1, 0.1, jpnlMain, jpnlIntel, defaultInsets);
	}

	/**
	 * This JList will fill with the files in all searching directories the
	 * program overwatches, so basically every movie file found on the harddrive
	 */
	private JList<String> jlstFileList;

	/**
	 * This JTextField is for entering your private search stuff into a
	 * textField to search your library for this
	 */
	private JTextField jtfSearch;

	public JTextField get_jtfSearch() {
		return jtfSearch;
	}

	/**
	 * This JComboBox is ment for sorting your search by different categories
	 */
	private JComboBox<String> jcbSearchCategories;

	/**
	 * By pressing this JButton the search by the chosen searching-categories
	 * and the entered keywords is executed.
	 */
	private JButton jbtnSearchExecute; // Button for executing the search

	/**
	 * Create Sub-sub-panels in the explorer panel
	 */
	private void buildExplorerGUI() {
		// String[] fileList = {};
		// Fill the filelist String-Array
		movies = new ArrayList<String>();
		DefaultListModel<String> defaultJList = new DefaultListModel<String>();
		jlstFileList = new JList<String>(defaultJList);
		movies.add("Star Wars: The Revenge of the Sith");
		movies.add("Fargo");
		movies.add("Matrix");
		for (String temp : movies) {
			defaultJList.addElement(temp);
		}
		jlstFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlstFileList.setSelectedIndex(0);

		jtfSearch = new JTextField(20);

		String[] searchCategories = { "By Length, increasing", // Index 0
		        "By length, decreasing", // Index 1
		        "By rating, increasing", // Index 2
		        "By rating, decreasing", }; // Index 3
		jcbSearchCategories = new JComboBox<String>(searchCategories);
		jcbSearchCategories.setEditable(false);
		jcbSearchCategories.setSelectedIndex(0);
		JScrollPane jspSearchCategories = new JScrollPane(jcbSearchCategories);
		jspSearchCategories.setPreferredSize(jcbSearchCategories.getSize());

		jbtnSearchExecute = new JButton("Search");
		jbtnSearchExecute.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");

		// x y w h wx wy cont comp insets
		addComponent(0, 0, 2, 1, 0.0, 0.0, jpnlExplorer, jtfSearch, defaultInsets);
		addComponent(0, 1, 1, 1, 0.0, 0.0, jpnlExplorer, jcbSearchCategories, defaultInsets);
		addComponent(1, 1, 1, 1, 0.0, 0.0, jpnlExplorer, jbtnSearchExecute, defaultInsets);
		addComponent(0, 2, 2, 1, 1.0, 1.0, jpnlExplorer, jlstFileList, defaultInsets);
	}

	/**
	 * Create Sub-sub- components in the movie panel including the JVLC plugin
	 * to play movies Maybe also a section to control the movie (play, pause,
	 * volume up/down, fast forward etc.)
	 */

	private class VolumeSliderListener implements ChangeListener {

		public void stateChanged(ChangeEvent ce) {
			JSlider source = (JSlider) ce.getSource();
			int newVolume = source.getValue();
			VLC.getMediaPlayer().setVolume(newVolume);
			if (JlabelVolume != null) {
				JlabelVolume.setText(Integer.toString(newVolume) + "%");
			}
		}
	}

	// TODO Timeline implementieren, Dauer des Films herausfinden, neue Position
	// setzen, moeglicherweise prozentual?
	private class TimelineSliderListener implements ChangeListener {

		public void stateChanged(ChangeEvent ce) {
			JSlider source = (JSlider) ce.getSource();
			VLC.getMediaPlayer().setTime((long) (((float) source.getValue() / 100) * VLC.getMediaPlayer().getLength()));
			System.out.println(((float) source.getValue() / 100));
		}
	}

	/** Indicator of current volume level **/
	private JLabel JlabelVolume;

	/** Slider for volume level **/
	private JSlider jsliderVolume;

	/** Indicator of progress of the media file **/
	private JLabel jlabelMovieTimeline;

	/** Slider for movie timeline **/
	private JSlider jsliderMovieProgress;

	/**
	 * @author Fabian Volkert
	 */
	private void buildMovieGUI() {
		jpnlMovie.add(VLC.getCanvas(), BorderLayout.CENTER);
		JPanel jpnlMovieControl = new JPanel();
		jpnlMovieControl.setLayout(new FlowLayout());

		JButton jbtnPlayMovie = new JButton("Play/Pause");
		jbtnPlayMovie.addActionListener(button_parser);
		jbtnPlayMovie.setActionCommand("jB_toggle_movie_playback");
		jbtnPlayMovie.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
		jpnlMovieControl.add(jbtnPlayMovie);

		JButton jbtnStopMovie = new JButton("Stop");
		jbtnStopMovie.addActionListener(button_parser);
		jbtnStopMovie.setActionCommand("jB_stop_movie");
		jbtnStopMovie.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
		jpnlMovieControl.add(jbtnStopMovie);

		JButton jbtnPreviousMovie = new JButton("|<");
		jbtnPreviousMovie.addActionListener(button_parser);
		jbtnPreviousMovie.setActionCommand("jB_previous_movie");
		jbtnPreviousMovie.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
		jpnlMovieControl.add(jbtnPreviousMovie);

		JButton jbtnNextMovie = new JButton(">|");
		jbtnNextMovie.addActionListener(button_parser);
		jbtnNextMovie.setActionCommand("jB_next_movie");
		jbtnNextMovie.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
		jpnlMovieControl.add(jbtnNextMovie);

		JButton jbtnPreviousChapter = new JButton("<<");
		jbtnPreviousChapter.addActionListener(button_parser);
		jbtnPreviousChapter.setActionCommand("jB_previous_chapter");
		jbtnPreviousChapter.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
		jpnlMovieControl.add(jbtnPreviousChapter);

		JButton jbtnNextChapter = new JButton(">>");
		jbtnNextChapter.addActionListener(button_parser);
		jbtnNextChapter.setActionCommand("jB_next_chapter");
		jbtnNextChapter.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
		jpnlMovieControl.add(jbtnNextChapter);

		jsliderVolume = new JSlider(JSlider.HORIZONTAL, VLC.getMinVolume(), VLC.getMaxVolume(),
		        ((VLC.getMinVolume() + VLC.getMaxVolume()) / 2));
		jpnlMovieControl.add(jsliderVolume);
		jsliderVolume.addChangeListener(new VolumeSliderListener());

		JlabelVolume = new JLabel(Integer.toString(((VLC.getMinVolume() + VLC.getMaxVolume()) / 2)) + "%");
		jpnlMovieControl.add(JlabelVolume);

		jpnlMovie.add(jpnlMovieControl, BorderLayout.SOUTH);

		JPanel jPanel_movie_north = new JPanel();
		jpnlMovie.add(jPanel_movie_north, BorderLayout.NORTH);

		jsliderMovieProgress = new JSlider(0, 100, 0);
		jsliderMovieProgress.setMajorTickSpacing(5);
		jsliderMovieProgress.addChangeListener(new TimelineSliderListener());
		jPanel_movie_north.add(jsliderMovieProgress, BorderLayout.CENTER);

		jlabelMovieTimeline = new JLabel();
		jlabelMovieTimeline.setText("0");

		jPanel_movie_north.add(jlabelMovieTimeline, BorderLayout.EAST);

	}

	/**
	 * Updates the state of the nominator for current volune level
	 * 
	 * @author Fabian Volkert
	 */
	public void update_volume_label() {
		// TODO Properly implement Volume slider and update; Label is bugged
		// right now
		if (VLC.getMediaPlayer() != null) {
			if (JlabelVolume != null) {
				JlabelVolume.setText(Integer.toString(VLC.getMediaPlayer().getVolume()) + "%");
			}
		}
	}

	/**
	 * Updates the state of the nominator for current progress in media file
	 * 
	 * @author Fabian Volkert
	 */
	public void update_timeline() {
		// TODO implement timeline bar, bugged now
		if (VLC.getMediaPlayer() != null) {
			jlabelMovieTimeline.setText(Long.toString(VLC.getMediaPlayer().getTime() / 1000));
			EmbeddedMediaPlayer media_player = VLC.getMediaPlayer();
			double procentual_of_movie = (double) media_player.getTime() / media_player.getLength();
			System.out.println(procentual_of_movie * 100);
			if (procentual_of_movie % 0 < 0.3) {
				System.out.println("got here");
				jsliderMovieProgress.setValue((int) procentual_of_movie);
			}
		}
	}

	/**
	 * Create Sub-sub-components in the intel panel for showing information on
	 * the selected video file in the explorer section
	 */
	private void buildIntelGUI() {
		// Here some information about the currently selected video file will be
		// stored.
	}

	/**
	 * This JMenuBar makes the windowmenu come alive
	 */
	private JMenuBar jmbMenu;

	/**
	 * The submenu File containing a direct way to open VLC and to close the
	 * window without pressing the red X.s
	 */
	private JMenu jmFile;

	/**
	 * MenuItem which allows to define the Path to VLC.exe manually.
	 */
	private JMenuItem jmiPathVLC;

	/**
	 * MenuItem which allows to add a file to the watchlist of displayed files
	 * to play.
	 */
	private JMenuItem jmiAddFile;

	/**
	 * MenuItem which allows to add a whole directory to the watchlist of
	 * displayed files to play.
	 */
	private JMenuItem jmiAddDirectory;

	/**
	 * MenuItem which allows the user to close the program.
	 */
	private JMenuItem jmiClose;

	/**
	 * The submenu Help containing the tutorial
	 */
	private JMenu jmHelp;

	/**
	 * MenuItem from which the user will get a pop-up window with a short
	 * tutorial on how to use the program.
	 */
	private JMenuItem jmiTutorial;

	/**
	 * The submenu About containing the website, the team, the code on GitHub
	 * and the journals so far
	 */
	private JMenu jmAbout;

	/**
	 * MenuItem from which the user can access the website of the project
	 */
	private JMenuItem jmiWebsite;

	/**
	 * MenuItem from which the user can access the team descriptions
	 */
	private JMenuItem jmiMeetTheTeam;

	/**
	 * MenuItem from which the user can look up the code on GitHub
	 */
	private JMenuItem jmiWatchCode;

	/**
	 * MenuItem from which the user can access the teams journals
	 */
	private JMenuItem jmiJournals;
	// More menu stuff to come

	/**
	 * Method for creating the menu with all submenus and menuItems + all
	 * eventhandlers
	 */
	private void buildMenuBar() {
		// MenuBar
		jmbMenu = new JMenuBar();
		setJMenuBar(jmbMenu);
		jmbMenu.setVisible(true);

		// MenuFile
		jmFile = new JMenu("File");
		jmiPathVLC = new JMenuItem("Define VLC.exe Path");
		jmiAddFile = new JMenuItem("Add File ...");
		jmiAddDirectory = new JMenuItem("Add Directory ...");
		jmiClose = new JMenuItem("Close");
		jmFile.add(jmiPathVLC);
		jmFile.add(jmiAddFile);
		jmFile.add(jmiAddDirectory);
		jmFile.add(jmiClose);

		// MenuHelp
		jmHelp = new JMenu("Help");
		jmiTutorial = new JMenuItem("Tutorial");
		jmHelp.add(jmiTutorial);

		// MenuAbout
		jmAbout = new JMenu("About");
		jmiWebsite = new JMenuItem("Website");
		jmiMeetTheTeam = new JMenuItem("Meet the Team");
		jmiWatchCode = new JMenuItem("Watch the Code in GitHub");
		jmiJournals = new JMenuItem("Programming-journals");
		jmAbout.add(jmiWebsite);
		jmAbout.add(jmiMeetTheTeam);
		jmAbout.add(jmiWatchCode);
		jmAbout.add(jmiJournals);

		// Adding all menus to the actual menuBar
		jmbMenu.add(jmFile);
		jmbMenu.add(jmHelp);
		jmbMenu.add(jmAbout);

		// Adding ActionListeners with URLs
		try {
			addURLActionListenerToMenuBarItem(jmiWebsite, new URI("http://cyril-casapao.github.io/vip-project/"));
			addURLActionListenerToMenuBarItem(jmiMeetTheTeam,
			        new URI("http://cyril-casapao.github.io/vip-project/team.html"));
			addURLActionListenerToMenuBarItem(jmiWatchCode, new URI("https://github.com/cyril-casapao/vip-project"));
			addURLActionListenerToMenuBarItem(jmiJournals,
			        new URI("http://cyril-casapao.github.io/vip-project/journals.html"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// Adding AL to the close button
		jmiClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}

}
