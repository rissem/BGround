package com.jukejuice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.tag.FieldKey;

public class Db {
	private String sqliteFilePath;
	public static final String PROD_DB = "prod.db";
	public static final String TESTING_DB = "testing.db";		
	
	private void exec (String sql) throws SQLException
	{
		Connection conn = getConnection();
        Statement statement = conn.createStatement();
        conn.setAutoCommit(true);
        statement.execute(sql);
        conn.close();
	}
	
	public void addSongs(List<SongFileInfo> songInfos) throws SQLException
	{
		Connection conn = getConnection();
		//TODO add filename here
		PreparedStatement preparedStatement = conn.prepareStatement("insert into song (artist, title, length) values (?, ?, ?)");
		for (SongFileInfo info: songInfos)
		{
			try
			{
				preparedStatement.setString(1, info.tag.getFirst(FieldKey.ARTIST));
				preparedStatement.setString(2, info.tag.getFirst(FieldKey.TITLE));
				preparedStatement.setInt(3, info.header.getTrackLength());
				preparedStatement.addBatch();
			}
			catch (Exception e)
			{
				System.out.println("Failure on songInfo " + info.f.getFile().toString());
				e.printStackTrace();
			}
		}
		conn.setAutoCommit(false);
		preparedStatement.executeBatch();
		conn.setAutoCommit(true);
		conn.close();
	}
	
	public void createSongTable() throws SQLException
	{
		exec("create table if not exists song (id integer primary key, artist, title, length, filename)");
	}
	
	public void dropSongTable() throws SQLException
	{
		exec("drop table if exists song");
	}
	
	private Connection getConnection() 
	{
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + getSqliteFilePath());
	        return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getSqliteFilePath() {
		if (sqliteFilePath == null)
			sqliteFilePath ="live.db"; 
		return sqliteFilePath;
	}
	
	public void setSqliteFilePath(String sqliteFilePath) {
		this.sqliteFilePath = sqliteFilePath;
	}
	
	public List<Song> search(String search) throws SQLException
	{
		Connection connection = getConnection();

		PreparedStatement statement = connection.prepareStatement(
				"select * from song where artist like ? or title like ?");
		statement.setString(1, "%" + search + "%");
		statement.setString(2, "%" + search + "%");
		ResultSet resultSet = statement.executeQuery();
		List<Song> results = resultSetToSongs(resultSet);
		connection.close();
		return results;
	}
	
	public Song findSongById (int id) throws SQLException
	{
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(
				"select * from song where id = ?");
		preparedStatement.setInt(1, id);
		preparedStatement.execute();
		List<Song> songs = resultSetToSongs(preparedStatement.getResultSet());
		connection.close();
		if (songs.size() == 1)
			return songs.get(0);
		if (songs.size() == 0)
			return null;
		else
			System.err.println("One song per ID constraint not being respected");

		return null;
	}
	
	private List<Song> resultSetToSongs(ResultSet resultSet) throws SQLException
	{
		List<Song> songs = new ArrayList<Song>();
		while (resultSet.next())
		{
			int id = resultSet.getInt("id");
			String artist = resultSet.getString("artist");
			String title = resultSet.getString("title");
			int length = resultSet.getInt("length");
			Song song = new Song(id,artist,title);
			songs.add(song);
		}
		return songs;
	}
}
