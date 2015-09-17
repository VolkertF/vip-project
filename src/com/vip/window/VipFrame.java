package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class VipFrame extends JFrame {
	/**
	 * Constructor for building the frame and initialize all event handlers.
	 */
	public VipFrame() {
		super("VipFrame");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		defaultInsets = new Insets(2, 2, 2, 2);
		
		buildPanels();
		buildExplorerGUI();
		buildMovieGUI();
		buildIntelGUI();
		pack();
	}

	/**
	 * The JPanel that represents the Explorer and do file-searching stuff and listing all the videos
	 * in a JList, so the user is able to select one and play it
	 */
	private JPanel jpnlExplorer;
	
	/**
	 * The JPanel that represents the movie been played by the VLC plugin
	 */
	private JPanel jpnlMovie;
	
	/**
	 * The JPanel that represents the information section of the program, where intel about the currently selected
	 * movie can be clicked on so the program do a search for this one, and various information about the title can be viewed.
	 */
	private JPanel jpnlIntel;
	
	/**
	 * Standard insets for creating the GridBagLayout
	 */
	private Insets defaultInsets;

	/**
	 * Helping routine for creating  components and adding them to a GridBagLayout
	 * The parameters are constraints when they are added
	 * @param x x-position
	 * @param y y-position
	 * @param width Width of the cell
	 * @param height Height of the cell
	 * @param weightx Weight vertically
	 * @param weighty Weight horizontally
	 * @param cont Container
	 * @param comp Component that will be added
	 * @param insets Distances round the component that is added
	 */
	private static void addComponent(int x, int y, int width, int height, double weightx, double weighty, Container cont, Component comp, Insets insets) {
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
	 * Create Subpanels
	 * Have to be called before any other building-methods
	 */
	private void buildPanels() {
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

		getContentPane().setLayout(new GridBagLayout());
		
		addComponent(0, 0, 1, 2, 0.0, 0.0, getContentPane(), jpnlExplorer, defaultInsets);
		addComponent(1, 0, 1, 1, 1.0, 1.0, getContentPane(), jpnlMovie, defaultInsets);
		addComponent(1, 1, 1, 1, 0.0, 0.0, getContentPane(), jpnlIntel, defaultInsets);
	}
	
	/**
	 * This JList will fill with the files in all searching directories the program
	 * overwatches, so basically every movie file found on the harddrive
	 */
	private JList<String> jlstFileList;
	
	/**
	 * This JTextField is for entering your private search stuff into a textField to search your library for this
	 */
	private JTextField jtfSearch;
	
	/**
	 * This JComboBox is ment for sorting your search by different categories
	 */
	private JComboBox<String> jcbSearchCategories;
	
	/**
	 * By pressing this JButton the search by the chosen searching-categories and the
	 * entered keywords is executed.
	 */
	private JButton jbtnSearchExecute;			//Button for executing the search
	
	/**
	 * Create Sub-sub-panels in the explorer panel
	 */
	private void buildExplorerGUI() {
		String[] fileList = {};
		//Fill the filelist String-Array
		jlstFileList = new JList<String>(fileList);
		jlstFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlstFileList.setSelectedIndex(0);
		
		jtfSearch = new JTextField(20);
		
		String[] searchCategories = {"By Length, increasing",
									 "By length, decreasing",
									 "By rating, increasing",
									 "By rating, decreasing",};
		jcbSearchCategories = new JComboBox<String>(searchCategories);
		jcbSearchCategories.setEditable(false);
		jcbSearchCategories.setSelectedIndex(0);
		JScrollPane jspSearchCategories = new JScrollPane(jcbSearchCategories);
		jspSearchCategories.setPreferredSize(jcbSearchCategories.getSize());
		
		jbtnSearchExecute = new JButton("Search");
		
		//			 x  y  w  h  wx   wy   cont          comp            insets
		addComponent(0, 0, 2, 1, 0.0, 0.0, jpnlExplorer, jtfSearch, defaultInsets);
		addComponent(0, 1, 1, 1, 0.0, 0.0, jpnlExplorer, jcbSearchCategories, defaultInsets);
		addComponent(1, 1, 1, 1, 0.0, 0.0, jpnlExplorer, jbtnSearchExecute, defaultInsets);
		addComponent(0, 2, 2, 1, 1.0, 1.0, jpnlExplorer, jlstFileList, defaultInsets);
	}
	
	/**
	 * Create Sub-sub- components in the movie panel including the JVLC plugin to play movies
	 * Maybe also a section to control the movie (play, pause, volume up/down, fast forward etc.)
	 */
	private void buildMovieGUI() {
		//Here the VLC Plugin comes to life
	}
	
	/**
	 * Create Sub-sub-components in the intel panel for showing information on the selected video
	 * file in the explorer section
	 */
	private void buildIntelGUI() {
		//Here some information about the currently selected video file will be stored.
	}
	
	
	
	
	
	
	
}
