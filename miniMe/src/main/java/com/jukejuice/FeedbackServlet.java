package com.jukejuice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class FeedbackServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 7761208634716021271L;
	private static final Logger log = Logger.getLogger(FeedbackServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info(req.getParameter("feedback"));
		resp.sendRedirect("http://localhost:8080/miniMe/indexF.html");
	}
}
