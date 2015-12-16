package com.vip.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.vip.Main;
import com.vip.attributes.Video;
import com.vip.controllers.Controller;
import com.vip.controllers.OMDBController;
import com.vip.controllers.SearchSortController;
import com.vip.extractor.MediaSearchResult;
import com.vip.extractor.SearchResult;

/**
 * This class creates a window from an OMDb-Request, if the user presses the
 * button for fetching information on a single movie in the list. This window
 * displays a choice of movies, which then are able to be selected by the user.
 * 
 * @author Johannes Licht
 */
@SuppressWarnings("serial")
public class OmdbRequest extends JFrame {
	/**
	 * ArrayList containing searchResults from the OMDb-Request
	 */
	private ArrayList<MediaSearchResult> searchResult;

	/**
	 * An ArrayList containing the URLs of the images, that should be displayed.
	 */
	private ArrayList<String> imageUrls;

	/**
	 * An ArrayList of the actual Icons that are displayed by using a JList with
	 * a DefaultListModel.
	 */
	private ArrayList<ImageIcon> icons = new ArrayList<ImageIcon>();

	/**
	 * The DefaultListModel for keeping the JList working
	 */
	@SuppressWarnings("rawtypes")
	private DefaultListModel iconListModel = new DefaultListModel();

	/**
	 * The Actual JList containing all the Icons to be displayed.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JList resultList = new JList(iconListModel);

	/**
	 * A Button to confirm, that the movie that is selected, should be fetched
	 * to the Video object.
	 */
	private JButton buttonFetch = new JButton();

	/**
	 * A Button for leaving this view and going back to the main frame.
	 */
	private JButton buttonCancel = new JButton();

	/** Instance of the application's controller **/
	private Controller controller;

	/**
	 * Constructor for OMDb-Request window
	 * 
	 * @param searchResults
	 *            An ArrayList of SearchResults from the OMDb-Request.
	 * @param vid
	 *            The video that information should be fetched to.
	 */
	@SuppressWarnings("unchecked")
	public OmdbRequest(ArrayList<SearchResult> searchResults, Video vid, Controller newController) {
		final Video video = vid;
		this.searchResult = switchArrayList(searchResults);
		this.imageUrls = fillArrayListFromArrayList(this.searchResult);
		this.controller = newController;

		URL url = Main.class.getResource("/icon.png");
		Image iconImage = Toolkit.getDefaultToolkit().createImage(url);
		this.setIconImage(iconImage);
		// TODO: redesigning Layout to show the Fetch button correctly
		this.setTitle("IMDB Information Fetcher");
		this.setLocation(750, 200);
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(resultList), BorderLayout.LINE_END);

		this.add(new JPanel(), BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(buttonFetch, BorderLayout.CENTER);
		buttonFetch.setText("Fetch Information");
		buttonFetch.setToolTipText("Press this button if you have selected the right movie you wanted to search for");
		buttonFetch.setVisible(true);
		buttonPanel.add(buttonCancel, BorderLayout.LINE_END);
		buttonCancel.setVisible(true);
		buttonCancel.setText("Cancel");
		buttonCancel.setToolTipText("Press this button if you want to leave this screen and return to the main frame.");

		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		buttonFetch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] temp = ((ImageIcon) resultList.getSelectedValue()).getDescription().split("%%%%");
				String[] tempTemp = temp[3].split("\"");
				Map<String, String> infoMap = OMDBController.getInstance().getById(tempTemp[1]);
				// Updates the video's data and repaints the intel-panel and
	            // the list
				SearchSortController.getInstance().assignMapToVideo(infoMap, video);
				video.setChanged(true);
				controller.updateIntel(video);
				controller.getFrame().getFileList().repaint();
				dispose();
			}
		});

		resultList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(arg0)) {
					String[] temp = ((ImageIcon) resultList.getSelectedValue()).getDescription().split("%%%%");
					String[] tempTemp = temp[3].split("\"");
					Map<String, String> infoMap = OMDBController.getInstance().getById(tempTemp[1]);
					// Updates the video's data and repaints the intel-panel and
		            // the list
					SearchSortController.getInstance().assignMapToVideo(infoMap, video);
					video.setChanged(true);
					controller.updateIntel(video);
					controller.getFrame().getFileList().repaint();
					dispose();
				}
			}
		});
		for (String imageURL : imageUrls) {
			BufferedImage img;
			try {
				String[] splittedImageURL = imageURL.split("%%%%");
				String poster = splittedImageURL[0];
				img = ImageIO.read(new URL(poster));
				img = ImageUtil.createScaledImage(img);
				ImageIcon icon = new ImageIcon(img, poster);
				icon.setDescription(imageURL);
				icons.add(icon);
				iconListModel.addElement(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				String[] descrip = ((ImageIcon) resultList.getSelectedValue()).getDescription().split("%%%%");
				resultList.setToolTipText(
		                "<html>Name: " + descrip[1] + " <br>Year of Release: " + descrip[2] + "</html>");
			}
		});
		this.setPreferredSize(new Dimension(280, 500));
		this.pack();
		this.setScreenLocation();
	}

	/**
	 * Helping method for filling an ArrayList of Strings with poster, title and
	 * year of the SearchResults
	 * 
	 * @param searchResult
	 *            ArrayList of searchResults that should be displayed
	 * @return Another ArrayList containing String of the poster, the title and
	 *         the year of the movie.
	 */
	private ArrayList<String> fillArrayListFromArrayList(ArrayList<MediaSearchResult> searchResult) {
		ArrayList<String> urlList = new ArrayList<String>();
		for (MediaSearchResult temp : searchResult) {
			if (temp.getPoster() == null) {
				// throw new MalformedURLException();
			}
			String description = temp.getPoster() + "%%%%" + temp.getTitle() + "%%%%" + temp.getYear() + "%%%%"
			        + temp.getImdbId();
			urlList.add(description);
		}
		return urlList;
	}

	/**
	 * Helping method for turning an ArrayList of SearchResults into an
	 * ArrayList of MediaSearchResults
	 * 
	 * @param searchResults
	 *            The ArrayList of SearchResults
	 * @return The same list containing MediaSearchResults
	 */
	private ArrayList<MediaSearchResult> switchArrayList(ArrayList<SearchResult> searchResults) {
		ArrayList<MediaSearchResult> mediaSearchResult = new ArrayList<MediaSearchResult>();
		for (SearchResult temp : searchResults) {
			mediaSearchResult.add((MediaSearchResult) temp);
		}
		return mediaSearchResult;
	}
	
	/**
	 * Helping method for showing the Frame in the middle of every screen.
	 */
	private void setScreenLocation() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] allDevices = env.getScreenDevices();
		int width = (int) allDevices[0].getDefaultConfiguration().getBounds().width;
		int height = (int) allDevices[0].getDefaultConfiguration().getBounds().height;
		this.setLocation(((width/2) - (this.getWidth()/2)), ((height/2) - (this.getHeight()/2)));
	}
}
