package com.vip.window;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.vip.attributes.Video;

@SuppressWarnings("serial")
public class ContextVideoMenu extends JPopupMenu {
	public ContextVideoMenu(Video vid) {
		this.add(new JMenuItem("Play " + vid.getTitle()));
	}
}
