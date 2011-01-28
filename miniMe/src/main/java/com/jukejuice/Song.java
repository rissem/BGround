package com.jukejuice;

import java.util.Date;
import java.util.Set;

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
	private boolean banned;
	private Set<SongSet> songSets;
	private Date lastPlayed;

	private static final Logger log = Logger.getLogger(Song.class);
	
	//TODO song objects should be able to be instantiated from the database
	//or from a filename which will use id3 tags to instantiate all the attributes
	//public Song(String filename)
	
	public Song(int id, String filename, String artist, String title, String album, int year, int length, boolean banned)
	{
		log.debug("song instantiated with id = " + id);
		this.id = id;
		this.setFilename(filename);
		this.artist = artist;
		this.title = title;
		this.album = album;
		this.year = year;
		this.length = length;
		this.banned = banned;
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

	public void setSongSets(Set<SongSet> songSet) {
		this.songSets = songSet;
	}

	public Set<SongSet> getSongSets() {
		return songSets;
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

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setLastPlayed(Date lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	public Date getLastPlayed() {
		return lastPlayed;
	}
}
