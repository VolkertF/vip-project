package com.vip.window;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.vip.attributes.Video;
import com.vip.controllers.Controller;

/**
 * This was ought to be implemented, but due
 * to time problems there is no context menu
 * in the final project.
 * 
 * *flies away*
 * @author Johannes
 *
 */
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
