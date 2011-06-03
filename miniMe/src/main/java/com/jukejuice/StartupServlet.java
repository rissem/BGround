package com.jukejuice;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class StartupServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 1579930295271758659L;
	private static final Logger log = Logger.getLogger(StartupServlet.class);
	
	VlcPlayer audioPlayer;
	Properties env;
	
	public void init()
	{
		/*
		TODO - add back db for more efficient addition of songs (don't add songs) 
		
		Db db = new Db();
		try {
			db.initDb();
		} catch (SQLException e) {
			log.error("", e);
		}
		Timer timer = new Timer();
		
	*/
		env = Util.getEnvProperties();
		
		audioPlayer = new VlcPlayer();
		Timer timer = new Timer();
		log.info("starting it up");
		timer.schedule(new VlcTask(), 0, 1000);
		timer.schedule(new KeepVlcAliveTask(),0, 5 *1000);

	}
	
	class VlcTask extends TimerTask
	{
		@Override
		public void run() {
			try
			{
				audioPlayer.updatePlaylist();
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
	
	class KeepVlcAliveTask extends TimerTask
	{
		@Override
		public void run() {
			if (!audioPlayer.isAlive()) {
				try {
					log.error("vlc server is down; attempting to restart");
					Util.exec(env.getProperty("vlcScript"));
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
	}
}
