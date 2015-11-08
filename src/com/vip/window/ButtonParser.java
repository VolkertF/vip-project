package com.vip.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.vip.media.VLC;

/**
 * Class is responsible for ActionCommand parsing and delegating to the specific
 * task.
 */
public class ButtonParser implements ActionListener {

	private VLC vlc;

	/**
	 * 
	 */
	public ButtonParser(VLC vlcInstance) {
		vlc = vlcInstance;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		if ("jbtnToggleMoviePlayback".equals(action))
			vlc.toggleMediaPlayback();
		if ("jbtnStopMovie".equals(action))
			vlc.stopMedia();
		if ("jbtnPreviousMovie".equals(action)) {
			// TODO exchange hardcoded video file with list reference
			vlc.switchMediaFile("F:\\The Saga Of Bjorn-HD.mp4");
		}
		if ("jbtnNextMovie".equals(action)) {
			// TODO exchange hardcoded video file with list reference
			vlc.switchMediaFile("F:\\Dji. Death Sails-HD.mp4");
		}
		if ("jbtnPreviousChapter".equals(action))
			vlc.previousChapter();
		if ("jbtnNextChapter".equals(action))
			vlc.nextChapter();
		if ("jbtnJumpBack".equals(action))
			vlc.jumpBack();
		if ("jbtnJumpForward".equals(action))
			vlc.jumpForward();
		if ("jbtnVolumeDown".equals(action))
			vlc.volumeDown();
		if ("jbtnVolumeUp".equals(action))
			vlc.volumeUp();
	}
}
