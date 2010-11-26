package com.jukejuice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlaylistManager 
{
	private List<Song> playlist = new ArrayList<Song>();
	
	public void enqueue(List<Song> songs){
		playlist.addAll(songs);
	}
	
	public List<Song> getPlaylist()
	{
		return getPlaylist(0,100);
	}
	
	public List<Song> getPlaylist(int limit, int offset){
		return playlist.subList(offset, offset+limit);
	}
	
	public Song findSong(int songId)
	{
		for (Song song: playlist)
		{
			if (song.getId() == songId)
				return song;
		}
		return null;
	}
	
	public void boost(int songId, int amount /*userId at some point*/){
		Song song = findSong(songId);
		song.boost(amount);
		resort();
	}
	
	public void slam(int songId, int amount){
		Song song = findSong(songId);
		song.slam(amount);
		resort();
	}	

	public void enqueue(Song song){
		playlist.add(song);
	}
	
	private void resort() {
		Collections.sort(playlist, new Comparator<Song>()
		{
			public int compare(Song song1, Song song2) 
			{
				return song1.getScore() - song2.getScore();
			}
		}
		);
	}	
}
