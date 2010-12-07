package com.jukejuice;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class is responsible for all interactions with the VLC media player.
 * TODO extract an interface so that it would be easy to switch to another audio player
 * 
 * @author mike
 */
public class AudioPlayer {
	
	private static final Logger log = Logger.getLogger(AudioPlayer.class);
	
	public void pause() {
		Util.fetchUrl("http://localhost:8081/pause.html");		
	}

	public void skip() {
		Util.fetchUrl("http://localhost:8081/skip.html");
	}
	
	public void volumeUp() {
		Util.fetchUrl("http://localhost:8081/requests/status.xml?command=volume&val=%2B20");
	}
	
	public void volumeDown(){
		Util.fetchUrl("http://localhost:8081/requests/status.xml?command=volume&val=-20");
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
			playlistManager.setNowPlaying(song);
			log.debug("playing the song " + song.getFilename());
			playSong(song);
		}
	}	

	private String getStatus() {
		return Util.fetchUrl("http://localhost:8081/status.html");
	}
	
	private void playSong(Song song) {
		String escapedFilename = song.getFilename().replaceAll(" ", "%20");
		Util.fetchUrl("http://localhost:8081/addPlay.html?songName=" + escapedFilename);
	}
}
