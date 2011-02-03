package com.jukejuice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jaudiotagger.tag.FieldKey;

public class Db {
	private String sqliteFilePath;
	public static final String PROD_DB = "prod.db";
	public static final String TESTING_DB = "testing.db";

	private static final Logger log = Logger.getLogger(Db.class);

	public void addSongs(final List<SongFileInfo> songInfos) {
		DbTask<Void> dbTask = new DbTask<Void>() {

			@Override
			public Void exec() throws SQLException {
				PreparedStatement preparedStatement = conn
						.prepareStatement("insert into song (artist, title, album, year, length, filename, banned) values (?, ?, ?, ?, ?, ?, ?)");
				for (SongFileInfo info : songInfos) {
					if (info.tag != null)
					try {
						preparedStatement.setString(1, info.tag
								.getFirst(FieldKey.ARTIST));
						preparedStatement.setString(2, info.tag
								.getFirst(FieldKey.TITLE));
						preparedStatement.setString(3, info.tag
								.getFirst(FieldKey.ALBUM));
						int year = 0;
						try {
							year = Integer.parseInt(info.tag.getFirst(FieldKey.YEAR));
						}
						catch (Exception e)
						{
							log.warn("failed to parse year");
							log.warn(e);
						}
						preparedStatement.setInt(4, year);
						preparedStatement.setInt(5, info.header
								.getTrackLength());
						preparedStatement.setString(6, info.filename);
						preparedStatement.setBoolean(7, false);						
						preparedStatement.addBatch();
					} catch (Exception e) {
						log.error("Failure on songInfo "
								+ info.f.getFile().toString(), e);
					}
				}
				try {
					conn.setAutoCommit(false);
					preparedStatement.executeBatch();
					conn.setAutoCommit(true);
				}
				catch (Exception e)
				{
					log.error("", e);
				}
				return null;
			}
		};
		dbTask.run();
	}

	/**
	 * Create all the tables needed by the application if they don't already
	 * exist
	 * 
	 * @throws SQLException
	 */
	public void initDb() throws SQLException {
		DbTask<Void> dbTask = new DbTask<Void>() {
			@Override
			public Void exec() throws SQLException {
				Statement statement = conn.createStatement();
				conn.setAutoCommit(true);
				statement.execute("create table if not exists song (id integer primary key, artist, title, album, year, length, filename, banned, lastPlayed)");
				statement.execute("create index if not exists artist_idx on song(artist)");
				statement.execute("create index if not exists title_idx on song(title)");
				statement.execute("create unique index if not exists filename_idx on song(filename)");
				statement.execute("create table if not exists user (id integer primary key, ip_address, used_energy, max_energy)");
				statement.execute("create table if not exists song_set (id integer primary key, name)");
				statement.execute("create table if not exists set_membership (id integer primary key, song_id, set_id)");				
				return null;
			}
		};
		dbTask.run();
	}

	public void dropTables() throws SQLException {
		//does it make more sense to just remove the one db file so we can be certain that everything is deleted?
		new DbTask<Void>() {
			@Override
			public Void exec() throws SQLException {
				Statement statement = conn.createStatement();
				conn.setAutoCommit(true);
				statement.execute("drop table if exists song");
				statement.execute("drop table if exists user");
				statement.execute("drop table if exists song_set");
				statement.execute("drop table if exists set_membership");
				return null;
			}
		}.run();
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
	public List<Song> search(final String search) throws SQLException {
		return new DbTask<List<Song>>() {
			@Override
			public List<Song> exec() throws SQLException {
				PreparedStatement statement = conn
						.prepareStatement("select * from song where (artist like ? or title like ?) and banned = ?");
				statement.setString(1, "%" + search + "%");
				statement.setString(2, "%" + search + "%");
				statement.setBoolean(3, false);
				ResultSet resultSet = statement.executeQuery();
				return resultSetToSongs(resultSet, conn);
			}
		}.run();
	}

	public Song findSongById(final int id) throws SQLException {

		return new DbTask<Song>() {
		
			@Override
			public Song exec() throws SQLException {
				PreparedStatement preparedStatement = conn.prepareStatement("select * from song where id = ?");
				preparedStatement.setInt(1, id);
				preparedStatement.execute();
				List<Song> songs = resultSetToSongs(preparedStatement.getResultSet(),conn);
				System.out.println("any songs?");
				if (songs.size() == 1) {
					return songs.get(0);
				}
				if (songs.size() == 0) {
					return null;
				}
				else {
					log.error("One song per ID constraint not being respected");
					return null;
				}
			}
		}.run();
	}

	private List<Song> resultSetToSongs(final ResultSet resultSet, final Connection conn){
		DbTask<List<Song>> task = new DbTask<List<Song>>() {
		
			@Override
			public List<Song> exec() throws SQLException {
				List<Song> songs = new ArrayList<Song>();
				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String artist = resultSet.getString("artist");
					String title = resultSet.getString("title");
					String album = resultSet.getString("album");
					int year = resultSet.getInt("year");
					String filename = resultSet.getString("filename");
					int length = resultSet.getInt("length");
					boolean banned = resultSet.getBoolean("banned");
					Song song = new Song(id, filename, artist, title, album, year,
							length, banned);
					song.setLastPlayed(resultSet.getDate("lastPlayed"));
					songs.add(song);
				}
				return songs;
			}
		};
		return task.run(conn);
	}

