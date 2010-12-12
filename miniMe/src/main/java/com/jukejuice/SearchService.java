package com.jukejuice;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchService

{
	private Db db;
	private static final Logger log = Logger.getLogger(SearchService.class);
	
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

	public JSONObject getJsonResults(String query) {
		JSONObject results = new JSONObject();
		JSONArray jsonSongs = new JSONArray();
		try {
			List<Song> songs = getDb().search(query);
			for (Song song: songs)
			{
				try {
					jsonSongs.put(song.toJson());
				} catch (JSONException e) {
					log.error("", e);
				}
			}			
		} catch (SQLException e1) {
			log.error("", e1);
		}
		try {
			results.put("songs", jsonSongs);
			results.put("query", query);
		}
		catch (JSONException e){
			log.error("", e);
		}
		return results;
	}	
}
