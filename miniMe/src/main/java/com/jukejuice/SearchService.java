package com.jukejuice;

import java.sql.SQLException;
import java.util.List;


public class SearchService

{
	private Db db;
	
	public String getHtmlResults(String query) throws ClassNotFoundException, SQLException
	{
		StringBuffer sb = new StringBuffer();
		List<Song> songs = getDb().search(query);
		for (Song song: songs)
		{
			sb.append(song.getTitle() + "<br />");
		}
		return sb.toString();
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
