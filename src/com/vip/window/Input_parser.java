package com.vip.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import com.vip.media.VLC;

@SuppressWarnings("serial")
public class Input_parser extends AbstractAction implements ActionListener {

	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		if (("jB_toggle_movie_playback".equals(action)))
			VLC.toggle_movie_playback();
		if (("jB_stop_movie".equals(action)))
			VLC.stop_movie();
		if (("jB_previous_chapter".equals(action)))
			VLC.previous_chapter();
		if (("jB_next_chapter".equals(action)))
			VLC.next_chapter();
		if (("jB_volume_down".equals(action)))
			VLC.volume_down();
		if (("jB_volume_up".equals(action)))
			VLC.volume_up();
		if("Space_pressed".equals(action))
			System.out.println("Space pressed");
	}

}
