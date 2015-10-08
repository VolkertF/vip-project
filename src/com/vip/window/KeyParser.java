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
public class KeyParser implements KeyEventDispatcher {

	private VipFrame vipFrame;

	public void setVipFrame(VipFrame newVipFrame) {
		vipFrame = newVipFrame;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent ke) {
		int state = ke.getID();
		int pressed = KeyEvent.KEY_PRESSED;
		int id = ke.getKeyCode();
		// When no textfield is focused, input will be processed
		if (!(ke.getSource() instanceof JTextField)) {
			if (state == KeyEvent.KEY_PRESSED && id == KeyEvent.VK_SPACE)
				VLC.toggleMoviePlayback();
			if (state == KeyEvent.KEY_PRESSED && (id == KeyEvent.VK_PLUS || id == KeyEvent.VK_ADD)) {
				vipFrame.updateVolume(VLC.getIncreasedVolume());
			}
			if (state == KeyEvent.KEY_PRESSED && (id == KeyEvent.VK_MINUS || id == KeyEvent.VK_SUBTRACT)) {
				vipFrame.updateVolume(VLC.getDecreasedVolume());
			}
			// When CTRL+F is pressed, the search textfield will be focussed
			if (state == pressed && id == KeyEvent.VK_F && (ke.getModifiers() & KeyEvent.CTRL_MASK) != 0)
				vipFrame.get_jtfSearch().requestFocus();
		}
		return false;
	}

}
