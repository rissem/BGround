package com.jukejuice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class FetchSetServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = -3052567553532304255L;
	private static final Logger log = Logger.getLogger(FetchSetServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		SetService setService = new SetService();
		try {
			JSONObject results = setService.getJsonResults(Integer.parseInt(req.getParameter("setId")));
			writer.write(results.toString());
		} catch (Exception e) {
			log.error("", e);
		}

		
	}
}
