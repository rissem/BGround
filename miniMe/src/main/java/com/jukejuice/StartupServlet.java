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
		log.info("Server has been initialized.");
		
		Db db = new Db();
		try {
			db.initDb();
		} catch (SQLException e) {
			log.error("", e);
		}
		
		audioPlayer = new VlcPlayer();
		Timer timer = new Timer();
		env = new Properties();
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("env.properties");
		log.info("stream = " + stream);
		if (stream != null) {
			try {
				env.load(stream);
				log.info(env);
			} catch (IOException e) {
				log.error("", e);
			}
		}
		log.info(env.getProperty("fakePlayer"));
		if (env != null && "true".equals(env.getProperty("fakePlayer"))) {
			log.info("Faking playback instead of using VLC");
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					log.info("popping song");
					PlaylistManager.getInstance(getServletContext()).pop();
				}
			}, 1000*20, 1000 * 30);
		}
		else {
			timer.schedule(new VlcTask(), 0, 1000);
		}
		timer.schedule(new EnergyTask(), 0, 7 * 60 * 1000);
		timer.schedule(new PlaylistTask(), 0, 5000);
		timer.schedule(new KeepVlcAliveTask(),0, 5 *1000); 
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