	public User getUser(final String ipAddress, final int uuid) {
		return getUserDbTask(ipAddress, uuid).run();
	}
	
	private DbTask<User> getUserDbTask(final String ipAddress, final int uuid)
	{
		return new DbTask<User>() {

			@Override
			public User exec() throws SQLException {

				PreparedStatement statement = conn
						.prepareStatement("select * from user where id = ?");
				statement.setInt(1, uuid);
				statement.execute();
				ResultSet resultSet = statement.getResultSet();

				int usedEnergy, maxEnergy;
				User user = new User();
				if (resultSet.next()) {
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
			}
		};		
	}
	
	public User getUser(final String ipAddress, final int uuid, Connection conn)
	{
		return getUserDbTask(ipAddress, uuid).run(conn);		
	}

	/**
	 * create a user in the database with default energy attributes and random
	 * UUID
	 * 
	 * @param ipAddress
	 * @return
	 */
	public User createUser(final String ipAddress,  Integer uuid) {
		final int userId;
		if (uuid == null)
			userId = UUID.randomUUID().hashCode();			
		else
			userId = uuid;
		return new DbTask<User>() {
			@Override
			public User exec() throws SQLException {
				log.info("creating user " + ipAddress);

				PreparedStatement preparedStatement = conn
						.prepareStatement("insert into user (id, ip_address, used_energy, max_energy) values (?,?,?,?)");
				preparedStatement.setInt(1, userId);
				preparedStatement.setString(2, ipAddress);
				preparedStatement.setInt(3, 0);
				preparedStatement.setInt(4, 5);
				preparedStatement.execute();
				return getUser(ipAddress, userId, conn);
			}
		}.run();
	}

	public void updateUser(final User user) {
		new DbTask<Void>() {
		
			@Override
			public Void exec() throws SQLException {
				PreparedStatement preparedStatement = conn
						.prepareStatement("update user set ip_address = ?, used_energy = ?, max_energy = ? where id = ?");
				preparedStatement.setString(1, user.getIpAddress());
				preparedStatement.setInt(2, user.getUsedEnergy());
				preparedStatement.setInt(3, user.getMaxEnergy());
				preparedStatement.setInt(4, user.getId());
				preparedStatement.execute();
				return null;
			}
		}.run();
	}

	/**
	 * give one energy back to everybody
	 */
	public void regenerateEnergy() {
		new DbTask<Void>() {

			@Override
			public Void exec() throws SQLException {
				PreparedStatement increaseEnergyStatement = conn
						.prepareStatement("update user set used_energy = used_energy - 1 where id in "
								+ "(select id from user where used_energy != 0)");
				increaseEnergyStatement.execute();
				return null;
			}
		}.run();
	}

	public Song getRandomSong() {
		return new DbTask<Song>() {
			@Override
			public Song exec() throws SQLException {
				PreparedStatement statement = conn
						.prepareStatement("select count(*) from song;");
				statement.execute();
				int songCount = statement.getResultSet()
						.getInt(1);
				conn.close();
				int randomSongId = ((Double) Math.ceil(Math.random()
						* songCount)).intValue();
				Song song = findSongById(randomSongId);
				if (song == null)
					return getRandomSong();
				return song;
			}
		}.run();
	}

	public int createSet(final String setName) {
		return new DbTask<Integer>() {
			@Override
			public Integer exec() throws SQLException {
				PreparedStatement insertSetStmt = conn
						.prepareStatement("insert into song_set (name) values (?)");
				System.out.println(insertSetStmt);
				insertSetStmt.setString(1, setName);
				insertSetStmt.execute();

				PreparedStatement getSetIdStmt = conn
						.prepareStatement("select * from song_set where name = ?");
				getSetIdStmt.setString(1, setName);
				ResultSet resultSet = getSetIdStmt.executeQuery();
				int id = resultSet.getInt("id");
				System.out.println("id = " + id);
				return id;
			}
		}.run();
	}

	public SongSet findSetById(final int setId, final boolean includeSongs) {
		return new DbTask<SongSet>() {
			@Override
			public SongSet exec() throws SQLException {
				SongSet songSet = null;
				PreparedStatement stmt = conn
						.prepareStatement("select * from song_set where id = ?");
				stmt.setInt(1, setId);
				ResultSet resultSet = stmt.executeQuery();
				String name = resultSet.getString("name");
				songSet = new SongSet();
				songSet.setName(name);
				songSet.setId(setId);

				if (includeSongs) {
					System.out.println(setId);
					stmt = conn
							.prepareStatement("select * from song where id in (select song_id from set_membership where set_id = ?)");
					stmt.setInt(1, setId);
					List<Song> songs = resultSetToSongs(
							stmt.executeQuery(), conn);
					songSet.setSongs(songs);
				}
				return songSet;
			}
		}.run();
	}
	
	public SongSet findSetByName(final String name) {
		return new DbTask<SongSet>() {
			@Override
			public SongSet exec() throws SQLException {
				SongSet songSet = null;
				PreparedStatement stmt = conn
						.prepareStatement("select * from song_set where name = ?");
				stmt.setString(1, name);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					String name = resultSet.getString("name");
					int id = resultSet.getInt("id");
					songSet = new SongSet();
					songSet.setName(name);
					songSet.setId(id);
				}
				return songSet;
			}
		}.run();
	}

