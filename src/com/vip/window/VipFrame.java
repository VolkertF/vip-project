package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
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
		pack();
		
	}

	private JPanel jpnlExplorer;
	private JPanel jpnlMovie;
	private JPanel jpnlIntel;
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
		
		jpnlMovie = new JPanel();
		jpnlMovie.setLayout(new BorderLayout());
		jpnlMovie.setBorder(BorderFactory.createTitledBorder("Movie"));
		
		jpnlIntel = new JPanel();
		jpnlIntel.setLayout(new BorderLayout());
		jpnlIntel.setBorder(BorderFactory.createTitledBorder("Intel"));

		getContentPane().setLayout(new GridBagLayout());
		
		addComponent(0, 0, 1, 2, 0.0, 0.0, getContentPane(), jpnlExplorer, defaultInsets);
		addComponent(1, 0, 1, 1, 1.0, 1.0, getContentPane(), jpnlMovie, defaultInsets);
		addComponent(1, 1, 1, 1, 0.0, 0.0, getContentPane(), jpnlIntel, defaultInsets);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
