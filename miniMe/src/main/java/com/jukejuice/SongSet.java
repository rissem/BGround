package com.jukejuice;

import java.util.List;

public class SongSet {
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
}
