package com.jukejuice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jaudiotagger.tag.FieldKey;

public class Db {
	private String sqliteFilePath;
	public static final String PROD_DB = "prod.db";
	public static final String TESTING_DB = "testing.db";

	private static final Logger log = Logger.getLogger(Db.class);

	/**
	 * Executes a string of SQL, useful if you don't need a ResultSet
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	private void exec(String sql) throws SQLException {
		Connection conn = getConnection();
		Statement statement = conn.createStatement();
		conn.setAutoCommit(true);
		statement.execute(sql);
		conn.close();
	}

	public void addSongs(List<SongFileInfo> songInfos) throws SQLException {
		Connection conn = getConnection();
		// TODO add filename here
		PreparedStatement preparedStatement = conn
				.prepareStatement("insert into song (artist, title, album, year, length, filename) values (?, ?, ?, ?, ?, ?)");
		for (SongFileInfo info : songInfos) {
			try {
				for (FieldKey fieldKey : FieldKey.values()) {
					try {
						log.info(info.filename + " " + fieldKey.name() + " "
								+ info.tag.getFirst(fieldKey));
						log.info(info.tag.getFirstArtwork());
					} catch (Exception e) {
						log.warn(e);
					}
				}

				preparedStatement.setString(1, info.tag
						.getFirst(FieldKey.ARTIST));
				preparedStatement.setString(2, info.tag
						.getFirst(FieldKey.TITLE));
				preparedStatement.setString(3, info.tag
						.getFirst(FieldKey.ALBUM));
				preparedStatement.setInt(4, Integer.parseInt(info.tag
						.getFirst(FieldKey.YEAR)));
				preparedStatement.setInt(5, info.header.getTrackLength());
				preparedStatement.setString(6, info.filename);
				preparedStatement.addBatch();
			} catch (Exception e) {
				log.error("Failure on songInfo " + info.f.getFile().toString(),
						e);
			}
		}
		conn.setAutoCommit(false);
		preparedStatement.executeBatch();
		conn.setAutoCommit(true);
		conn.close();
	}

	/**
	 * Create all the tables needed by the application if they don't already
	 * exist
	 * 
	 * @throws SQLException
	 */
	public void initDb() throws SQLException {
		exec("create table if not exists song (id integer primary key, artist, title, album, year, length, filename)");
		exec("create table if not exists user (id integer primary key, ip_address, used_energy, max_energy)");
		exec("create table if not exists song_set (id integer primary key, name)");
		exec("create table if not exists set_membership (id integer primary key, song_id, set_id)");
	}

	public void dropTables() throws SQLException {
		exec("drop table if exists song");
		exec("drop table if exists user");
	}

