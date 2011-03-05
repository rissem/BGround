package com.jukejuice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class is responsible for all interactions with the VLC media player.
 * TODO extract an interface so that it would be easy to switch to another audio player
 * 
 * @author mike
 */
public class VlcPlayer 
{
	
	private static final Logger log = Logger.getLogger(VlcPlayer.class);
	
	public void pause() {
		try {
			Util.fetchUrl("http://127.0.0.1:8081/pause.html");
		} catch (IOException e) {
			log.error("", e);
		}
	}

	public void skip() {
		try {
			Util.fetchUrl("http://127.0.0.1:8081/skip.html");
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	public void volumeUp() {
		try {
			Util.fetchUrl("http://127.0.0.1:8081/requests/status.xml?command=volume&val=%2B20");
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	public void volumeDown(){
		try {
			Util.fetchUrl("http://127.0.0.1:8081/requests/status.xml?command=volume&val=-20");
		} catch (IOException e) {
			log.error("", e);
		}
	}

	/**
	 * This interface assumes whenever VLC has stopped the end of VLC's playlist has been reached
	 * and it is time to begin playing the song currently on the top of tomcat's playlist
	 * @param playlistManager
	 */
	public void updatePlaylist(PlaylistManager playlistManager) {
		log.debug("VLC status = *" + getStatus() + "*");
		List<Song> playlist = playlistManager.getPlaylist();
		log.debug("playlist size = *" + playlist.size() + "*");		
		if ((getStatus().startsWith("stop")) && playlist.size() != 0)
		{
			Song song = playlist.remove(0);
			Db db = new Db();
			db.setLastPlayed(song);
			playlistManager.setNowPlaying(song);
			log.debug("playing the song " + song.getFilename());
			playSong(song);
		}
	}	

	private String getStatus() {
		try
		{
			return Util.fetchUrl("http://127.0.0.1:8081/status.html");
		}
		catch (IOException e)
		{
			log.error("", e);
			return "NOT_RUNNING";
		}
	}
	
	private void playSong(Song song) {
		try
		{
			String escapedFilename = URLEncoder.encode(song.getFilename(), "UTF-8").replaceAll("\\+", "%20");
			log.info("escaped fileanme = " + escapedFilename);
			Util.fetchUrl("http://127.0.0.1:8081/addPlay.html?songName=" + escapedFilename);
		}
		catch (Exception e)
		{
			log.error("", e);
		}
	}
	
	public boolean isAlive() {
		try {
			Util.fetchUrl("http://127.0.0.1:8081/status.html");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
