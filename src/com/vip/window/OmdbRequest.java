package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.vip.extractor.MediaSearchResult;
import com.vip.extractor.SearchResult;

@SuppressWarnings("serial")
public class OmdbRequest extends JFrame {
	private ArrayList<MediaSearchResult> searchResult;
	public ArrayList<String> imageUrls;
	private ArrayList<ImageIcon> icons = new ArrayList<ImageIcon>();
	@SuppressWarnings("rawtypes")
	private DefaultListModel iconListModel = new DefaultListModel();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JList resultList = new JList(iconListModel);
	
	@SuppressWarnings("unchecked")
	public OmdbRequest(ArrayList<SearchResult> searchResults) {
		this.searchResult = switchArrayList(searchResults);
		this.imageUrls = fillArrayListFromArrayList(this.searchResult);
		
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(resultList), BorderLayout.LINE_START);
		
		this.add(new JPanel(), BorderLayout.CENTER);
		for(String imageURL : imageUrls) {
			BufferedImage img;
			try {
				img = ImageIO.read(new URL(imageURL));
				img = ImageUtil.createScaledImage(img);
				ImageIcon icon = new ImageIcon(img, imageURL);
				icons.add(icon);
				iconListModel.addElement(icon);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setPreferredSize(new Dimension(400, 500));
		this.pack();
	}

	private ArrayList<String> fillArrayListFromArrayList(ArrayList<MediaSearchResult> searchResult) {
		ArrayList<String> urlList = new ArrayList<String>();
		for(MediaSearchResult temp : searchResult) {
			urlList.add(temp.getPoster());
		}
		return urlList;
	}
	
	private ArrayList<MediaSearchResult> switchArrayList(ArrayList<SearchResult> searchResults) {
		ArrayList<MediaSearchResult> mediaSearchResult = new ArrayList<MediaSearchResult>();
		for(SearchResult temp : searchResults) {
			mediaSearchResult.add((MediaSearchResult) temp);
		}
		return mediaSearchResult;
	}
}
