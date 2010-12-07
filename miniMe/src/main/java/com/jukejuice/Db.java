package com.jukejuice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jaudiotagger.tag.FieldKey;

public class Db {
	private String sqliteFilePath;
	public static final String PROD_DB = "prod.db";
	public static final String TESTING_DB = "testing.db";		
	
	private static final Logger log = Logger.getLogger(Db.class);
	
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
		PreparedStatement preparedStatement = conn.prepareStatement("insert into song (artist, title, length, filename) values (?, ?, ?, ?)");
		for (SongFileInfo info: songInfos)
		{
			try
			{
				preparedStatement.setString(1, info.tag.getFirst(FieldKey.ARTIST));
				preparedStatement.setString(2, info.tag.getFirst(FieldKey.TITLE));
				preparedStatement.setInt(3, info.header.getTrackLength());
				preparedStatement.setString(4, info.filename);
				preparedStatement.addBatch();
			}
			catch (Exception e)
			{
				log.error("Failure on songInfo " + info.f.getFile().toString(), e);
				e.printStackTrace();
			}
		}
		conn.setAutoCommit(false);
		preparedStatement.executeBatch();
		conn.setAutoCommit(true);
		conn.close();
	}
	
	public void initDb() throws SQLException
	{
		exec("create table if not exists song (id integer primary key, artist, title, length, filename)");
		exec("create table if not exists user (id integer primary key, ip_address, used_energy, max_energy)");
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
			log.error("One song per ID constraint not being respected");

		return null;
	}
	
	public User findUserById (int uuid)
	{
		Connection conn = getConnection();
		ResultSet resultSet;
		try {
			PreparedStatement statement = conn.prepareStatement(
				"select * from user where id = ?");
			statement.setInt(1, uuid);
			statement.execute();
			resultSet = statement.getResultSet();

			String ipAddress = resultSet.getString("ip_address"); 
			int usedEnergy = resultSet.getInt("used_energy");
			int maxEnergy = resultSet.getInt("max_energy");
			conn.close();
			
			User user = new User();
			user.setId(uuid);
			user.setIpAddress(ipAddress);
			user.setMaxEnergy(maxEnergy);
			user.setUsedEnergy(usedEnergy);
			return user;			

		} catch (SQLException e) {
			log.error("", e);
		}
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
			//int length = resultSet.getInt("length");
			String filename = resultSet.getString("filename");
			Song song = new Song(id,filename,artist,title);
			songs.add(song);
		}
		return songs;
	}

	public User createUser(String ipAddress) {
		try {
			log.info("creating user " + ipAddress);
			
			Connection conn = getConnection();
			int uuid = UUID.randomUUID().hashCode(); 
			PreparedStatement preparedStatement = conn.prepareStatement(
					"insert into user (id, ip_address, used_energy, max_energy) values (?,?,?,?)");
			preparedStatement.setInt(1, uuid);
			preparedStatement.setString(2, ipAddress);
			preparedStatement.setInt(3, 0);
			preparedStatement.setInt(4, 5);
			preparedStatement.execute();
			conn.close();
			
			return findUserById(uuid);
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}

	public void updateUser(User user) {
		try {
			Connection conn = getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(
					"update user set ip_address = ?, used_energy = ?, max_energy = ? where id = ?");
			preparedStatement.setString(1, user.getIpAddress());
			preparedStatement.setInt(2, user.getUsedEnergy());
			preparedStatement.setInt(3, user.getMaxEnergy());
			preparedStatement.setInt(4, user.getId());
			preparedStatement.execute();
			conn.close();			
			
		} catch (SQLException e) {
			log.error("", e);
		}
	}
	
	public void regenerateEnergy()
	{
		Connection conn = getConnection();
		try {
			PreparedStatement increaseEnergyStatement = conn.prepareStatement(
					"update user set used_energy = used_energy - 1 where id in " +
					"(select id from user where used_energy != 0)");
			increaseEnergyStatement.execute();
		} catch (SQLException e) {
			log.error("", e);
		}
		
	}

	public Song getRandomSong() {
		Connection conn = getConnection();
		try {
			PreparedStatement increaseEnergyStatement = conn.prepareStatement(
			"select count(*) from song;");
			increaseEnergyStatement.execute();
			int songCount = increaseEnergyStatement.getResultSet().getInt(1);
			conn.close();
			int randomSongId = ((Double) Math.ceil(Math.random() * songCount)).intValue();
			Song song = findSongById(randomSongId);
			if (song == null)
				return getRandomSong();
			return song;
		} catch (SQLException e) {
			log.error("", e);
		} 
		return null;
	}
}
