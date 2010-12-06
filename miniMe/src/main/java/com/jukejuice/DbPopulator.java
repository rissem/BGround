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

	
	public void init () throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, SQLException
	{
		String [] mp3SongFilenames = Util.exec("find " + getMusicDirectory() + " -name *mp3").split("\n");
		String [] m4aSongFilenames = Util.exec("find " + getMusicDirectory() + " -name *m4a").split("\n");
		String[] songFilenames = new String[mp3SongFilenames.length + m4aSongFilenames.length];
		System.arraycopy(mp3SongFilenames, 0, songFilenames, 0, mp3SongFilenames.length);
		System.arraycopy(m4aSongFilenames, 0, songFilenames, mp3SongFilenames.length, m4aSongFilenames.length);

		List<SongFileInfo> songInfos = new ArrayList<SongFileInfo>();
		for (String songFilename: songFilenames)
		{
			if (! "".equals(songFilename))
			{
				songInfos.add(new SongFileInfo(songFilename));
				log.info("added song " + songFilename);
			}
		}
		Db db = getDb();
		db.dropSongTable();
		db.initDb();
		db.addSongs(songInfos);
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