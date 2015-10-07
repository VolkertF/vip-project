package com.vip.window;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import com.vip.media.VLC;

/**
 * The Key_parser translates user keyboard input into reasonable commands.
 * 
 * @author Fabian Volkert
 *
 */
public class Key_parser implements KeyEventDispatcher {

	private VipFrame vip_frame;

	public void set_vip_frame(VipFrame new_vip_frame) {
		vip_frame = new_vip_frame;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent ke) {
		int state = ke.getID();
		int pressed = KeyEvent.KEY_PRESSED;
		int id = ke.getKeyCode();
		// When space is pressed and no textfield is focussed, movie playback
		// will be toggled
		if (state == KeyEvent.KEY_PRESSED && id == KeyEvent.VK_SPACE && !(ke.getSource() instanceof JTextField))
			VLC.toggle_movie_playback();
		if (state == KeyEvent.KEY_PRESSED && (id == KeyEvent.VK_PLUS || id == KeyEvent.VK_ADD))
			VLC.volume_up();
		if (state == KeyEvent.KEY_PRESSED && (id == KeyEvent.VK_MINUS || id == KeyEvent.VK_SUBTRACT))
			VLC.volume_down();
		// When CTRL+F is pressed, the search textfield will be focussed
		if (state == pressed && id == KeyEvent.VK_F && (ke.getModifiers() & KeyEvent.CTRL_MASK) != 0)
			vip_frame.get_jtfSearch().requestFocus();

		return false;
	}

}
