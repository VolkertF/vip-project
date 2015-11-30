package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class FullscreenDialog extends JDialog {

	private Canvas canvas = new Canvas();

	public FullscreenDialog(VipFrame parentFrame) {
		super(parentFrame, true);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.requestFocus();
		this.setUndecorated(true);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(parentFrame.getController().getKeyParser());

		JPanel contentPanel = new JPanel();
		this.add(contentPanel, BorderLayout.CENTER);

		canvas.setBackground(Color.BLACK);
		contentPanel.add(canvas, BorderLayout.CENTER);

		this.setVisible(true);
	}
}
