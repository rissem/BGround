package com.jukejuice;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class StartupServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 1579930295271758659L;
	private static final Logger log = Logger.getLogger(StartupServlet.class);
	
	AudioPlayer audioPlayer;
	
	public void init()
	{
		log.info("Server has been initialized.");
		
		Db db = new Db();
		try {
			db.initDb();
		} catch (SQLException e) {
			log.error("", e);
		}
		
		audioPlayer = new AudioPlayer();
		Timer timer = new Timer();
		timer.schedule(new VlcTask(), 0, 1000);
		timer.schedule(new EnergyTask(), 0, 7 * 60 * 1000);
		timer.schedule(new PlaylistTask(), 0, 5000);
	}
	
	class VlcTask extends TimerTask
	{
		@Override
		public void run() {
			try
			{
				audioPlayer.updatePlaylist(PlaylistManager.getInstance(getServletContext()));
			}
			catch (Exception e)
			{
				log.error("", e);
			}
		}
	}
	
	class EnergyTask extends TimerTask
	{
		@Override
		public void run() {
			try {
				Db db = new Db();
				db.regenerateEnergy();
			}
			catch (Exception e)
			{
				log.error("", e);
			}
		}
	}
	
	class PlaylistTask extends TimerTask
	{
		@Override
		public void run() {
			try {
				PlaylistManager.getInstance(getServletContext()).addRandomSongIfEmpty();
			}
			catch (Exception e)
			{
				log.error("", e);
			}
		}
	}
}
