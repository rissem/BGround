package com.jukejuice;

import java.util.List;

public class SongSet {
	private int id;
	private List<Song> songs;
	private String setName;
	
	public void setName(String setName)
	{
		this.setSetName(setName);
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getSetName() {
		return setName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
