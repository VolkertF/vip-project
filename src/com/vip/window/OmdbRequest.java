package com.vip.window;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.vip.extractor.MediaSearchResult;
import com.vip.extractor.SearchResult;

@SuppressWarnings("serial")
public class OmdbRequest extends JFrame {
	private ArrayList<MediaSearchResult> searchResult;
	
	public OmdbRequest(ArrayList<SearchResult> results) {
		super("Search-Results");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.searchResult = switchArrayList(results);
		this.setPreferredSize(new Dimension(600, 400));
		buildPanels();
		this.pack();
	}
	
	private void buildPanels() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		for(MediaSearchResult temp : searchResult) {
			try {
				panel.add(new SingleVideoPanel(temp));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		this.getContentPane().add(panel);
	}
	
	private ArrayList<MediaSearchResult> switchArrayList(ArrayList<SearchResult> searchResults) {
		ArrayList<MediaSearchResult> mediaSearchResult = new ArrayList<MediaSearchResult>();
		for(SearchResult temp : searchResults) {
			mediaSearchResult.add((MediaSearchResult) temp);
		}
		return mediaSearchResult;
	}
}
