package com.vip.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import com.vip.attributes.Video;

public class VideoTable {

	public void createTable() {

		Connection c = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);

			statement = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS VIDEO " + "(PATH 			STRING		PRIMARY KEY		NOT NULL,"
			        + " TITLE           STRING 						NOT NULL," + " RELEASE_DATE 	DATE,"
			        + " GENRE			STRING," + " DIRECTOR        STRING," + "	CAST			STRING,"
			        + " WRITERS			STRING," + " PLOT        	STRING," + " COUNTRY			STRING,"
			        + " IMDB_RATING		DOUBLE," + " PERSONAL_RATING	DOUBLE," + " SEASON			INT,"
			        + " EPISODE			INT," + " INFO_FETCHED	BOOLEAN)";

			statement.executeUpdate(sql);

			statement.close();
			c.commit();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public void saveVideo(Video video) {
		if (video.hasChanged()) {
			Connection c = null;
			PreparedStatement statement = null;

			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:test.db");
				c.setAutoCommit(false);

				String updateString = "	INSERT OR REPLACE INTO VIDEO"
				        + " (PATH,TITLE,RELEASE_DATE,GENRE,DIRECTOR,CAST,WRITERS,"
				        + "	PLOT,COUNTRY,IMDB_RATING,PERSONAL_RATING,SEASON,EPISODE,INFO_FETCHED) "
				        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				statement = c.prepareStatement(updateString);

				statement.setString(1, video.getFilePath());
				statement.setString(2, video.getTitle());
				statement.setDate(3, video.getReleaseDate());

				if (video.getGenre() != null) {
					statement.setString(4, video.getGenre().toString().replace("[", "'").replace("]", "'"));
				}
				statement.setString(5, video.getDirector());
				if (video.getCast() != null) {
					statement.setString(6, video.getCast().toString().replace("[", "'").replace("]", "'"));
				}
				if (video.getWriters() != null) {
					statement.setString(7, video.getWriters().toString().replace("[", "'").replace("]", "'"));
				}
				statement.setString(8, video.getPlotSummary());
				statement.setString(9, video.getCountry());
				statement.setDouble(10, video.getImdbRating());
				statement.setDouble(11, video.getPersonalRating());
				statement.setInt(12, video.getSeason());
				statement.setInt(13, video.getEpisode());
				statement.setBoolean(14, video.isInfoFetched());

				statement.execute();

				c.commit();
				statement.close();

			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
		}
	}

	public void deleteVideo(Video video) {
		Connection c = null;
		PreparedStatement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);

			String updateString = "	DELETE FROM VIDEO WHERE PATH = ?";
			statement = c.prepareStatement(updateString);

			statement.setString(1, video.getFilePath());

			statement.execute();
			c.commit();
			statement.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	/*
	 * public void insertVideo(Video video){ Connection c = null;
	 * PreparedStatement statement = null;
	 * 
	 * try { Class.forName("org.sqlite.JDBC"); c =
	 * DriverManager.getConnection("jdbc:sqlite:test.db");
	 * c.setAutoCommit(false);
	 * 
	 * String insertString = "	INSERT INTO VIDEO" +
	 * " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	 * 
	 * statement = c.prepareStatement(insertString);
	 * 
	 * statement.setString(1, video.getFilePath()); statement.setString(2,
	 * video.getTitle()); statement.setString(3,
	 * video.getReleaseDate().toString()); statement.setString(4,
	 * video.getGenre().toString() .replace("[", "'") .replace("]", "'"));
	 * statement.setString(5, video.getDirector()); statement.setString(6,
	 * video.getCast().toString() .replace("[", "'") .replace("]", "'"));
	 * statement.setString(7, video.getWriters().toString() .replace("[", "'")
	 * .replace("]", "'")); statement.setString(8, video.getPlotSummary());
	 * statement.setString(9, video.getCountry()); statement.setDouble(10,
	 * video.getImdbRating()); statement.setDouble(11,
	 * video.getPersonalRating()); statement.setInt(12, video.getSeason());
	 * statement.setInt(13, video.getEpisode());
	 * 
	 * statement.execute();
	 * 
	 * c.commit(); statement.close();
	 * 
	 * } catch (Exception e) { System.err.println(e.getClass().getName() + ": "
	 * + e.getMessage()); System.exit(0); }
	 * 
	 * }
	 */

	public ArrayList<Video> loadVideos() {

		Connection c = null;
		Statement statement = null;

		ArrayList<Video> videoList = new ArrayList<Video>();
		try {

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			statement = c.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM VIDEO ");

			while (rs.next()) {
				Video video = new Video();
				video.setFilePath(rs.getString("PATH"));
				video.setTitle(rs.getString("TITLE"));
				video.setReleaseDate(rs.getDate("RELEASE_DATE"));
				video.setGenre(buildArrayList(rs.getString("GENRE")));
				video.setDirector(rs.getString("DIRECTOR"));
				video.setCast(buildArrayList(rs.getString("CAST")));
				video.setWriters(buildArrayList(rs.getString("WRITERS")));
				video.setPlotSummary(rs.getString("PLOT"));
				video.setCountry(rs.getString("COUNTRY"));
				video.setImdbRating(rs.getDouble("IMDB_RATING"));
				video.setPersonalRating(rs.getDouble("PERSONAL_RATING"));
				video.setSeason(rs.getInt("SEASON"));
				video.setEpisode(rs.getInt("EPISODE"));

				videoList.add(video);

			}
			rs.close();
			statement.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return videoList;

	}

	private ArrayList<String> buildArrayList(String string) {

		if (string != null) {
			String[] array = string.split(",");

			ArrayList<String> list = new ArrayList<String>(Arrays.asList(array));

			System.out.println(list);

			return list;
		}
		return null;
	}

}
