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
		timer.schedule(new EnergyTask(), 0, 5 * 60 * 1000);
		timer.schedule(new PlaylistTask(), 0, 5000);
	}
	
	class VlcTask extends TimerTask
	{
		@Override
		public void run() {
			audioPlayer.updatePlaylist(PlaylistManager.getInstance(getServletContext()));
		}
	}
	
	class EnergyTask extends TimerTask
	{
		@Override
		public void run() {
			Db db = new Db();
			db.regenerateEnergy();
		}
	}
	
	class PlaylistTask extends TimerTask
	{
		@Override
		public void run() {
			PlaylistManager.getInstance(getServletContext()).addRandomSongIfEmpty();
		}
	}
}
