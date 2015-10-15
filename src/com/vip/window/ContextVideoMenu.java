package com.vip.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.vip.attributes.Video;
import com.vip.media.VLC;

@SuppressWarnings("serial")
public class ContextVideoMenu extends JPopupMenu {
	private JMenuItem jmiPlay;
	private JMenuItem jmiInfo;
	
	public ContextVideoMenu(Video vid) {
		init(vid);
		addMenuItems();
	}
	
	private void init(final Video vid) {
		final String vidPath = vid.getPath();
		
		jmiPlay = new JMenuItem("Play " + vid.getTitle());
		jmiPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				VLC.loadMedia(vidPath);
				VLC.toggleMoviePlayback();
				vid.deactivateContextVideoMenu();
			}
		});
		
		
		jmiInfo = new JMenuItem("Get Video Information");
		final String info = "You have selected information about " + vid.getTitle();
		jmiInfo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vid.deactivateContextVideoMenu();
				JOptionPane.showMessageDialog(getRootPane(), info);
			}
		});
	}
	
	private void addMenuItems() {
		this.add(jmiPlay);
		this.addSeparator();
		this.add(jmiInfo);
	}
}
