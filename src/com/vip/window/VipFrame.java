package com.vip.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;

import com.vip.Controller;
import com.vip.attributes.Video;
import com.vip.controllers.DatabaseController;
import com.vip.controllers.OMDBController;
import com.vip.controllers.SearchSortController;
import com.vip.media.VLC;

@SuppressWarnings("serial")
public class VipFrame extends JFrame {

	private VipFrame thisFrame=this;

	/**
	 * Constructor for building the frame and initialize all event handlers.
	 */
	public VipFrame() {
		super("VipFrame");
		OmdbRequest testReq = new OmdbRequest(OMDBController.getInstance().searchApi("Star Wars"));
		testReq.setVisible(true);
		testReq.setEnabled(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent we) {
				thisFrame.dispose();
				System.exit(0);
				// TODO catch exit wish of user.
		        // save stuff
			}
		});
		setMinimumSize(new Dimension(1280, 720));
		defaultInsets = new Insets(2, 2, 2, 2);
		changeGUILook();
		controller.initConfiguration();
		selectRootFolder();
		buildPanels();
		buildExplorerGUI();
		buildMovieGUI();
		buildIntelGUI();
		buildMenuBar();
		addFileListActionListener();
		searchForMovies(rootFolderPath);
		pack();
		requestFocus();
	}

	/**
	 * Outsourced method for detecting whether a root folder is already
	 * determined or has to be declared by the user
	 */
	private void selectRootFolder() {
		JOptionPane.showMessageDialog(this, "You have to select a root folder for you video collection!");
		rootFolderPath = getFilePath(2);
	}

	/**
	 * Method for getting the path of a selected folder
	 * 
	 * @return Absolute path to the file
	 * @param int
	 *            type Integer for choosing a different selection type of the
	 *            open dialog
	 */
	private String getFilePath(int type) {
		JFileChooser chooser = new JFileChooser();
		if (type == 2) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else if (type == 1) {
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		} else if (type != 1 && type != 2) {
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}

		int returnVal = chooser.showOpenDialog(rootPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return "";
	}

	/**
	 * Overloaded method for searching for files with a certain ending
	 * 
	 * @param filter
	 *            FileNameExtensionFilter for only making files searchable for
	 *            the JFileChooser, that have the specified ending.
	 * @return Absolute path to the file
	 */
	private String getFilePath(FileNameExtensionFilter filter) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(rootPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return "";
	}

	/**
	 * String Array for filtering the right files
	 */
	private final String[] movieExtensions = { "avi", "mp4", "webm", "flv", "mkv", "ogg", "mov", "wmv", "m4v" };

	/**
	 * String for the root-folder path
	 */
	private String rootFolderPath;

	/**
	 * Controller class, that contains methods to access data
	 */
	private Controller controller = new Controller(this);

	/**
	 * Controller for saving the movies into a database
	 */
	private DatabaseController dataController = new DatabaseController();

	/**
	 * Controller for searching and sorting videos
	 */
	private SearchSortController ssController = SearchSortController.getInstance();

	/**
	 * Getter for controller class
	 * 
	 * @return the responsible controller class for this frame.
	 */
	public Controller getController() {
		return controller;
	}

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
	 * Tries to change the look and feel of Java to Nimbus, a cross-platform GUI
	 * that comes with Java 6 update 10
	 */
	public void changeGUILook() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// If Nimbus is not found, it will be the default look and feel
		}
	}

	/**
	 * Create Subpanels Have to be called before any other building-methods
	 */
	private void buildPanels() {
		jpnlMain = new JPanel();
		jpnlMain.setLayout(new GridBagLayout());
		add(jpnlMain);

		jpnlExplorer = new JPanel();
		jpnlExplorer.setLayout(new GridBagLayout());
		jpnlExplorer.setBorder(BorderFactory.createTitledBorder("Explorer"));
		jpnlExplorer.setPreferredSize(new Dimension(250, 800));

		jpnlMovie = new JPanel();
		jpnlMovie.setLayout(new GridBagLayout());
		jpnlMovie.setBorder(BorderFactory.createTitledBorder("Movie"));

		jpnlIntel = new JPanel();
		jpnlIntel.setLayout(new GridBagLayout());
		jpnlIntel.setBorder(BorderFactory.createTitledBorder("Intel"));
		jpnlIntel.setPreferredSize(new Dimension(1200, 150));

		addComponent(0, 0, 1, 2, 0.35, 1.0, jpnlMain, jpnlExplorer, defaultInsets);
		addComponent(1, 0, 1, 1, 0.65, 0.7, jpnlMain, jpnlMovie, defaultInsets);
		addComponent(1, 1, 1, 1, 0.65, 0.3, jpnlMain, jpnlIntel, defaultInsets);
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
		jlstFileList = new JList<String>(ssController.getList());
		ssController.updateList(ssController.getMovies());
		jlstFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlstFileList.setSelectedIndex(0);
		JTextArea jtaScrollPaneText = new JTextArea(20, 1);
		JScrollPane scrollPane = new JScrollPane(jtaScrollPaneText);
		scrollPane.getViewport().setView(jlstFileList);
		jlstFileList.revalidate();

		jtfSearch = new JTextField(20);

		String[] searchCategories = { "By Length, increasing", // Index 0
		        "By length, decreasing", // Index 1
		        "By rating, increasing", // Index 2
		        "By rating, decreasing", }; // Index 3

		jcbSearchCategories = new JComboBox<String>(searchCategories);
		jcbSearchCategories.setEditable(false);
		jcbSearchCategories.setSelectedIndex(0);
		// JScrollPane jspSearchCategories = new
		// JScrollPane(jcbSearchCategories);

		jbtnSearchExecute = new JButton("Search");

		JPanel jpnlSearchControls = new JPanel();
		jpnlSearchControls.setLayout(new GridBagLayout());
		addComponent(0, 0, 2, 1, 1.0, 0.1, jpnlSearchControls, jtfSearch, defaultInsets);
		addComponent(0, 2, 1, 1, 0.5, 0.1, jpnlSearchControls, jcbSearchCategories, defaultInsets);
		addComponent(1, 2, 1, 1, 0.5, 0.1, jpnlSearchControls, jbtnSearchExecute, defaultInsets);

		addComponent(0, 0, 1, 1, 1, 0.05, jpnlExplorer, jpnlSearchControls, defaultInsets);
		addComponent(0, 1, 1, 1, 1, 0.95, jpnlExplorer, scrollPane, defaultInsets);

	}

	/**
	 * Indicator of current volume level
	 */
	private JButton jbtnVolume;

	/**
	 * Slider for volume level
	 */
	private JSlider jsliderVolume;

	/** Indicator for time left in the current movie **/
	private JLabel jlabelMovieTimer;

	/**
	 * Slider for movie timeline
	 */
	private JSlider jsliderMovieProgress;

	private boolean isFullscreen = false;

	private FullscreenDialog fullscreenDialog;

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setFullscreen(boolean newStatus) {
		isFullscreen = newStatus;
	}

	public FullscreenDialog getFullscreen() {
		return fullscreenDialog;
	}

	public void createFullscreen() {
		isFullscreen = true;

		MoviePanel jpnlVideoSurface = new MoviePanel(controller.getVLC(),
		        controller.getVLC().getMediaPlayer().getTime(), controller.getVLC().getCurrentPlaybackPath(),
		        controller.getVLC().getMediaPlayer().isPlaying());
		controller.getVLC().stopMedia();
		jpnlMovie.remove(controller.getVLC().getVideoSurface());
		fullscreenDialog = new FullscreenDialog(this, controller.getVLC(), controller.getKeyParser(), jpnlVideoSurface);
		fullscreenDialog.getSurface().componentResized(null);
	}

	public void disposeFullscreen() {
		MoviePanel jpnlVideoSurface = new MoviePanel(controller.getVLC(),
		        controller.getVLC().getMediaPlayer().getTime(), controller.getVLC().getCurrentPlaybackPath(),
		        controller.getVLC().getMediaPlayer().isPlaying());
		fullscreenDialog.getVLC().getMediaPlayer().stop();
		fullscreenDialog.dispose();
		fullscreenDialog = null;
		controller.getVLC().setSurface(jpnlVideoSurface);
		addComponent(0, 0, 1, 1, 1.0, 0.95, jpnlMovie, controller.getVLC().getVideoSurface(), defaultInsets);
		this.revalidate();
		controller.getVLC().getVideoSurface().componentResized(null);
		this.requestFocus();
		isFullscreen = false;
	}

	/**
	 * Create Sub-sub- components in the movie panel including the JVLC plugin
	 * to play movies Maybe also a section to control the movie (play, pause,
	 * volume up/down, fast forward etc.)
	 * 
	 * @author Fabian Volkert
	 */
	private void buildMovieGUI() {
		addComponent(0, 0, 1, 1, 1.0, 0.95, jpnlMovie, controller.getVLC().getVideoSurface(), defaultInsets);

		JPanel jpnlMovieControls = new JPanel();
		jpnlMovieControls.setLayout(new GridBagLayout());
		addComponent(0, 1, 1, 1, 1.0, 0.05, jpnlMovie, jpnlMovieControls, defaultInsets);

		JButton jbtnPlayMovie = new JButton("Play/Pause");
		jbtnPlayMovie.addActionListener(controller.getButtonParser());
		jbtnPlayMovie.setActionCommand("jbtnToggleMoviePlayback");

		JButton jbtnStopMovie = new JButton("Stop");
		jbtnStopMovie.addActionListener(controller.getButtonParser());
		jbtnStopMovie.setActionCommand("jbtnStopMovie");

		JButton jbtnPreviousMovie = new JButton("Previous Media");
		jbtnPreviousMovie.addActionListener(controller.getButtonParser());
		jbtnPreviousMovie.setActionCommand("jbtnPreviousMovie");

		JButton jbtnNextMovie = new JButton("Next Media");
		jbtnNextMovie.addActionListener(controller.getButtonParser());
		jbtnNextMovie.setActionCommand("jbtnNextMovie");

		JButton jbtnPreviousChapter = new JButton("Previous Chapter");
		jbtnPreviousChapter.addActionListener(controller.getButtonParser());
		jbtnPreviousChapter.setActionCommand("jbtnJumpBack");

		JButton jbtnNextChapter = new JButton("Next Chapter");
		jbtnNextChapter.addActionListener(controller.getButtonParser());
		jbtnNextChapter.setActionCommand("jbtnJumpForward");

		jsliderVolume = new JSlider(JSlider.HORIZONTAL, VLC.getMinVolume(), VLC.getMaxVolume(),
		        ((VLC.getMinVolume() + VLC.getMaxVolume()) / 2));
		jsliderVolume.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				JSlider jslider = (JSlider) me.getSource();
				BasicSliderUI ui = (BasicSliderUI) jslider.getUI();
				int newVolume = ui.valueForXPosition(me.getX());
				controller.getVLC().setVolume(newVolume);
			}
		});

		jbtnVolume = new JButton(Integer.toString(((VLC.getMinVolume() + VLC.getMaxVolume()) / 2)) + "%");
		jbtnVolume.addActionListener(controller.getButtonParser());
		jbtnVolume.setActionCommand("jbtnVolume");

		JButton jbtnFullscreen = new JButton("Fullscreen");
		jbtnFullscreen.addActionListener(controller.getButtonParser());
		jbtnFullscreen.setActionCommand("jbtnFullscreen");

		jsliderMovieProgress = new JSlider(0, 100, 0);
		jsliderMovieProgress.setMajorTickSpacing(5);
		jsliderMovieProgress.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				JSlider jslider = (JSlider) me.getSource();
				BasicSliderUI ui = (BasicSliderUI) jslider.getUI();
				int newTime = ui.valueForXPosition(me.getX());
				controller.getVLC().getMediaPlayer().setTime(newTime);
			}
		});

		jlabelMovieTimer = new JLabel();
		jlabelMovieTimer.setHorizontalAlignment(SwingConstants.CENTER);
		jlabelMovieTimer.setText("00:00:00 / 00:00:00   0%");

		if (!controller.getVLC().isVLCInstalled()) {
			jbtnPlayMovie.setEnabled(false);
			jbtnStopMovie.setEnabled(false);
			jbtnNextChapter.setEnabled(false);
			jbtnPreviousChapter.setEnabled(false);
			jbtnNextMovie.setEnabled(false);
			jbtnPreviousMovie.setEnabled(false);
			jbtnFullscreen.setEnabled(false);
			jbtnVolume.setEnabled(false);
			jsliderVolume.setEnabled(false);
			jsliderMovieProgress.setEnabled(false);
			jlabelMovieTimer.setText("");
			controller.getVLC().getVideoSurface().setBackground(Color.GRAY);
			TitledBorder titledBorderMoviePanel = BorderFactory.createTitledBorder("Movie");
			titledBorderMoviePanel.setTitleColor(Color.GRAY);
			jpnlMovie.setBorder(titledBorderMoviePanel);
		}

		addComponent(0, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnPlayMovie, defaultInsets);
		addComponent(1, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnStopMovie, defaultInsets);
		addComponent(2, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnPreviousMovie, defaultInsets);
		addComponent(3, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnNextMovie, defaultInsets);
		addComponent(4, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnPreviousChapter, defaultInsets);
		addComponent(5, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnNextChapter, defaultInsets);
		addComponent(6, 1, 2, 1, 1, 1, jpnlMovieControls, jsliderVolume, defaultInsets);
		addComponent(8, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnVolume, defaultInsets);
		addComponent(9, 1, 1, 1, 1, 1, jpnlMovieControls, jbtnFullscreen, defaultInsets);
		addComponent(0, 0, 9, 1, 1, 1, jpnlMovieControls, jsliderMovieProgress, defaultInsets);
		addComponent(9, 0, 1, 1, 0, 1, jpnlMovieControls, jlabelMovieTimer, defaultInsets);
	}

	private void initProgressBar() {
		int movieLength = (int) controller.getVLC().getMediaPlayer().getLength();
		jsliderMovieProgress.setMaximum(movieLength);
		jsliderMovieProgress.setMinimum(0);
		revalidate();
		jsliderMovieProgress.repaint();
	}

	/**
	 * Updates the state of the nominators for current progress in media file
	 */
	public void updateGUI() {
		VLC vlcInstance = controller.getVLC();

		if (vlcInstance.isVLCInstalled() && vlcInstance.getMediaPlayer() != null
		        && vlcInstance.getMediaPlayer().getLength() != -1) {
			if (controller.getVLC().shouldInitMedia()) {
				initProgressBar();
				controller.getVLC().setMediaInitState(false);
			} else {

				// If initialization fails: retry
				if (jsliderMovieProgress.getMaximum() == 0) {
					controller.getVLC().setMediaInitState(true);
				}

				updateTimelineLabels();
				updateVolumeSlider();
				updateRatingSlider();

				int currentMovieTime = (int) controller.getVLC().getMediaPlayer().getTime();
				jsliderMovieProgress.setValue(currentMovieTime);
			}
		} else {
			if (vlcInstance.isVLCInstalled()) {
				jlabelMovieTimer.setText("00:00:00 / 00:00:00   0%");
			}
		}
	}

	/**
	 *
	 */
	private void updateTimelineLabels() {
		Double procentualProgress = ((double) controller.getVLC().getMediaPlayer().getTime()
		        / controller.getVLC().getMediaPlayer().getLength()) * 100;
		// is newTime is not a valid Number, we display a default Text
		int hoursPassed = 0;
		int minutesPassed = 0;
		int secondsPassed = 0;
		int hoursTotal = 0;
		int minutesTotal = 0;
		int secondsTotal = 0;
		hoursPassed = (int) (controller.getVLC().getMediaPlayer().getTime() / 3600000);
		minutesPassed = (int) (controller.getVLC().getMediaPlayer().getTime() / 60000 % 60);
		secondsPassed = (int) (controller.getVLC().getMediaPlayer().getTime() / 1000 % 60);
		hoursTotal = (int) (controller.getVLC().getMediaPlayer().getLength() / 3600000);
		minutesTotal = (int) (controller.getVLC().getMediaPlayer().getLength() / 60000 % 60);
		secondsTotal = (int) (controller.getVLC().getMediaPlayer().getLength() / 1000 % 60);

		if (procentualProgress.isNaN() || procentualProgress.isInfinite()) {
			String newLabelText = String.format("%02d:%02d:%02d / %02d:%02d:%02d   000,0", hoursPassed, minutesPassed,
			        secondsPassed, hoursTotal, minutesTotal, secondsTotal, procentualProgress);
			jlabelMovieTimer.setText(newLabelText + "%");
		} else {
			if (procentualProgress > 100) {
				procentualProgress = 100.0;
			}
			String newLabelText = String.format("%02d:%02d:%02d / %02d:%02d:%02d   %4.1f", hoursPassed, minutesPassed,
			        secondsPassed, hoursTotal, minutesTotal, secondsTotal, procentualProgress);
			jlabelMovieTimer.setText(newLabelText + "%");
		}
	}

	/**
	 * 
	 */
	public void updateVolumeSlider() {
		jbtnVolume.setText(controller.getVLC().getMediaPlayer().getVolume() + "%");
		jsliderVolume.setValue(controller.getVLC().getMediaPlayer().getVolume());
	}

	public void updateRatingSlider() {
		jlabelRating.setText(Double.toString(((double) jsliderRating.getValue() / 2.0)));
	}

	/**
	 * Method for delivering KeyParser the search TextField
	 */
	public JComponent get_jtfSearch() {
		return jtfSearch;
	}

	private JSlider jsliderRating;

	private JLabel jlabelRating;

	private JTextArea jtaMediaInfo;

	/**
	 * Create Sub-sub-components in the intel panel for showing information on
	 * the selected video file in the explorer section
	 */
	private void buildIntelGUI() {
		// Here some information about the currently selected video file will be
		// stored.

		JPanel jpnlIntelNorth = new JPanel();
		jpnlIntelNorth.setLayout(new GridBagLayout());

		jsliderRating = new JSlider(0, 20, 0);
		jsliderRating.setMajorTickSpacing(0);
		jsliderRating.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				JSlider jslider = (JSlider) me.getSource();
				BasicSliderUI ui = (BasicSliderUI) jslider.getUI();
				int newRating = ui.valueForXPosition(me.getX());
				// TODO pass new rating to video object -> get item from list
		        // (it's just String right now, so there must be some sort of
		        // workaround for this)
			}
		});

		jlabelRating = new JLabel(Integer.toString(jsliderRating.getValue()));
		jlabelRating.setHorizontalAlignment(SwingConstants.CENTER);

		addComponent(0, 0, 9, 1, 0.9, 1, jpnlIntelNorth, jsliderRating, defaultInsets);
		addComponent(9, 0, 1, 1, 0.1, 1, jpnlIntelNorth, jlabelRating, defaultInsets);

		jpnlIntelNorth.setBorder(BorderFactory.createTitledBorder("Rating"));
		jlabelRating.setBorder(BorderFactory.createTitledBorder(""));

		jtaMediaInfo = new JTextArea(20, 1);
		jtaMediaInfo.setEditable(false);
		jtaMediaInfo.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(jtaMediaInfo);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		jtaMediaInfo.setText(
		        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labor.Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labor.Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labor.Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labor");

		addComponent(0, 0, 1, 1, 1, 0.1, jpnlIntel, jpnlIntelNorth, defaultInsets);
		addComponent(0, 1, 1, 1, 1, 0.9, jpnlIntel, scrollPane, defaultInsets);
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
	 * 
	 */
	private JMenuItem jmiAddDirectory;

	/**
	 * MenuItem which allows the user to save his/her whole progress in the
	 * database, which will keep everything over to the next boot.
	 */
	private JMenuItem jmiSaveAll;

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
		jmiSaveAll = new JMenuItem("Save All");
		jmiClose = new JMenuItem("Close");
		jmFile.add(jmiPathVLC);
		jmFile.add(jmiAddFile);
		jmFile.add(jmiAddDirectory);
		jmFile.add(jmiSaveAll);
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

		// Adding ActionListener to VLC-Path MenuItem which say, that it is
		// recommended that you
		// have installed the 64-Bit version of VLC in order to run VIP
		// smoothly.
		jmiPathVLC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(rootPane, "In order to run the 'Video Information Program' smoothly "
		                + "you have to install the 64-Bit Version of Video Lan Player! "
		                + "\n It can be found on the Website: http://www.videolan.org/vlc/download-windows.html");
			}
		});

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

		jmiAddFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path = getFilePath(new FileNameExtensionFilter("Video Files", movieExtensions));
				ssController.addMovieToList(new Video(path));
				ssController.updateList(ssController.getMovies());
			}
		});

		jmiAddDirectory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path = getFilePath(2);
				searchForMovies(path);
			}
		});

		jmiSaveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dataController.saveAll(ssController.getMovies());
			}
		});
	}

	/**
	 * Add ActionListener to the FileList, so a movie will be played if
	 * double-clicked
	 */
	private void addFileListActionListener() {
		jlstFileList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(ev)) {
					controller.getVLC()
		                    .loadMedia(ssController.getVideoByIndex(jlstFileList.getSelectedIndex()).getFilePath());
					controller.getVLC().toggleMediaPlayback();
				}
			}

			@Override
			public void mousePressed(MouseEvent ev) {
				if (SwingUtilities.isRightMouseButton(ev)) {
					// movies.get(jlstFileList.getSelectedIndex()).activateContextVideoMenu(ev);
				}
			}
		});
	}

	/**
	 * Searching for movie Files with a folder path.
	 * 
	 * @param path
	 *            Path of the folder to search for movies
	 */
	private void searchForMovies(String filePath) {
		Path path = Paths.get(filePath);
		ArrayList<File> fileList = new ArrayList<File>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					searchForMovies(entry.toString());
				}
				String tempFilePath = entry.toString();
				String[] tempFilePathSplitted = tempFilePath.split(Pattern.quote("."));
				for (int i = 0; i < movieExtensions.length; i++) {
					if (tempFilePathSplitted[tempFilePathSplitted.length - 1].equalsIgnoreCase(movieExtensions[i])) {
						fileList.add(entry.toFile());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < fileList.size(); i++) {
			ssController.addMovieToList(new Video(fileList.get(i).getAbsolutePath()));
		}

		ssController.updateList(ssController.getMovies());
	}
}
