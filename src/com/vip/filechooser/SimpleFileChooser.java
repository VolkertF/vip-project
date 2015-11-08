package com.vip.filechooser;

// SimpleFileChooser.java
// A simple file chooser to see what it takes to make one of these work.

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class SimpleFileChooser extends JFrame {

	/**
	 * Simple File chooser that is initialized with an Integer value for
	 * choosing a selection mode, and a pre-defined filter for selecting special
	 * values of files.
	 * 
	 * @param selectionType
	 *            Choosing the FileSelectionMode, 1-Files Only, 2-Directories
	 *            only, all others-Files and directories
	 * @param fileFilter
	 *            Adding a FileNameExtensionFilter
	 */
	public SimpleFileChooser(int selectionType,
			FileNameExtensionFilter fileFilter) {
		super("File Chooser Frame");
		setSize(750, 550);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(new FlowLayout());

		final JLabel statusbar = new JLabel(
				"Output of your selection will go here");

		JPanel filePanel = new JPanel();
		JPanel labelPanel = new JPanel();

		JButton selectButton = new JButton("Select");
		JButton unselectButton = new JButton("Unselect");

		final JFileChooser chooser = new JFileChooser();
		chooser.setControlButtonsAreShown(false);
		chooser.setFileFilter(fileFilter);
		if (selectionType != 1 && selectionType != 2) {
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		} else if (selectionType == 1) {
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		} else if (selectionType == 2) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				statusbar.setText("You saved "
						+ ((chooser.getSelectedFile() != null) ? chooser
								.getSelectedFile().getName() : "nothing"));
			}
		});

		unselectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				statusbar.setText("You have unselected all options");
			}
		});

		c.add(labelPanel);
		labelPanel.add(statusbar);
		labelPanel.add(selectButton);
		labelPanel.add(unselectButton);
		c.add(filePanel);
		filePanel.add(chooser);
	}
}
