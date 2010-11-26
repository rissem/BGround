package com.jukejuice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class DbPopulator {
	private String musicDirectory;
	
	public DbPopulator(String musicDirectory)
	{
		this.musicDirectory = musicDirectory;
	}
	
	public DbPopulator() {}
	
	public void init () throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, ClassNotFoundException, SQLException
	{
		//drop song table if it exists
		//create song table
		
		if (musicDirectory == null)
			musicDirectory = System.getProperty("user.home") + "/Music";
		
		String [] songFilenames = Util.exec("find " + musicDirectory + " -name *mp3").split("\n");
		List<SongFileInfo> songInfos = new ArrayList<SongFileInfo>();
		for (String songFilename: songFilenames)
		{
			songInfos.add(new SongFileInfo(songFilename));
		}
		Db db = new Db();
		db.createSongTable();
		db.addSongs(songInfos);
	}	
}