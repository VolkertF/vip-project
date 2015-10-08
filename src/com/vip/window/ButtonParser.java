package com.vip.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.vip.media.VLC;

public class ButtonParser implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		if (("jB_toggle_movie_playback".equals(action)))
			VLC.toggleMoviePlayback();
		if (("jB_stop_movie".equals(action)))
			VLC.stopMovie();
		if (("jB_previous_chapter".equals(action)))
			VLC.previousChapter();
		if (("jB_next_chapter".equals(action)))
			VLC.nextChapter();
		if (("jB_volume_down".equals(action)))
			VLC.volumeDown();
		if (("jB_volume_up".equals(action)))
			VLC.volumeUp();
	}
}
