package com.jukejuice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;

public class PlaylistManager 
{
	private static String PLAYLIST_MANAGER = "playlistManager";
	private List<Song> playlist = new ArrayList<Song>();
	private Db db;
	
	public static PlaylistManager getInstance(ServletContext context)
	{
		PlaylistManager playlistManager = (PlaylistManager) context.getAttribute(PLAYLIST_MANAGER);
		if (playlistManager == null)
			playlistManager = new PlaylistManager();
		return playlistManager;
	}
	
	public List<Song> getPlaylist()
	{
		return playlist;
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
	
	public void enqueue(int songId){
		
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
	
	public Db getDb()
	{
		if (db == null)
			db = new Db();
		return db;
	}
	
	public void setDb(Db db)
	{
		this.db = db;
	}	
}
