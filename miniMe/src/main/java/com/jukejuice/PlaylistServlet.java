package com.jukejuice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlaylistServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 8052599430418595916L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PlaylistManager playlistManager = PlaylistManager.getInstance(getServletContext());
		System.out.println("Size = " + playlistManager.getPlaylist().size());
		resp.getWriter().append(PlaylistManager.getInstance
				(getServletContext()).toHtml());
	}
}
