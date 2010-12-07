package com.jukejuice;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


public class Song 
{
	private int score;
	private int id;
	private String artist;
	private String title;
	private String filename;

	private static final Logger log = Logger.getLogger(Song.class);
	
	public Song(int id, String filename, String artist, String title)
	{
		log.debug("song instantiated with id = " + id);
		this.id = id;
		this.setFilename(filename);
		this.artist = artist;
		this.title = title;
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
	//	public JSONObject toJson()
//	{

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}
	
	public JSONObject toJson() throws JSONException
	{
		JSONObject song = new JSONObject();
		song.put("score", getScore());
		song.put("id", getId());
		song.put("artist", getArtist());
		song.put("title", getTitle());
		song.put("filename", getFilename());
		return song;
	}

	public static JSONObject mysterySong() throws JSONException {
		JSONObject song = new JSONObject();
		song.put("score", 0);
		song.put("id", 31415927);
		song.put("artist", "???");
		song.put("title", "???");
		song.put("filename", "???");
		return song;
	}
		
//	}
}
