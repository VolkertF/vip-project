package com.sqlite;

import java.sql.*;
import java.util.ArrayList;

import com.vip.attributes.*;

public class SQLiteTest {

	public SQLiteTest() {

	}

	public void createTable(Connection c) {

		Statement stmt = null;

		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS VIDEO "
					+ "(PATH STRING 	PRIMARY KEY     NOT NULL,"
					+ " TITLE           STRING 			NOT NULL,"
					+ " DIRECTOR        STRING,"
					+ " PLOT        	STRING)" ;
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	public void fillTable(Connection c) {

		try {

			
			Video testMovie = new Video();
			testMovie.setFilePath("/turbiaka/videos/Matrix.mkv");
			testMovie.setTitle("The Matrix");
			testMovie.setDirector("The Wachowskis");
			testMovie.setPlotSummary("The world is a computer.");
			
			String insertString = 	"INSERT INTO VIDEO (PATH,TITLE,DIRECTOR,PLOT) " +
									"VALUES (?,?,?,?)";
			
			PreparedStatement statement = c.prepareStatement(insertString);
			
			statement.setString(1, testMovie.getFilePath());
			statement.setString(2, testMovie.getTitle());
			statement.setString(3, testMovie.getDirector());
			statement.setString(4, testMovie.getPlotSummary());
			
			statement.execute();
			
			c.commit();
			statement.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	public void printTable(Connection c){
		
	    Statement stmt = null;
	    try {
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM VIDEO " );
	      while ( rs.next() ) {
	         String path = rs.getString("PATH");
	         String  title  = rs.getString("TITLE");
	         String director = rs.getString("DIRECTOR");
	         String  plot = rs.getString("PLOT");
	         System.out.println( "Path: " + path );
	         System.out.println( "Title: " + title );
	         System.out.println( "Director: " + director );
	         System.out.println( "Plot: " + plot );
	         System.out.println();
	      }
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Operation done successfully");
		
	}
	
	public static void main(String args[]) {

		SQLiteTest test = new SQLiteTest();

		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			ArrayList<String> testing = new ArrayList<String>();
			
			testing.add("How");
			testing.add("does");
			testing.add("this");
			testing.add("work?");
			
			System.out.println(testing);
			
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}