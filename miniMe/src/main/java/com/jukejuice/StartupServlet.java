package com.jukejuice;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;

public class StartupServlet 
	extends HttpServlet
{
	private static final long serialVersionUID = 1579930295271758659L;

	AudioPlayer audioPlayer;
	
	public void init()
	{
		System.out.println("Server has been initialized.");
		audioPlayer = new AudioPlayer();
		Timer timer = new Timer();
		timer.schedule(new VlcTask(), 0, 500);
	}
	
	class VlcTask extends TimerTask
	{
		@Override
		public void run() {
			audioPlayer.updatePlaylist(PlaylistManager.getInstance(getServletContext()));
		}
	}
}
