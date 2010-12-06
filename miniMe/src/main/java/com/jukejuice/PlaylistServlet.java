package com.jukejuice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 8052599430418595916L;
	private static final Logger log = Logger.getLogger(PlaylistServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PlaylistManager playlistManager = PlaylistManager.getInstance(getServletContext());
		log.debug("Playlist Size = " + playlistManager.getPlaylist().size());

		JSONObject playlist = PlaylistManager.getInstance(getServletContext()).toJson();
		try {
			playlist.put("maxEnergy", ((User) req.getAttribute("user")).getMaxEnergy());
			playlist.put("currentEnergy", ((User) req.getAttribute("user")).getEnergy());
			
		} catch (JSONException e) {
			log.error("", e);
		}
		resp.getWriter().append(playlist.toString());
	}
}
