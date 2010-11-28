package com.jukejuice;


public class Song 
{
	private int score;
	private int id;
	private String artist;
	private String title;
	private String filePath;
	
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

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
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
		
//	}
}
