package com.jukejuice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;

public class SetNamesServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = -3665926590628473153L;
	private static final Logger log = Logger.getLogger(SetNamesServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		SetService setService = new SetService();
		PrintWriter writer = resp.getWriter();
		try {
			writer.write(setService.getSetNames().toString());
		} catch (JSONException e) {
			log.error("", e);
		}
		
	}
}
