package com.jukejuice;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;
import org.json.JSONArray;
import org.json.JSONObject;

public class RemoteDbPopulator {
	private String musicDirectory;
	private String venue;
	private Logger log = Logger.getLogger(RemoteDbPopulator.class);
	private String host;

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
		List<File> mp3Files = getAudioFiles(new File(getMusicDirectory()), 0l);
		JSONArray mp3s = new JSONArray();
		for (File mp3: mp3Files)
		{
			JSONObject mp3Info = new JSONObject();
			try {
				SongFileInfo songFileInfo = new SongFileInfo(mp3.getAbsolutePath());
				mp3Info.put("artist", songFileInfo.tag.getFirst(FieldKey.ARTIST));
				mp3Info.put("title", songFileInfo.tag.getFirst(FieldKey.TITLE));
				mp3Info.put("album", songFileInfo.tag.getFirst(FieldKey.ALBUM));
				mp3Info.put("file_path", mp3.getAbsolutePath());
				mp3s.put(mp3Info);
			}
			catch (Exception e)
			{
				log.error("", e);
			}
		}


		try {
			String address = "http://" + getHost() + "/" + getVenue() + "/sync_library.json";
			log.info("calling " + address);
			Map<String,String> data = new HashMap<String,String>();
			data.put("mp3s", mp3s.toString());
			log.info("mp3s string = "+ mp3s.toString());
			data.put("insane", "no");
			Util.postUrl(address, data);
		}
		catch (Exception e) {
			log.error("", e);
		}
	}
	
	//TODO only process files after a specified timestamp
	public List<File> getAudioFiles(File directory, long lastUpdated)
	{
		List<File> audioFiles = new ArrayList<File>();
		for (File file: directory.listFiles()) {
			if (file.isFile() && (file.getName().toLowerCase().endsWith("mp3") || file.getName().toLowerCase().endsWith("m4a"))) {
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

	public void setHost(String host) {
		this.host = host;
	}
	
	public String getHost() {
		return host;
	}
}
