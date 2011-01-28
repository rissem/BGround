package com.jukejuice;

import java.sql.SQLException;
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
		return getDb().findSetById(setId, true);
	}

	public JSONObject getSetNames() throws JSONException
	{
		Db db = new Db();
		Map<Integer,String> idNameMap = db.getSetNames();
		JSONArray jsonArray = new JSONArray();
		for (Integer key: idNameMap.keySet())
		{
			JSONObject obj = new JSONObject();
			obj.put("id", key);
			obj.put("name", idNameMap.get(key));
			jsonArray.put(obj);
		}
		JSONObject results = new JSONObject();
		results.put("sets", jsonArray);
		return results;
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

	public void addSongToSet(int songId, int setId) throws SQLException
	{
		SongSet set = getDb().findSetById(setId, true);
		Song song = getDb().findSongById(songId);
		if (! set.getSongs().contains(song))
			getDb().addSongToSet(songId, setId);
	}
	
	public void removeSongFromSet(int songId, int setId)
	{
		
	}
	
	public Map<String, Integer> getSetIdNameMap ()
	{
		return null;
	}
	
	public JSONObject getJsonResults(int setId) {
		log.info("fetching JSON results");
		
		JSONObject results = new JSONObject();
		JSONArray jsonSongs = new JSONArray();
		SongSet songSet = getDb().findSetById(setId, true);
		for (Song song: songSet.getSongs())
		{
			try {
				jsonSongs.put(song.toJson());
			} catch (JSONException e) {
				log.error("", e);
			}
		}
		try {
			results.put("songs", jsonSongs);
			results.put("title", songSet.getSetName());
		} catch (JSONException e) {
			log.error("", e);
		}
		return results;
	}
}
