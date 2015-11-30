package com.vip.window;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vip.controllers.OMDBController;
import com.vip.extractor.MediaSearchResult;
import com.vip.extractor.SearchResult;

@SuppressWarnings("serial")
public class OmdbRequest extends JFrame {
	private ArrayList<MediaSearchResult> searchResult;
	
	public OmdbRequest(ArrayList<SearchResult> results) {
		super("Search-Results");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		buildPanels();
		this.searchResult = switchArrayList(results);
	}
	
	private void buildPanels() {
		JPanel panel = new JPanel(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
		for(MediaSearchResult temp : searchResult) {
			try {
				BufferedImage image = ImageIO.read(new URL(OMDBController.getInstance().getMediaResultPoster(temp)));
				JPanel singleVideoPanel = new JPanel(new BorderLayout());
				JLabel pictureLabel = new JLabel(new ImageIcon(image));
				panel.add(singleVideoPanel);
				singleVideoPanel.add(pictureLabel);
				JLabel informationLabel = new JLabel();
				informationLabel.setText(("Title of the Video: " + temp.getTitle() + "\nYear of Release: " + temp.getYear()));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<MediaSearchResult> switchArrayList(ArrayList<SearchResult> searchResults) {
		ArrayList<MediaSearchResult> mediaSearchResult = new ArrayList<MediaSearchResult>();
		for(SearchResult temp : searchResults) {
			mediaSearchResult.add((MediaSearchResult) temp);
		}
		return mediaSearchResult;
	}
}
