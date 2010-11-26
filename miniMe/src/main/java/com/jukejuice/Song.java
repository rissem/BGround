package com.jukejuice;

public class Song 
{
	private int score;
	private int id;
	private String artist;
	private String title;
	
	public Song(int id, String artist, String title)
	{
		this.id = id;
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
	
	//	public JSONObject toJson()
//	{
		
//	}
}
