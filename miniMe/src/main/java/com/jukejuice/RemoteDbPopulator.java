package com.jukejuice;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;

public class RemoteDbPopulator {
	private String musicDirectory;
	private Logger log = Logger.getLogger(RemoteDbPopulator.class);

	/**
	 * find all of the mp3s and m4as in the musicDirectory, parse their ID3 tags and add them to the database
	 * @throws KeyNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 * @throws SQLException
	 */
	public List<File> sync () {
		//store the lastUpdated time in a file?
		
		List<File> files = getAudioFiles(new File(getMusicDirectory()), 0l);
		log.info(files);
		return files;
		//what songs have been deleted?
		//get a list of all songs from the database
		//build up a list of songs that no longer exist
		//submit this list back to the server for removal from the database

		/*
		for (String songFilename: songFilenames)
		{
			if (! "".equals(songFilename)) {
				try {
//					SongFileInfo songFileInfo = new SongFileInfo(songFilename);
					StringBuffer url= new StringBuffer();
					url.append("http://localhost:8124/addSong");
//					url.append("?artist=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.ARTIST))); 				
//					url.append("&title=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.TITLE)));
//					url.append("&album=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.ALBUM)));
				}
				catch (Exception e)
				{
					log.error(e);
				}
				log.info("added song " + songFilename);

			}
		}
		*/
	}
	
	//TODO only process files after a specified timestamp
	public List<File> getAudioFiles(File directory, long lastUpdated)
	{
		List<File> audioFiles = new ArrayList<File>();
		for (File file: directory.listFiles()) {
			if (file.isFile() && (file.getName().endsWith("mp3") || file.getName().endsWith("m4a"))) {
				if (file.lastModified() > lastUpdated) 
					audioFiles.add(file);
			}
			else if (file.isDirectory() && ! file.getAbsolutePath().equals(directory.getAbsolutePath()))
			{
				log.info(file.getAbsolutePath());
				log.info(directory.getAbsolutePath());
				log.info(file.equals(directory));
				log.info(file.getAbsolutePath().equals(directory.getAbsolutePath()));
				audioFiles.addAll(getAudioFiles(file, lastUpdated));
			}
		}
		return audioFiles;
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