	private Connection getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"
					+ getSqliteFilePath());
			return conn;
		} catch (ClassNotFoundException e) {
			log.error("", e);
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}

	/**
	 * return the live database file if a different db file hasn't been set
	 * 
	 * @return
	 */
	public String getSqliteFilePath() {
		if (sqliteFilePath == null)
			sqliteFilePath = "live.db";
		return sqliteFilePath;
	}

	public void setSqliteFilePath(String sqliteFilePath) {
		this.sqliteFilePath = sqliteFilePath;
	}

	/**
	 * return a list of songs with matching title or artist
	 * 
	 * @param search
	 * @return
	 * @throws SQLException
	 */
	public List<Song> search(String search) throws SQLException {
		Connection connection = getConnection();

		PreparedStatement statement = connection
				.prepareStatement("select * from song where artist like ? or title like ?");
		statement.setString(1, "%" + search + "%");
		statement.setString(2, "%" + search + "%");
		ResultSet resultSet = statement.executeQuery();
		List<Song> results = resultSetToSongs(resultSet, connection);
		connection.close();
		return results;
	}

	public Song findSongById(int id) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("select * from song where id = ?");
		preparedStatement.setInt(1, id);
		preparedStatement.execute();
		List<Song> songs = resultSetToSongs(preparedStatement.getResultSet(),
				connection);
		connection.close();
		if (songs.size() == 1)
			return songs.get(0);
		if (songs.size() == 0)
			return null;
		else
			log.error("One song per ID constraint not being respected");

		return null;
	}

	private List<Song> resultSetToSongs(ResultSet resultSet, Connection conn)
			throws SQLException {
		List<Song> songs = new ArrayList<Song>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String artist = resultSet.getString("artist");
			String title = resultSet.getString("title");
			String album = resultSet.getString("album");
			int year = resultSet.getInt("year");
			String filename = resultSet.getString("filename");
			int length = resultSet.getInt("length");
			Song song = new Song(id, filename, artist, title, album, year,
					length);

			PreparedStatement preparedStatement = conn
					.prepareStatement("select * from song_set where id in (select set_id from set_membership where song_id = ?)");
			preparedStatement.setInt(1, id);
			ResultSet songSetResults = preparedStatement.executeQuery();
			Set<SongSet> songSets = new HashSet<SongSet>();
			while (songSetResults.next()) {
				SongSet songSet = new SongSet();
				songSet.setName(songSetResults.getString("name"));
				songSet.setId(songSetResults.getInt("id"));
				songSets.add(songSet);
			}
			song.setSongSets(songSets);
			songs.add(song);
		}
		return songs;
	}

	public User getUser(String ipAddress, int uuid) {
		Connection conn = getConnection();
		ResultSet resultSet;
		try {
			PreparedStatement statement = conn
					.prepareStatement("select * from user where id = ?");
			statement.setInt(1, uuid);
			statement.execute();
			resultSet = statement.getResultSet();

			int usedEnergy, maxEnergy;
			User user = new User();
			if (resultSet.next()) {
				ipAddress = resultSet.getString("ip_address");
				usedEnergy = resultSet.getInt("used_energy");
				maxEnergy = resultSet.getInt("max_energy");
			} else {
				createUser(ipAddress, uuid);
				conn.close();
				return getUser(ipAddress, uuid);
			}
			conn.close();

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

	/**
	 * create a user in the database with default energy attributes and random
	 * UUID
	 * 
	 * @param ipAddress
	 * @return
	 */
	public User createUser(String ipAddress, Integer uuid) {
		try {
			log.info("creating user " + ipAddress);

			Connection conn = getConnection();
			if (uuid == null)
				uuid = UUID.randomUUID().hashCode();
			PreparedStatement preparedStatement = conn
					.prepareStatement("insert into user (id, ip_address, used_energy, max_energy) values (?,?,?,?)");
			preparedStatement.setInt(1, uuid);
			preparedStatement.setString(2, ipAddress);
			preparedStatement.setInt(3, 0);
			preparedStatement.setInt(4, 5);
			preparedStatement.execute();
			conn.close();

		} catch (SQLException e) {
			log.error("", e);
		}
		return getUser(ipAddress, uuid);
	}

	public void updateUser(User user) {
		try {
			Connection conn = getConnection();
			PreparedStatement preparedStatement = conn
					.prepareStatement("update user set ip_address = ?, used_energy = ?, max_energy = ? where id = ?");
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

	/**
	 * give one energy back to everybody
	 */
	public void regenerateEnergy() {
		Connection conn = getConnection();
		try {
			PreparedStatement increaseEnergyStatement = conn
					.prepareStatement("update user set used_energy = used_energy - 1 where id in "
							+ "(select id from user where used_energy != 0)");
			increaseEnergyStatement.execute();
			conn.close();
		} catch (SQLException e) {
			log.error("", e);
		}

	}

	public Song getRandomSong() {
		Connection conn = getConnection();
		try {
			PreparedStatement increaseEnergyStatement = conn
					.prepareStatement("select count(*) from song;");
			increaseEnergyStatement.execute();
			int songCount = increaseEnergyStatement.getResultSet().getInt(1);
			conn.close();
			int randomSongId = ((Double) Math.ceil(Math.random() * songCount))
					.intValue();
			Song song = findSongById(randomSongId);
			if (song == null)
				return getRandomSong();
			return song;
		} catch (SQLException e) {
			log.error("", e);
		}
		return null;
	}

	public int createSet(String setName) {
		System.out.println("inserting set " + setName);
		Connection conn = getConnection();
		try {
			PreparedStatement insertSetStmt = conn
					.prepareStatement("insert into song_set (name) values (?)");
			insertSetStmt.setString(1, setName);
			insertSetStmt.execute();

			PreparedStatement getSetIdStmt = conn
					.prepareStatement("select * from song_set where name = ?");
			getSetIdStmt.setString(1, setName);
			ResultSet resultSet = getSetIdStmt.executeQuery();
			int id = resultSet.getInt("id");
			conn.close();
			return id;
		} catch (SQLException e) {
			log.error("", e);
			return -1;
		}
	}

	public SongSet findSetById(int setId, boolean includeSongs) {
		Connection conn = getConnection();
		PreparedStatement stmt;
		SongSet songSet = null;
		try {
			stmt = conn.prepareStatement("select * from song_set where id = ?");
			stmt.setInt(1, setId);
			ResultSet resultSet = stmt.executeQuery();
			String name = resultSet.getString("name");
			songSet = new SongSet();
			songSet.setName(name);

			if (includeSongs) {
				System.out.println(setId);
				stmt = conn
						.prepareStatement("select * from song where id in (select song_id from set_membership where set_id = ?)");
				stmt.setInt(1, setId);
				List<Song> songs = resultSetToSongs(stmt.executeQuery(), conn);
				songSet.setSongs(songs);
			}
			conn.close();
		} catch (SQLException e) {
			log.error("", e);
		}
		return songSet;
	}

	public void addSongToSet(int songId, int setId) {
		Connection conn = getConnection();
		PreparedStatement stmt;
		try {
			stmt = conn
					.prepareStatement("insert into set_membership (song_id, set_id) values (?,?)");
			stmt.setInt(1, songId);
			stmt.setInt(2, setId);
			stmt.execute();
			conn.close();
		} catch (SQLException e) {
			log.error("", e);
		}
	}

}
