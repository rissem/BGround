package com.jukejuice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class DbPopulator {
	private String musicDirectory;
	private Db db;
	private Logger log = Logger.getLogger(DbPopulator.class);

	/**
	 * find all of the mp3s and m4as in the musicDirectory, parse their ID3 tags and add them to the database
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 * @throws SQLException
	 */
	public void init ()
	{
		Db db = getDb();
		try {
			db.dropTables();
			db.initDb();
		}
		catch (SQLException e)
		{
			log.error("", e);
		}
		
		String[] mp3SongFilenames = new String[1];
		String[] m4aSongFilenames = new String[1];
		try {
			mp3SongFilenames = Util.exec("find " + getMusicDirectory() + " -name *mp3").split("\n");
			m4aSongFilenames = Util.exec("find " + getMusicDirectory() + " -name *m4a").split("\n");			
		} catch (IOException e) {
			log.error("", e);
		}
		
		String[] songFilenames = new String[mp3SongFilenames.length + m4aSongFilenames.length];
		System.arraycopy(mp3SongFilenames, 0, songFilenames, 0, mp3SongFilenames.length);
		System.arraycopy(m4aSongFilenames, 0, songFilenames, mp3SongFilenames.length, m4aSongFilenames.length);

		List<SongFileInfo> songInfos = new ArrayList<SongFileInfo>();
		for (String songFilename: songFilenames)
		{
			if (! "".equals(songFilename))
			{
				songInfos.add(0, new SongFileInfo(songFilename));
				try {
					db.addSongs(songInfos);
					songInfos.remove(0);
					log.info("added song " + songFilename);
				} catch (SQLException e) {
					log.error("", e);
					songInfos.remove(0);
				}
			}
		}
	}
	
	public Db getDb()
	{
		if (db == null)
			db = new Db();
		return db;
	}
	
	public void setDb(Db db)
	{
		this.db = db;
	}
	
	public String getMusicDirectory()
	{
		if (musicDirectory == null)
			musicDirectory = System.getProperty("user.home") + "/Music";			
		return musicDirectory;
	}
	
	public void setMusicDirectory(String musicDirectory)
	{
		this.musicDirectory = musicDirectory;
	}
}