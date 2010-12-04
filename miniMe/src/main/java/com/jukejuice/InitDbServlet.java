package com.jukejuice;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class InitDbServlet extends HttpServlet {
	private static final long serialVersionUID = 7262331411229610139L;
	private Logger log = Logger.getLogger(InitDbServlet.class);
	
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	{
		response.setContentType("plain/text");
		DbPopulator dbPopulator = new DbPopulator();
		try {
			log.info("initializing database");
			dbPopulator.init();
			JSONObject obj = new JSONObject();
			obj.put("success", true);
			response.getWriter().write(obj.toString());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}