package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.vip.controllers.OMDBController;
import com.vip.extractor.MediaSearchResult;

@SuppressWarnings("serial")
public class SingleVideoPanel extends JPanel{
	private JLabel picturePanel;
	
	private JTextPane informationPanel;
	
	private BufferedImage image;
	
	public SingleVideoPanel(MediaSearchResult temp) {
		URL imageUrl;
		try {
			imageUrl = new URL(OMDBController.getInstance().getMediaResultPoster(temp));
			image = ImageIO.read(imageUrl);
			this.setLayout(new BorderLayout());
			picturePanel = new JLabel(new ImageIcon(image));
			
			informationPanel = new JTextPane();
			informationPanel.setText(("Title of the Video: " + temp.getTitle() + "\n Year of Release: " + temp.getYear()));
			
			this.add(picturePanel);
			this.add(informationPanel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
