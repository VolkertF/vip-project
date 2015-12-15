package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.vip.controllers.Controller;
import com.vip.media.VLC;

@SuppressWarnings("serial")
public class FullscreenDialog extends JDialog implements MouseMotionListener, MouseListener {

	private MoviePanel jpnlVideoSurface;

	private VLC vlcInstance;

	private Controller controller;

	public FullscreenDialog(VipFrame parentFrame, VLC vlcInstance, Controller newController) {
		super(parentFrame, true);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setUndecorated(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.vlcInstance = vlcInstance;
		controller = newController;
		this.jpnlVideoSurface = new MoviePanel(vlcInstance);
		this.add(jpnlVideoSurface, BorderLayout.CENTER);
		this.setModalityType(Dialog.ModalityType.MODELESS);
		jpnlVideoSurface.setDrawOverlay(true);
		this.setVisible(true);
		requestFocus();
		this.vlcInstance.switchSurface(jpnlVideoSurface, true);
	}

	public MoviePanel getSurface() {
		return jpnlVideoSurface;
	}

	public VLC getVLC() {
		return vlcInstance;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		jpnlVideoSurface.setDrawOverlay(true);
		jpnlVideoSurface.setDisplayStates(true, true, false, true);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		controller.toggleFullscreen();
	}
}
