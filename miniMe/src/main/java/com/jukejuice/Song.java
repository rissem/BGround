package com.jukejuice;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


public class Song 
{
	private int score;
	private int id;
	private String artist;
	private String album;
	private int year; 
	private String title;
	private String filename;
	private int length; //length of song in seconds

	private static final Logger log = Logger.getLogger(Song.class);
	
	public Song(int id, String filename, String artist, String title, String album, int year, int length)
	{
		log.debug("song instantiated with id = " + id);
		this.id = id;
		this.setFilename(filename);
		this.artist = artist;
		this.title = title;
		this.album = album;
		this.year = year;
		this.length = length;
	}
	
	public void boost(int amount)
	{
		score += amount;
	}
	
	public void slam (int amount)
	{
		score -= amount;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public String getArtist()
	{
		return artist;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getAlbum()
	{
		return album;
	}
	
	public int getYear()
	{
		return year;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Song))
			return false;
		Song song = (Song) obj;
		if (song.getId() == this.getId())
			return true;
		return false;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}
	
	public int getLength() {
		return length;
	}
	
	public JSONObject toJson() throws JSONException
	{
		JSONObject song = new JSONObject();
		song.put("score", getScore());
		song.put("id", getId());
		song.put("artist", getArtist());
		song.put("title", getTitle());
		song.put("album", getAlbum());
		song.put("year", getYear());
		song.put("length", getLength());
		song.put("filename", getFilename());
		return song;
	}

	public static JSONObject mysterySong() throws JSONException {
		JSONObject song = new JSONObject();
		song.put("score", 0);
		song.put("id", 31415927);
		song.put("artist", "???");
		song.put("title", "???");
		song.put("album", "???");
		song.put("year", 10);
		song.put("length", 260);
		song.put("filename", "???");
		return song;
	}
		
//	}
}
