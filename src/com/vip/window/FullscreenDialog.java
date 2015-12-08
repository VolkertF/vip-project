package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.vip.input.KeyParser;
import com.vip.media.VLC;

public class FullscreenDialog extends JDialog{

	private KeyParser parser;

	private VipFrame parentFrame;

	private MoviePanel jpnlVideoSurface;

	private VLC vlcInstance;

	public FullscreenDialog(VipFrame parentFrame, VLC vlcInstance, KeyParser parser, MoviePanel jpnlMovie) {
		super(parentFrame, true);
		this.parentFrame = parentFrame;
		this.parser = parser;
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setUndecorated(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.vlcInstance = vlcInstance;
		this.jpnlVideoSurface = jpnlMovie;
		vlcInstance.setSurface(jpnlVideoSurface);
		this.add(jpnlVideoSurface, BorderLayout.CENTER);

		this.setModalityType(Dialog.ModalityType.MODELESS);
		this.setVisible(true);
		jpnlVideoSurface.updateVideoSurface();
		this.requestFocus();
	}
	
	public MoviePanel getSurface() {
		return jpnlVideoSurface;
	}

	public VLC getVLC() {
		return vlcInstance;
	}
}
