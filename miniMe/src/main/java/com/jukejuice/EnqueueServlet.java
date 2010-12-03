package com.jukejuice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class EnqueueServlet 
	extends HttpServlet
{

	private static final long serialVersionUID = 3095537533171813026L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		
		try {
			JSONObject result = PlaylistManager.getInstance(getServletContext()).
				enqueue(Integer.parseInt(req.getParameter("songId")));
			writer.write(result.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
