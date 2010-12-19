package com.jukejuice;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SetService {
	private static final Logger log = Logger.getLogger(SetService.class);
	
	private Db db;
	
	public SongSet getSet(int setId)
	{
		return getDb().findSetById(setId);
	}
	
	public int createSet(String setName)
	{
		return getDb().createSet(setName);
	}
	
	public Db getDb() {
		if (db == null)
			db = new Db();
		return db;
	}
	
	public void setDb(Db db) {
		this.db = db;
	}

	public void addSongToSet(int setId, int songId)
	{
		
	}
	
	public void removeSongFromSet(int songId, int setId)
	{
		
	}
	
	public Map<String, Integer> getSetIdNameMap ()
	{
		return null;
	}
	
	public JSONObject getJsonResults(int setId) {
		JSONObject results = new JSONObject();
		JSONArray jsonSongs = new JSONArray();
		Db db = new Db();
		List<Song> songs = db.getSet(setId);
		for (Song song: songs)
		{
			try {
				jsonSongs.put(song.toJson());
			} catch (JSONException e) {
				log.error("", e);
			}
		}
		return results;
	}
}
