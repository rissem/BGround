package com.jukejuice;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BanSongServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 7268266191055423648L;
	//private static final Logger log = Logger.getLogger(BanSongServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	{
		if (req.getLocalAddr().equals(req.getRemoteAddr()))
		{
			Db db = new Db();
			db.banSong(Integer.parseInt(req.getParameter("songId")));
		}
	}
}
