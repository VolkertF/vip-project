package com.vip.window;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.vip.Controller;
import com.vip.attributes.Video;

@SuppressWarnings("serial")
public class ContextVideoMenu extends JPopupMenu {
	private JMenuItem jmiPlay;
	private JMenuItem jmiInfo;
	//private Controller controller;

	public ContextVideoMenu(Video vid, Controller controllerInstance) {
		//controller = controllerInstance;
		addMenuItems();
	}

	private void addMenuItems() {
		this.add(jmiPlay);
		this.addSeparator();
		this.add(jmiInfo);
	}
}
