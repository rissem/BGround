package com.jukejuice;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class UpdateRemoteDbServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = -7896422250359343262L;
	private static final Logger log = Logger.getLogger(UpdateRemoteDbServlet.class);

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		log.info("testing123");
		RemoteDbPopulator dbPopulator = new RemoteDbPopulator();
		dbPopulator.setMusicDirectory(req.getParameter("musicDir"));
		dbPopulator.setVenue(Util.getEnvProperties().getProperty("venue"));
		dbPopulator.setHost(Util.getEnvProperties().getProperty("host"));
		dbPopulator.sync();		
	}
}
