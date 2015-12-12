package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vip.controllers.OMDBController;
import com.vip.extractor.MediaSearchResult;

@SuppressWarnings("serial")
public class SingleVideoPanel extends JPanel{
	private JLabel picturePanel;
	
	private BufferedImage image;
	
	public SingleVideoPanel(MediaSearchResult temp) {
		URL imageUrl;
		try {
			imageUrl = new URL(OMDBController.getInstance().getMediaResultPoster(temp));
			image = ImageIO.read(imageUrl);
			Image toolkitImage = image.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
			this.setLayout(new BorderLayout());
			String info = "Title of the Video: " + temp.getTitle() + "\n Year of Release: " + temp.getYear();
			picturePanel = new JLabel(info, new ImageIcon(toolkitImage), JLabel.CENTER);
			this.add(picturePanel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
