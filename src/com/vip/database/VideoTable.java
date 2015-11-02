package com.vip.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.DateTime;

import com.vip.attributes.*;

public class VideoTable {

	public void createTable() {

		Connection c = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			statement = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS VIDEO "
					+ "(ID				INT		PRIMARY KEY		NOT NULL,"
					+ "	PATH 			STRING					NOT NULL,"
					+ " TITLE           STRING 					NOT NULL,"
					+ " RELEASE_DATE 	DATETIME," 
					+ " GENRE			STRING,"
					+ " DIRECTOR        STRING," 
					+ "	CAST			STRING,"
					+ " WRITERS			STRING," 
					+ " PLOT        	STRING,"
					+ " COUNTRY			STRING," 
					+ " IMDB_RATING		DOUBLE,"
					+ " PERSONAL_RATING	DOUBLE," 
					+ " SEASON			INT,"
					+ " EPISODE			INT)";

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

		Connection c = null;
		PreparedStatement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);

			String updateString = "	UPDATE VIDEO"
					+ " SET (PATH,TITLE,RELEASE_DATE,GENRE,DIRECTOR,CAST,WRITERS,"
					+ "	PLOT,COUNTRY,IMDB_RATING,PERSONAL_RATING,SEASON,EPISODE) "
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)" + " WHERE ID = ?";

			statement = c.prepareStatement(updateString);

			statement.setString(1, video.getFilePath());
			statement.setString(2, video.getTitle());
			statement.setString(3, video.getReleaseDate().toString());
			statement.setString(4, video.getGenre().toString()
					.replace("[", "'")
					.replace("]", "'"));
			statement.setString(5, video.getDirector());
			statement.setString(6, video.getCast().toString()
							.replace("[", "'")
							.replace("]", "'"));
			statement.setString(7, video.getWriters().toString()
							.replace("[", "'")
							.replace("]", "'"));
			statement.setString(8, video.getPlotSummary());
			statement.setString(9, video.getCountry());
			statement.setDouble(10, video.getImdbRating());
			statement.setDouble(11, video.getPersonalRating());
			statement.setInt(12, video.getSeason());
			statement.setInt(13, video.getEpisode());
			statement.setInt(14, video.getID());

			statement.execute();

			c.commit();
			statement.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}
	
	public void insertVideo(Video video){
		Connection c = null;
		PreparedStatement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);

			String insertString = "	INSERT INTO VIDEO"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			statement = c.prepareStatement(insertString);

			statement.setString(1, video.getFilePath());
			statement.setString(2, video.getTitle());
			statement.setString(3, video.getReleaseDate().toString());
			statement.setString(4, video.getGenre().toString()
					.replace("[", "'")
					.replace("]", "'"));
			statement.setString(5, video.getDirector());
			statement.setString(6, video.getCast().toString()
							.replace("[", "'")
							.replace("]", "'"));
			statement.setString(7, video.getWriters().toString()
							.replace("[", "'")
							.replace("]", "'"));
			statement.setString(8, video.getPlotSummary());
			statement.setString(9, video.getCountry());
			statement.setDouble(10, video.getImdbRating());
			statement.setDouble(11, video.getPersonalRating());
			statement.setInt(12, video.getSeason());
			statement.setInt(13, video.getEpisode());

			statement.execute();

			c.commit();
			statement.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}	
		
	}

	public void loadVideos() {

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
				video.setID(rs.getInt("ID"));
				video.setFilePath(rs.getString("PATH"));
				video.setTitle(rs.getString("TITLE"));
				video.setReleaseDate(DateTime.parse(rs.getString("RELEASE_DATE")));
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
		System.out.println("Operation done successfully");

	}

	private ArrayList<String> buildArrayList(String string) {
		
		String[] array = string.split(",");
		
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(array));
		
		System.out.println(list);
		
		return list;
	}
}
