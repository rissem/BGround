package com.jukejuice;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;

public class RemoteDbPopulator {
	private String musicDirectory;
	private String venue;
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
	public void sync () {
		//store the lastUpdated time in a file?
		
		List<File> mp3s = getAudioFiles(new File(getMusicDirectory()), 0l);
		for (File mp3: mp3s)
		{
			try {
				SongFileInfo songFileInfo = new SongFileInfo(mp3.getAbsolutePath());
				StringBuffer address = new StringBuffer();
				address.append("http://localhost:3000/" + getVenue() + "/add_mp3.json");
				address.append("?artist=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.ARTIST))); 				
				address.append("&title=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.TITLE)));
				address.append("&album=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.ALBUM)));
				address.append("&file_path=" + URLEncoder.encode(mp3.getAbsolutePath()));
		        URL url = new URL(address.toString());
				URLConnection connection = url.openConnection();
				connection.getContent();
			}
			catch (Exception e)
			{
				log.error(e);
			}
		}
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
				audioFiles.addAll(getAudioFiles(file, lastUpdated));
			}
		}
		return audioFiles;
	}
	
	public String getVenue()
	{
		return venue;
	}
	
	public void setVenue(String venue)
	{
		this.venue = venue;
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
