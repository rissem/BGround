package com.jukejuice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistManager 
{
	private static String PLAYLIST_MANAGER = "playlistManager";
	private List<Song> playlist = new ArrayList<Song>();
	private Song nowPlaying = null;
	private Db db;
	
	private static final Logger log = Logger.getLogger(PlaylistManager.class);
	
	public static PlaylistManager getInstance(ServletContext context)
	{
		PlaylistManager playlistManager = (PlaylistManager) context.getAttribute(PLAYLIST_MANAGER);
		if (playlistManager == null)
		{
			playlistManager = new PlaylistManager();
			context.setAttribute(PLAYLIST_MANAGER, playlistManager);
		}
		return playlistManager;
	}
	
	public JSONObject toJson()
	{
		JSONObject playlistJson = null;
		try {
			playlistJson = new JSONObject();
			if (nowPlaying != null) 
				playlistJson.put("nowPlaying", nowPlaying.toJson());
			else
				playlistJson.put("nowPlaying", Song.mysterySong());
			JSONArray songs = new JSONArray();
			for (Song song: playlist)
			{
				songs.put(song.toJson());
			}
			playlistJson.put("songs", songs);
		}
		catch (JSONException e)
		{
			log.error("", e);
		}
		return playlistJson;
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
	
	public String enqueue(int songId, User user, boolean charge) throws SQLException
	{
		Song song = getDb().findSongById(songId);
		Calendar cal= Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30);
		Date thirtyMinutesAgo = cal.getTime();
		if (song.getLastPlayed() != null && song.getLastPlayed().after(thirtyMinutesAgo))
			return "Song was played too recently";
		if (playlist.contains(song))
			return "Song already on playlist";
		
		if (! charge) {
			enqueue(getDb().findSongById(songId));
			return "Song added (free because you're on the beatlist player)";
		}
		
		if (user.getEnergy() > 0)
		{
			user.useEnergy(1);
			user.persist();
			enqueue(getDb().findSongById(songId));
			return "Song added";
		}
		return "Not enough credits";
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
	
	public Song getNowPlaying()
	{
		return nowPlaying;
	}
	
	public void setNowPlaying(Song song)
	{
		this.nowPlaying = song;
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

	public void addRandomSongIfEmpty() {
		if (getPlaylist().size() == 0)
		{
			Db db = new Db();
			Song song = db.getRandomSong();
			playlist.add(song);
		}
	}	
}