	public void addSongToSet(final int songId, final int setId) {
		new DbTask<Void>() {
			@Override
			public Void exec() throws SQLException {
				PreparedStatement stmt = conn.prepareStatement("insert into set_membership (song_id, set_id) values (?,?)");
				stmt.setInt(1, songId);
				stmt.setInt(2, setId);
				stmt.execute();
				return null;
			}
		}.run();
	}
	
	/**
	 * function primarily for adding a list of filenames (easily obtained from an iTunes playlist) to a set 
	 * @param filenames
	 */
	//TODO make sure this function can't be called by any user on the street
	public void addFilenamesToSet(final List<String> filenames, final int setId)
	{
		//just run the query and select he songs one by one instead of getting fancy with comma separation
		
		new DbTask<Void>() {
		
			@Override
			public Void exec() throws SQLException {
				PreparedStatement stmt = conn.prepareStatement("select id from song where filename = ?");
				PreparedStatement addSongStatement = conn.prepareStatement("insert into set_membership (song_id, set_id) values (?,?)");

				for (String filename: filenames)
				{
					stmt.setString(1, filename);
					ResultSet resultSet = stmt.executeQuery();
					if (resultSet.next()) {
						int songId = resultSet.getInt(1);
						addSongStatement.setInt(1, songId);
						addSongStatement.setInt(2, setId);
						addSongStatement.execute();
					}
				}
				return null;
			}
		}.run();
	}
	
	public void emptySet(final SongSet songSet)
	{
		new DbTask<Void>(){
		
			@Override
			public Void exec() throws SQLException {
				if (songSet != null) {
					PreparedStatement preparedStatement = conn.prepareStatement("delete from set_membership where set_id = ?");
					preparedStatement.setInt(1, songSet.getId());
					preparedStatement.execute();
				}
				return null;
			}
		}.run();
	}
	
	public void banSong(final int songId)
	{
		new DbTask<Void>() {
			@Override
			public Void exec() throws SQLException {
				PreparedStatement preparedStatement = conn.prepareStatement("update song set banned = ? where id = ?");
				preparedStatement.setBoolean(1, true);
				preparedStatement.setInt(2, songId);
				preparedStatement.execute();
				return null;
			}
		}.run();
	}
	
	public Map<Integer,String> getSetNames ()
	{
		return new DbTask<Map<Integer,String>>() {
		
			@Override
			public Map<Integer,String> exec() throws SQLException {
				Map<Integer,String> results = new HashMap<Integer,String>();
				PreparedStatement preparedStatment = conn.prepareStatement("select id, name from song_set");
				ResultSet resultSet = preparedStatment.executeQuery();
				while (resultSet.next()) {
					results.put(resultSet.getInt(1), resultSet.getString(2));
				}
				return results;
			}
		}.run();
	}
	
	public void setLastPlayed (final Song song)
	{
		new DbTask<Void>() {
		
			@Override
			public Void exec() throws SQLException {
				PreparedStatement preparedStatment = conn.prepareStatement("update song set lastPlayed = ? where id = ?");
				preparedStatment.setDate(1, new Date(System.currentTimeMillis()));
				preparedStatment.setInt(2, song.getId());
				preparedStatment.execute();
				return null;
			}
		}.run();
	}
}
