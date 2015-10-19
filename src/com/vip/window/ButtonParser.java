package com.vip.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.vip.media.VLC;

/**
 * Class is responsible for ActionCommand parsing and delegating to the specific
 * task.
 */
public class ButtonParser implements ActionListener {

	private VipFrame vipFrame;

	public ButtonParser(VipFrame newVipFrame) {
		vipFrame = newVipFrame;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		if ("jbtnToggleMoviePlayback".equals(action))
			VLC.toggleMoviePlayback();
		if ("jbtnStopMovie".equals(action))
			VLC.stopMedia();
		if ("jbtnPreviousMovie".equals(action)) {
			// TODO exchange hardcoded video file with list reference
			 VLC.switchMediaFile("F:\\The Saga Of Bjorn-HD.mp4");
			vipFrame.initMovie();
		}
		if ("jbtnNextMovie".equals(action)) {
			// TODO exchange hardcoded video file with list reference
			 VLC.switchMediaFile("F:\\Dji. Death Sails-HD.mp4");
			vipFrame.initMovie();
		}
		if ("jbtnPreviousChapter".equals(action))
			VLC.previousChapter();
		if ("jbtnNextChapter".equals(action))
			VLC.nextChapter();
		if ("jbtnJumpBack".equals(action))
			VLC.jumpBack();
		if ("jbtnJumpForward".equals(action))
			VLC.jumpForward();
		if ("jbtnVolumeDown".equals(action))
			VLC.volumeDown();
		if ("jbtnVolumeUp".equals(action))
			VLC.volumeUp();
	}
}
