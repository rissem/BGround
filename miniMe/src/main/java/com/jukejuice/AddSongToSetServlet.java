package com.jukejuice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AddSongToSetServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 3720926598572735586L;
	private static final Logger log = Logger.getLogger(AddSongToSetServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		SetService setService = new SetService();
		try {
			setService.addSongToSet(Integer.parseInt(req.getParameter("songId")), Integer.parseInt(req.getParameter("setId")));
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
