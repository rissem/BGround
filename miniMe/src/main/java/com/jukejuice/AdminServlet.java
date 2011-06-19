package com.jukejuice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allow the pausing of songs, increasing/decreasing volume etc.
 * This class should only be accessible by employees or privileged patrons
 * @author mike
 *
 */
public class AdminServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		VlcPlayer player = new VlcPlayer();
		if ("pause".equals(action))
			player.pause();
		if ("volumeUp".equals(action))
			player.volumeUp();
		if ("volumeDown".equals(action))
			player.volumeDown();
		if ("skip".equals(action))
			player.skip();
	}	
}
