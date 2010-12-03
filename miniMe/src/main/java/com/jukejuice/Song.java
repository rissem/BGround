package com.jukejuice;

import org.json.JSONException;
import org.json.JSONObject;


public class Song 
{
	private int score;
	private int id;
	private String artist;
	private String title;
	private String filename;
	
	public Song(int id, String filename, String artist, String title)
	{
		System.out.println("id = " + id);
		this.id = id;
		this.setFilename(filename);
		this.artist = artist;
		this.title = title;
	}
	//object storing interaction from user: boosts, slams, queues, 
	
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
		
//	}
}
