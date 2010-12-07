package com.jukejuice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;

public class SearchSongsServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = -11188561767380050L;
	private static final Logger log = Logger.getLogger(SearchSongsServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		SearchService ss = new SearchService();
		try {
			JSONArray results = ss.getJsonResults(req.getParameter("search"));
			writer.write(results.toString());
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
}
