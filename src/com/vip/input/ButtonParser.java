package com.vip.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.vip.attributes.Video;
import com.vip.media.VLC;
import com.vip.window.VipFrame;

/**
 * Class is responsible for ActionCommand parsing and delegating to the specific
 * task.
 */
public class ButtonParser implements ActionListener {

	private VipFrame vipFrame;
	private VLC vlc;

	/**
	 * 
	 */
	public ButtonParser(VLC vlcInstance, VipFrame vipFrameInstance) {
		vlc = vlcInstance;
		vipFrame = vipFrameInstance;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		if ("jbtnToggleMoviePlayback".equals(action))
			vlc.toggleMediaPlayback();
		if ("jbtnStopMovie".equals(action))
			vlc.stopMedia();
		if ("jbtnPreviousMovie".equals(action)) {
			int oldIndex = vipFrame.getFileList().getSelectedIndex();
			int newIndex = oldIndex - 1;
			// If reached pre-beginning of list
			if (newIndex < 0) {
				newIndex = (vipFrame.getFileList().getModel().getSize() - 1);
				vipFrame.getFileList().setSelectedIndex(newIndex);
			}
			vipFrame.getFileList().setSelectedIndex(newIndex);
			Video videoInstance = com.vip.controllers.SearchSortController.getInstance().getVideoByIndex(newIndex);
			vipFrame.updateIntel(videoInstance);
			vlc.switchMediaFile(videoInstance.getFilePath());
		}
		if ("jbtnNextMovie".equals(action)) {
			int oldIndex = vipFrame.getFileList().getSelectedIndex();
			int newIndex = oldIndex + 1;
			// If reached end of list
			if (newIndex >= (vipFrame.getFileList().getModel().getSize())) {
				newIndex = 0;
			}
			vipFrame.getFileList().setSelectedIndex(newIndex);
			Video videoInstance = com.vip.controllers.SearchSortController.getInstance().getVideoByIndex(newIndex);
			vipFrame.updateIntel(videoInstance);
			vlc.switchMediaFile(videoInstance.getFilePath());
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
			vipFrame.getController().toggleFullscreen();
	}
}
