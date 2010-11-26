package com.jukejuice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.jaudiotagger.tag.FieldKey;

public class Db {
	private String sqliteFilePath;
	
	private void exec (String sql) throws ClassNotFoundException, SQLException
	{
		Connection conn = getConnection();
        Statement statement = conn.createStatement();
        conn.setAutoCommit(true);
        statement.execute(sql);
        conn.close();
	}
	
	public void addSongs(List<SongFileInfo> songInfos) throws ClassNotFoundException, SQLException
	{
		Connection conn = getConnection();
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
	
	public void createSongTable() throws ClassNotFoundException, SQLException
	{
		exec("create table if not exists song (artist, title, length)");
	}
	
	
	public void dropSongTable() throws ClassNotFoundException, SQLException
	{
		exec("drop table if exists song");
	}
	
	private Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
        Connection conn =
        	DriverManager.getConnection("jdbc:sqlite:" + sqliteFilePath);
        return conn;
	}
	
	public String getSqliteFilePath() {
		if (sqliteFilePath == null)
			sqliteFilePath ="live.db"; 
		return sqliteFilePath;
	}
	
	public void setSqliteFilePath(String sqliteFilePath) {
		this.sqliteFilePath = sqliteFilePath;
	}
}
