package com.jukejuice;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;


public class SearchService

{
	private Db db;
	
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

	public JSONArray getJsonResults(String query) {
		JSONArray results = new JSONArray();
		try {
			List<Song> songs = getDb().search(query);
			results = new JSONArray();
			for (Song song: songs)
			{
				try {
					results.put(song.toJson());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return results;
	}	
}
