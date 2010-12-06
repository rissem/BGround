package com.jukejuice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class EnqueueServlet 
	extends HttpServlet
{

	private static final long serialVersionUID = 3095537533171813026L;
	private static final Logger log = Logger.getLogger(EnqueueServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		
		try {
			JSONObject result = PlaylistManager.getInstance(getServletContext()).
				enqueue(Integer.parseInt(req.getParameter("songId")), (User) req.getAttribute("user"));
			if (result != null)
				writer.write(result.toString());
			else {
				JSONObject obj = new JSONObject();
				obj.put("outOfEnergy", true);
				writer.write(obj.toString());
			}
		} catch (NumberFormatException e) {
			log.error("", e);
		} catch (SQLException e) {
			log.error("", e);
		} catch (JSONException e) {
			log.error("", e);
		}
	}
}
