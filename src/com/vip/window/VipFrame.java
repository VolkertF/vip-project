package com.vip.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;

import com.vip.Main;
import com.vip.attributes.Video;
import com.vip.controllers.Controller;
import com.vip.controllers.DatabaseController;
import com.vip.controllers.SearchSortController;
import com.vip.media.VLC;

@SuppressWarnings("serial")
public class VipFrame extends JFrame implements ComponentListener {

	/**
	 * Constructor for building the frame and initialize all event handlers.
	 */
	public VipFrame() {
		super("VipFrame");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent we) {
				// TODO ?
		        // Right now on exit we're default saving the database's
		        // changes.
		        // Might could open a window and ask for saving changes
		        // Would need a "changed" boolean
				controller.getVLC().stopMedia();
				dataController.saveAll(SearchSortController.getInstance().getMovies());
				dispose();
				System.exit(0);
			}
		});
		setMinimumSize(new Dimension(1280, 720));
		defaultInsets = new Insets(2, 2, 2, 2);
		changeGUILook();
		controller.initConfiguration();
		buildPanels();
		buildExplorerGUI();
		buildMovieGUI();
		buildIntelGUI();
		buildMenuBar();
		addFileListActionListener();
		dataController.loadVideos();
		if (SearchSortController.getInstance().getMovies().size() <= 0) {
			selectRootFolder();
			searchForMovies(rootFolderPath);
		}
		pack();
		setScreenLocation();
		requestFocus();
		setVisible(true);
		controller.getVLC().switchSurface(mainMoviePanel, false);
	}

	/** Indicates the default delay after firing a resizing event in ms **/
	private final int RESIZE_REFRESH_RATE = 50;

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
	private DatabaseController dataController = new DatabaseController();;

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
	private JPanel jpnlMain;

	/**
	 * This JList will fill with the files in all searching directories the
	 * program overwatches, so basically every movie file found on the harddrive
	 */
	private JList<Video> jlstFileList;

	/**
	 * This JTextField is for entering your private search stuff into a
	 * textField to search your library for this
	 */
	private JTextField jtfSearch;

	/**
	 * This JComboBox is ment for sorting your search by different categories
	 */
	private JComboBox<String> jcbSortCategories;

	/**
	 * This JComboBox is ment for searching in different sections of the movie
	 * files.
	 */
	private JComboBox<String> jcbSearchCategories;

	/**
	 * By pressing this JButton the search by the chosen searching-categories
	 * and the entered keywords is executed.
	 */
	private JButton jbtnSearchExecute;

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

	/**
	 * Reference to the panel in the movie panel used as canvas to draw the
	 * movie
	 **/
	private MoviePanel mainMoviePanel;

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
	 * MenuItem which allows the user to reset the database, but not the actual
	 * file list. So every information stored in the database will be deleted.
	 */
	private JMenuItem jmiResetDatabase;

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

	/** MenuItem which will show the user credits on used resources **/
	private JMenuItem jmiCredits;

	/** The slider that is used to manipulate the current video's rating **/
	private JSlider jsliderRating;

	/**
	 * The label that indicates the current value of the selected video's rating
	 **/
	private JLabel jlabelRating;

	/** The Textarea that contains the currently selected video's intel **/
	private JTextArea jtaMediaInfo;

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
	 * Getter for controller class
	 * 
	 * @return the responsible controller class for this frame.
	 */
	public Controller getController() {
		return controller;
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
		// jpnlExplorer.setBorder(BorderFactory.createTitledBorder("Explorer"));
		jpnlExplorer.setBorder(BorderFactory.createTitledBorder(""));
		jpnlExplorer.setPreferredSize(new Dimension(250, 800));

		jpnlMovie = new JPanel();
		jpnlMovie.setLayout(new GridBagLayout());
		// jpnlMovie.setBorder(BorderFactory.createTitledBorder("Movie"));
		jpnlMovie.setBorder(BorderFactory.createTitledBorder(""));

		jpnlIntel = new JPanel();
		jpnlIntel.setLayout(new GridBagLayout());
		// jpnlIntel.setBorder(BorderFactory.createTitledBorder("Intel"));
		jpnlIntel.setBorder(BorderFactory.createTitledBorder(""));
		jpnlIntel.setPreferredSize(new Dimension(1200, 150));

		addComponent(0, 0, 1, 2, 0.35, 1.0, jpnlMain, jpnlExplorer, defaultInsets);
		addComponent(1, 0, 1, 1, 0.65, 0.7, jpnlMain, jpnlMovie, defaultInsets);
		addComponent(1, 1, 1, 1, 0.65, 0.3, jpnlMain, jpnlIntel, defaultInsets);
	}

	/**
	 * Create Sub-sub-panels in the explorer panel
	 */
	private void buildExplorerGUI() {
		jlstFileList = new JList<Video>(SearchSortController.getInstance().getList());
		SearchSortController.getInstance().updateList(SearchSortController.getInstance().getMovies());
		SearchSortController.getInstance().setController(controller);
		jlstFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlstFileList.setSelectedIndex(0);
		JTextArea jtaScrollPaneText = new JTextArea(20, 1);
		JScrollPane scrollPane = new JScrollPane(jtaScrollPaneText);
		scrollPane.getViewport().setView(jlstFileList);
		jlstFileList.revalidate();

		jtfSearch = new JTextField(20);

		String[] sortCategories = { "By Title", // Index 0
		        "By Country", // Index 1
		        "By Personal Rating", // Index 2
		        "By IMDb Rating", // Index 3
		        "By Release Date", // Index 4
		};

		jcbSortCategories = new JComboBox<String>(sortCategories);
		jcbSortCategories.setEditable(false);
		jcbSortCategories.setSelectedIndex(0);

		String[] searchCategories = { "Default", "By Title", "By Director", "By Country", "By Cast", "By Genre",
		        "By Writers", "By Release Date", "By Plot" };

		jcbSearchCategories = new JComboBox<String>(searchCategories);
		jcbSearchCategories.setEditable(false);
		jcbSearchCategories.setSelectedIndex(0);

		jbtnSearchExecute = new JButton("Search");
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sortChoice = jcbSortCategories.getSelectedIndex();
				if (sortChoice == 4) {
					SearchSortController.getInstance().sortByReleaseDate();
				} else if (sortChoice == 1) {
					SearchSortController.getInstance().sortByCountry();
				} else if (sortChoice == 2) {
					SearchSortController.getInstance().sortByPersonalRating();
				} else if (sortChoice == 3) {
					SearchSortController.getInstance().sortByImdbRating();
				} else {
					SearchSortController.getInstance().sortByTitle();
				}
				int searchChoice = jcbSearchCategories.getSelectedIndex();
				if (searchChoice == 1) {
					SearchSortController.getInstance().searchByTitle(jtfSearch.getText());
				} else if (searchChoice == 2) {
					SearchSortController.getInstance().searchByDirector(jtfSearch.getText());
				} else if (searchChoice == 3) {
					SearchSortController.getInstance().searchByCountry(jtfSearch.getText());
				} else if (searchChoice == 4) {
					SearchSortController.getInstance().searchByCast(jtfSearch.getText());
				} else if (searchChoice == 5) {
					SearchSortController.getInstance().searchByGenre(jtfSearch.getText());
				} else if (searchChoice == 6) {
					SearchSortController.getInstance().searchByWriters(jtfSearch.getText());
				} else if (searchChoice == 7) {
					SearchSortController.getInstance().searchByReleaseDate(jtfSearch.getText());
				} else if (searchChoice == 8) {
					SearchSortController.getInstance().searchByPlot(jtfSearch.getText());
				} else {
					SearchSortController.getInstance().searchAll(jtfSearch.getText());
				}
			}
		};
		jbtnSearchExecute.addActionListener(action);
		jtfSearch.addActionListener(action);

		JPanel jpnlSearchControls = new JPanel();
		jpnlSearchControls.setLayout(new GridBagLayout());
		addComponent(0, 0, 2, 1, 1.0, 0.1, jpnlSearchControls, jtfSearch, defaultInsets);
		addComponent(0, 2, 1, 1, 0.5, 0.1, jpnlSearchControls, jcbSortCategories, defaultInsets);
		addComponent(1, 1, 3, 1, 0.5, 0.1, jpnlSearchControls, jcbSearchCategories, defaultInsets);
		addComponent(1, 2, 1, 1, 0.5, 0.1, jpnlSearchControls, jbtnSearchExecute, defaultInsets);

		addComponent(0, 0, 1, 1, 1, 0.05, jpnlExplorer, jpnlSearchControls, defaultInsets);
		addComponent(0, 1, 1, 1, 1, 0.95, jpnlExplorer, scrollPane, defaultInsets);

	}

	// Not gonna comment this. should be obious what this is!
	private ImageIcon imgIconPlay = null;
	private ImageIcon imgIconPause = null;
	private ImageIcon imgIconStop = null;
	private ImageIcon imgIconNextChapter = null;
	private ImageIcon imgIconPreviousChapter = null;
	private ImageIcon imgIconNextMovie = null;
	private ImageIcon imgIconPreviousMovie = null;
	private ImageIcon imgIconVolumeMuted = null;
	private ImageIcon imgIconVolumeLow = null;
	private ImageIcon imgIconVolumeMedium = null;
	private ImageIcon imgIconVolumeHigh = null;
	private ImageIcon imgIconFullscreen = null;
	private ImageIcon imgIconHate = null;
	private ImageIcon imgIconBad = null;
	private ImageIcon imgIconIndifferent = null;
	private ImageIcon imgIconGood = null;
	private ImageIcon imgIconPerfect = null;
	private ImageIcon imgIconDelete = null;

	/**
	 * Tries to load the image icons into the program
	 */
	public void initializeImageIcons() {
		URL url = Main.class.getResource("/play.png");
		Image img = Toolkit.getDefaultToolkit().createImage(url);
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconPlay = new ImageIcon(img);
		}

		url = Main.class.getResource("/pause.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconPause = new ImageIcon(img);
		}

		url = Main.class.getResource("/stop.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconStop = new ImageIcon(img);
		}

		url = Main.class.getResource("/chapter_next.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconNextChapter = new ImageIcon(img);
		}

		url = Main.class.getResource("/chapter_previous.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconPreviousChapter = new ImageIcon(img);
		}

		url = Main.class.getResource("/movie_next.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconNextMovie = new ImageIcon(img);
		}

		url = Main.class.getResource("/movie_previous.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconPreviousMovie = new ImageIcon(img);
		}

		url = Main.class.getResource("/volume_muted.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconVolumeMuted = new ImageIcon(img);
		}

		url = Main.class.getResource("/volume_low.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconVolumeLow = new ImageIcon(img);
		}

		url = Main.class.getResource("/volume_medium.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconVolumeMedium = new ImageIcon(img);
		}

		url = Main.class.getResource("/volume_high.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconVolumeHigh = new ImageIcon(img);
		}

		url = Main.class.getResource("/fullscreen.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconFullscreen = new ImageIcon(img);
		}

		url = Main.class.getResource("/hate.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconHate = new ImageIcon(img);
		}

		url = Main.class.getResource("/bad.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconBad = new ImageIcon(img);
		}

		url = Main.class.getResource("/indifferent.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconIndifferent = new ImageIcon(img);
		}

		url = Main.class.getResource("/good.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconGood = new ImageIcon(img);
		}

		url = Main.class.getResource("/perfect.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconPerfect = new ImageIcon(img);
		}

		url = Main.class.getResource("/bin.png");
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			imgIconDelete = new ImageIcon(img);
		}
	}

	/** Reference to the playbutton of the mainframe **/
	JButton jbtnPlayMovie;

	/** Font used from to text area to display information **/
	private final Font plainFont = new Font("Tahoma", Font.PLAIN, 14);

	/**
	 * Create Sub-sub- components in the movie panel including the JVLC plugin
	 * to play movies Maybe also a section to control the movie (play, pause,
	 * volume up/down, fast forward etc.)
	 */
	private void buildMovieGUI() {
		mainMoviePanel = new MoviePanel(controller.getVLC());
		addComponent(0, 0, 1, 1, 1.0, 0.95, jpnlMovie, mainMoviePanel, defaultInsets);
		addComponentListener(this);

		JPanel jpnlMovieControls = new JPanel();
		jpnlMovieControls.setLayout(new GridBagLayout());
		addComponent(0, 1, 1, 1, 1.0, 0.05, jpnlMovie, jpnlMovieControls, defaultInsets);

		initializeImageIcons();

		jbtnPlayMovie = new JButton();
		if (imgIconPlay != null) {
			jbtnPlayMovie.setIcon(imgIconPlay);
		} else {
			jbtnPlayMovie.setText("Play");
		}
		jbtnPlayMovie.addActionListener(controller.getButtonParser());
		jbtnPlayMovie.setActionCommand("jbtnToggleMoviePlayback");

		JButton jbtnStopMovie = new JButton();
		if (imgIconStop != null) {
			jbtnStopMovie.setIcon(imgIconStop);
		} else {
			jbtnStopMovie.setText("Stop");
		}
		jbtnStopMovie.setToolTipText("Stops current movie playback");
		jbtnStopMovie.addActionListener(controller.getButtonParser());
		jbtnStopMovie.setActionCommand("jbtnStopMovie");

		JButton jbtnPreviousMovie = new JButton();
		if (imgIconStop != null) {
			jbtnPreviousMovie.setIcon(imgIconPreviousMovie);
		} else {
			jbtnPreviousMovie.setText("Previous Movie");
		}
		jbtnPreviousMovie.setToolTipText("Jumps to the previous movie item in the list");
		jbtnPreviousMovie.addActionListener(controller.getButtonParser());
		jbtnPreviousMovie.setActionCommand("jbtnPreviousMovie");

		JButton jbtnNextMovie = new JButton();
		if (imgIconNextMovie != null) {
			jbtnNextMovie.setIcon(imgIconNextMovie);
		} else {
			jbtnNextMovie.setText("Next Movie");
		}
		jbtnNextMovie.setToolTipText("Jumps to the next movie item in the list");
		jbtnNextMovie.addActionListener(controller.getButtonParser());
		jbtnNextMovie.setActionCommand("jbtnNextMovie");

		JButton jbtnPreviousChapter = new JButton();
		if (imgIconPreviousChapter != null) {
			jbtnPreviousChapter.setIcon(imgIconPreviousChapter);
		} else {
			jbtnPreviousChapter.setText("Previous Chapter");
		}
		jbtnPreviousChapter.setToolTipText(
		        "<html>Jumps to the previous chapter of the currently playing movie,<br> if it exits. otherwise it will jump back in the movie a few percent of total time</html>");
		jbtnPreviousChapter.addActionListener(controller.getButtonParser());
		jbtnPreviousChapter.setActionCommand("jbtnPreviousChapter");

		JButton jbtnNextChapter = new JButton();
		if (imgIconNextChapter != null) {
			jbtnNextChapter.setIcon(imgIconNextChapter);
		} else {
			jbtnNextChapter.setText("Previous Chapter");
		}
		jbtnNextChapter.setToolTipText(
		        "<html>Jumps to the next chapter of the currently playing movie,<br> if it exits. otherwise it will jump forward in the movie a few percent of total time</html>");
		jbtnNextChapter.addActionListener(controller.getButtonParser());
		jbtnNextChapter.setActionCommand("jbtnNextChapter");

		jbtnVolume = new JButton(Integer.toString(((VLC.getMinVolume() + VLC.getMaxVolume()) / 2)) + "%");
		if (imgIconVolumeMedium != null) {
			jbtnVolume.setIcon(imgIconVolumeMedium);
		}
		jbtnVolume.setToolTipText("Mutes the movie if clicked");
		jbtnVolume.addActionListener(controller.getButtonParser());
		jbtnVolume.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if (SwingUtilities.isMiddleMouseButton(me)) {
					controller.getVLC().setVolume(VLC.getMaxVolume() / 2);
				}
			}
		});
		jbtnVolume.setActionCommand("jbtnVolume");

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

		JButton jbtnFullscreen = new JButton();
		if (imgIconFullscreen != null) {
			jbtnFullscreen.setIcon(imgIconFullscreen);
		} else {
			jbtnFullscreen.setText("Fullscreen");
		}
		jbtnFullscreen.setToolTipText("Sets display mode to fullscreen");
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
		jlabelMovieTimer.setFont(plainFont);
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
			mainMoviePanel.setBackground(Color.GRAY);
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

	/**
	 * Initializes the timeline below the canvas. A movie needs to be loaded
	 **/
	private void initProgressBar() {
		int movieLength = (int) controller.getVLC().getMediaPlayer().getLength();
		jsliderMovieProgress.setMaximum(movieLength);
		jsliderMovieProgress.setMinimum(0);
		revalidate();
		jsliderMovieProgress.repaint();
	}

	/**
	 * Updates the GUI's elements on a regular basis
	 */
	public void updateGUI() {
		if (!controller.isFullscreen()) {
			VLC vlcInstance = controller.getVLC();
			if (jlstFileList.getSelectedIndex() >= 0) {
				controller.updateIntel(
				        SearchSortController.getInstance().getVideoByIndex(jlstFileList.getSelectedIndex()));
			}
			updateRatingIndicators();

			if (vlcInstance.isVLCInstalled() && vlcInstance.getMediaPlayer() != null
			        && vlcInstance.getMediaPlayer().getLength() != -1) {
				if (controller.getVLC().shouldInitProgressBar()) {
					initProgressBar();
					controller.getVLC().setProgressBarInitState(false);
				} else {

					// If initialization fails: retry
					if (jsliderMovieProgress.getMaximum() == 0) {
						controller.getVLC().setProgressBarInitState(true);
					}
					updateVolumeIndicators();
					updateTimelineLabel();

					if (vlcInstance.getMediaPlayer().isPlaying()) {
						if (imgIconPlay != null) {
							jbtnPlayMovie.setIcon(imgIconPlay);
						} else {
							jbtnPlayMovie.setText("Play");
						}
						jbtnPlayMovie.setToolTipText("Click to stop playback");
					} else {
						if (imgIconPause != null) {
							jbtnPlayMovie.setIcon(imgIconPause);
						} else {
							jbtnPlayMovie.setText("Pause");
						}
						jbtnPlayMovie.setToolTipText("Click to start playback");
					}

					int currentMovieTime = (int) controller.getVLC().getMediaPlayer().getTime();
					jsliderMovieProgress.setValue(currentMovieTime);
				}
			} else {
				if (vlcInstance.isVLCInstalled()) {
					jsliderMovieProgress.setValue(0);
					jlabelMovieTimer.setText("00:00:00 / 00:00:00   0%");

					jsliderVolume.setValue((VLC.getMinVolume() + VLC.getMaxVolume()) / 2);
				}
			}
		}
	}

	/**
	 * Updates the label that indicates the current time and the overall time of
	 * the currently playing movie
	 **/
	private void updateTimelineLabel() {
		String newLabelText = controller.getVLC().getFormattedTimeToString();
		jlabelMovieTimer.setText(newLabelText + "%");
	}

	/**
	 * Updates the indicators(the slider and the next-to-it label) to the
	 * current volume level.
	 **/
	public void updateVolumeIndicators() {
		int volume = controller.getVLC().getMediaPlayer().getVolume();
		if (volume >= 0) {
			jbtnVolume.setText(volume + "%");
			if (volume == 0) {
				if (imgIconVolumeMuted != null) {
					jbtnVolume.setIcon(imgIconVolumeMuted);
					jbtnVolume.setToolTipText("Unmutes the movie if clicked");
				}
			} else if (volume < 33) {
				if (imgIconVolumeLow != null) {
					jbtnVolume.setIcon(imgIconVolumeLow);
				}
			} else if (volume < 66) {
				if (imgIconVolumeMedium != null) {
					jbtnVolume.setIcon(imgIconVolumeMedium);
				}
			} else {
				if (imgIconVolumeHigh != null) {
					jbtnVolume.setIcon(imgIconVolumeHigh);
				}
			}
			jsliderVolume.setValue(controller.getVLC().getMediaPlayer().getVolume());
		} else {
			jbtnVolume.setText("0%");
			jsliderVolume.setValue(0);
			if (imgIconVolumeMuted != null) {
				jbtnVolume.setIcon(imgIconVolumeMuted);
			}
		}
	}

	/**
	 * Updates the indicators(slider and next-to-it label) to the current
	 * personal rating of the selected video
	 **/
	public void updateRatingIndicators() {
		if (jlstFileList.getSelectedIndex() >= 0) {
			jsliderRating.setValue((int) SearchSortController.getInstance()
			        .getVideoByIndex(jlstFileList.getSelectedIndex()).getPersonalRating());
		} else {
			jsliderRating.setValue(0);
		}
		String ratingString = String.format("%3.1f", (jsliderRating.getValue() / 2.0));
		jlabelRating.setText(ratingString);
		double rating = jsliderRating.getValue() / 2.0;
		if (rating == 0.0) {
			jlabelRating.setIcon(imgIconHate);
		} else if (rating < 3.5) {
			jlabelRating.setIcon(imgIconBad);
		} else if (rating < 7.0) {
			jlabelRating.setIcon(imgIconIndifferent);
		} else if (rating < 10.0) {
			jlabelRating.setIcon(imgIconGood);
		} else {
			jlabelRating.setIcon(imgIconPerfect);
		}
	}

	/**
	 * A Method for getting the FileList that display the movies
	 * 
	 * @return The JList of String containing the files
	 */
	public JList<Video> getFileList() {
		return jlstFileList;
	}

	/**
	 * Method for delivering KeyParser the search TextField
	 */
	public JComponent get_jtfSearch() {
		return jtfSearch;
	}

	/** Font used from to text area to display information **/
	private final Font boldFont = new Font("Tahoma", Font.BOLD, 14);

	/**
	 * Create Sub-sub-components in the intel panel for showing information on
	 * the selected video file in the explorer section
	 */
	private void buildIntelGUI() {
		JPanel jpnlIntelNorth = new JPanel();
		jpnlIntelNorth.setLayout(new GridBagLayout());

		jsliderRating = new JSlider(0, 20, 0);
		jsliderRating.setMajorTickSpacing(1);
		jsliderRating.setMinorTickSpacing(1);
		jsliderRating.setPaintTicks(true);
		jsliderRating.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				JSlider jslider = (JSlider) me.getSource();
				BasicSliderUI ui = (BasicSliderUI) jslider.getUI();
				int newRating = ui.valueForXPosition(me.getX());
				if (jlstFileList.getSelectedIndex() >= 0) {
					Video videoInstance = SearchSortController.getInstance()
		                    .getVideoByIndex(jlstFileList.getSelectedIndex());
					videoInstance.setPersonalRating(newRating);
					videoInstance.setChanged(true);
					controller.updateIntel(videoInstance);
				}
			}
		});

		jlabelRating = new JLabel(Integer.toString(jsliderRating.getValue()));
		jlabelRating.setHorizontalAlignment(SwingConstants.CENTER);

		ImageIcon imgIcon = null;
		URL url = Main.class.getResource("/online.png");
		if (url != null) {
			Image img = Toolkit.getDefaultToolkit().createImage(url);
			imgIcon = new ImageIcon(img);
		}
		JButton jbtnOmDbFetch = new JButton("IMDb");
		if (imgIcon != null) {
			jbtnOmDbFetch.setIcon(imgIcon);
		} else {
			jbtnOmDbFetch.setText("Fetch IMDB");
		}
		jbtnOmDbFetch.setToolTipText("Opens a search dialog so you can fetch the information that you want from IMDb");
		jbtnOmDbFetch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if (jlstFileList.getSelectedIndex() >= 0) {
					Video videoInstance = SearchSortController.getInstance()
		                    .getVideoByIndex(jlstFileList.getSelectedIndex());
					SearchSortController.getInstance().fetchVideoInformation(videoInstance);
				}
			}
		});

		JButton jbtnDeleteMovie = new JButton();
		if (imgIconDelete != null) {
			jbtnDeleteMovie.setIcon(imgIconDelete);
		} else {
			jbtnDeleteMovie.setText("Remove Movie");
		}
		jbtnDeleteMovie.setToolTipText("Deletes the currently selected movie from this program");
		jbtnDeleteMovie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if (jlstFileList.getSelectedIndex() >= 0) {
					int dialogButton = JOptionPane.showConfirmDialog(null,
		                    "Are you sure you want to remove the movie \""
		                            + SearchSortController.getInstance()
		                                    .getVideoByIndex(jlstFileList.getSelectedIndex()).getTitle()
		                            + "\" from this list?",
		                    "Warning", JOptionPane.YES_NO_OPTION);
					if (dialogButton == JOptionPane.YES_OPTION) {
						SearchSortController.getInstance().deleteMovieFromList(
		                        SearchSortController.getInstance().getVideoByIndex(jlstFileList.getSelectedIndex()));
					} else if (dialogButton == JOptionPane.NO_OPTION) {
					}
				}
			}
		});

		addComponent(0, 0, 8, 1, 0.8, 1, jpnlIntelNorth, jsliderRating, defaultInsets);
		addComponent(8, 0, 1, 1, 0.1, 1, jpnlIntelNorth, jlabelRating, defaultInsets);
		addComponent(9, 0, 1, 1, 0.1, 1, jpnlIntelNorth, jbtnOmDbFetch, defaultInsets);
		addComponent(10, 0, 1, 1, 0.1, 1, jpnlIntelNorth, jbtnDeleteMovie, defaultInsets);

		jpnlIntelNorth.setBorder(BorderFactory.createTitledBorder("Rating"));
		jlabelRating.setBorder(BorderFactory.createTitledBorder(""));

		jtaMediaInfo = new JTextArea(20, 1);
		jtaMediaInfo.setForeground(Color.DARK_GRAY);
		jtaMediaInfo.setFont(boldFont);
		jtaMediaInfo.setEditable(false);
		jtaMediaInfo.setFocusable(false);
		jtaMediaInfo.setLineWrap(true);
		jtaMediaInfo.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(jtaMediaInfo);
		jtaMediaInfo.setOpaque(false);
		jtaMediaInfo.setBackground(new Color(0, 0, 0, 0));
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane.setViewportBorder(border);
		scrollPane.setBorder(border);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		addComponent(0, 0, 1, 1, 1, 0.1, jpnlIntel, jpnlIntelNorth, defaultInsets);
		addComponent(0, 1, 1, 1, 1, 0.9, jpnlIntel, scrollPane, defaultInsets);
	}

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
		jmiPathVLC = new JMenuItem("VLC Info");
		jmiAddFile = new JMenuItem("Add File ...");
		jmiAddDirectory = new JMenuItem("Add Directory ...");
		jmiSaveAll = new JMenuItem("Save All");
		jmiResetDatabase = new JMenuItem("Reset Database");
		jmiClose = new JMenuItem("Close");
		jmFile.add(jmiPathVLC);
		jmFile.add(jmiAddFile);
		jmFile.add(jmiAddDirectory);
		jmFile.add(jmiSaveAll);
		jmFile.add(jmiResetDatabase);
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
		jmiCredits = new JMenuItem("Credits");
		jmAbout.add(jmiWebsite);
		jmAbout.add(jmiMeetTheTeam);
		jmAbout.add(jmiWatchCode);
		jmAbout.add(jmiJournals);
		jmAbout.add(jmiCredits);

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

		jmiCredits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(rootPane, "Icons made by Google from www.flaticon.com", "Credits",
		                JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Adding AL to the close button
		jmiClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO ?
		        // Right now on exit we're default saving the database's
		        // changes.
		        // Might could open a window and ask for saving changes
		        // Would need a "changed" boolean
				controller.getVLC().stopMedia();
				dataController.saveAll(SearchSortController.getInstance().getMovies());
				dispose();
				System.exit(0);
			}
		});
		
		jmiTutorial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String tutorial = "<html>This is a tutorial for using VIP: The Video Information Program.<br><p>1. You can add your own video files or even whole directories<br>by using the 'Add File' and 'Add Directory' MenuItems,<br>that can be found in the menuBar</p><p>2. You can play a movie from the list by performing a double click<br>on the movie in the list, or selecting the movie and press play<br>under the video panel.</p><p>3. You can fetch information to every single video in your list.<br>Just select the video you want to add information to and press<br>the fetch button. Now you can enter a search key to simply<br>search in the imdb. Next you have to select one of the shown results,<br>and all information will be added automatically.</p><p>4. You can close your program everytime you want. After you<br>re-open it again, the program will restore all the movies in the list.</p><br><br>Have fun and enjoy the program!</html>";
				JOptionPane.showMessageDialog(null, tutorial, "Quick Tutorial",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		jmiAddFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path = getFilePath(new FileNameExtensionFilter("Video Files", movieExtensions));
				SearchSortController.getInstance().addMovieToList(new Video(path));
				SearchSortController.getInstance().updateList(SearchSortController.getInstance().getMovies());
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
				dataController.saveAll(SearchSortController.getInstance().getMovies());
			}
		});

		jmiResetDatabase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					File databaseFile = new File("test.db");
					if (databaseFile.exists() && !databaseFile.isDirectory()) {
						System.gc();
						if (databaseFile.delete()) {
							JOptionPane.showMessageDialog(rootPane, "The database has been resetted.",
		                            "Database deleted.", JOptionPane.INFORMATION_MESSAGE);
							dataController.createDatabase();
						}
					} else {
						JOptionPane.showMessageDialog(rootPane,
		                        "An error occured during database deletion. Please try again",
		                        "Database Deletion Error", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Add ActionListener to the FileList, so a movie will be played if
	 * double-clicked
	 */
	private void addFileListActionListener() {
		jlstFileList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				if (jlstFileList.getSelectedIndex() >= 0) {
					Video videoInstance = SearchSortController.getInstance()
		                    .getVideoByIndex(jlstFileList.getSelectedIndex());
					controller.updateIntel(videoInstance);
					if (ev.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(ev)) {
						controller.getVLC().switchMediaFile(videoInstance);
					}
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
			SearchSortController.getInstance().addMovieToList(new Video(fileList.get(i).getAbsolutePath()));
		}

		SearchSortController.getInstance().updateList(SearchSortController.getInstance().getMovies());
	}

	/**
	 * Getter method for the intel Textarea. This Textarea displays the
	 * currently selected video object's information.
	 * 
	 * @return the Textarea object
	 */
	public JTextArea getIntelTextArea() {
		return jtaMediaInfo;
	}

	/**
	 * Not Needed
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	/**
	 * Not Needed
	 */
	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	/**
	 * Timer that is reset every time, a resized event is fired. Thus preventing
	 * the media player from recreating on every single event, but once at the
	 * end of a resizing occurance. Just updates the current panel used as
	 * canvas.
	 */
	private Timer resizeTimer = new Timer(RESIZE_REFRESH_RATE, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			controller.getVLC().switchSurface(mainMoviePanel, true);
			resizeTimer.stop();
		}
	});

	/**
	 * Is called everytime the main frame is resized and starts a timer to
	 * update the canvas panel.
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		resizeTimer.restart();
	}

	/**
	 * Not Needed
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	/**
	 * Getter method for the main frames movie panel, that is used as canvas.
	 * 
	 * @return the panel used as canvas
	 */
	public MoviePanel getMoviePanel() {
		return mainMoviePanel;
	}

	/**
	 * Outsourced method for detecting whether a root folder is already
	 * determined or has to be declared by the user
	 */
	private void selectRootFolder() {
		JOptionPane.showMessageDialog(this, "You have to select a root folder for your video collection!");
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
	 * Helping method for showing the Frame in the middle of every screen.
	 */
	private void setScreenLocation() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] allDevices = env.getScreenDevices();
		int width = (int) allDevices[0].getDefaultConfiguration().getBounds().width;
		int height = (int) allDevices[0].getDefaultConfiguration().getBounds().height;
		System.out.println(height + " " + width);
		this.setLocation(((width/2) - (this.getWidth()/2)), ((height/2) - (this.getHeight()/2)));
	}
}
