package com.vip.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import com.vip.attributes.Video;
import com.vip.controllers.Controller;
import com.vip.media.VLC;

/**
 * Class is responsible for ActionCommand parsing and delegating to the specific
 * task.
 */
public class ButtonParser implements ActionListener {

	private Controller controller;

	/**
	 * 
	 */
	public ButtonParser(Controller newController) {
		controller = newController;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		VLC vlc = controller.getVLC();
		JList<Video> jlstFileList = controller.getFrame().getFileList();
		String action = ae.getActionCommand();
		if ("jbtnToggleMoviePlayback".equals(action))
			vlc.toggleMediaPlayback();
		if ("jbtnStopMovie".equals(action))
			vlc.stopMedia();
		if ("jbtnPreviousMovie".equals(action)) {
			controller.setToPreviousListItem();
		}
		if ("jbtnNextMovie".equals(action)) {
			controller.setToNextListItem();
		}
		if ("jbtnPreviousChapter".equals(action))
			vlc.previousChapter();
		if ("jbtnNextChapter".equals(action))
			vlc.nextChapter();
		if ("jbtnJumpBack".equals(action))
			vlc.jumpBack();
		if ("jbtnJumpForward".equals(action))
			vlc.jumpForward();
		if ("jbtnVolume".equals(action)) {
			if (vlc.isMuted()) {
				vlc.toggleMuted();
				vlc.getMediaPlayer().setVolume(vlc.getlastVolume());
			} else {
				vlc.toggleMuted();
				vlc.getMediaPlayer().setVolume(0);
			}
		}
		if ("jbtnFullscreen".equals(action))
			controller.toggleFullscreen();
	}
}
