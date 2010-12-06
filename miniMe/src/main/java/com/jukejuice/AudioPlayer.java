package com.jukejuice;

import java.util.List;

//TODO extract an interface so that it would be easy to switch to another media playing application
public class AudioPlayer {
	
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

	public void updatePlaylist(PlaylistManager playlistManager) {
		//if status is stopped then a song should immediately be added to the playlist
		System.out.println("status = *" + getStatus() + "*");
		List<Song> playlist = playlistManager.getPlaylist();
		System.out.println("playlist size = *" + playlist.size() + "*");		
		if ((getStatus().startsWith("stop")) && playlist.size() != 0)
		{
			Song song = playlist.remove(0);
			playlistManager.setNowPlaying(song);
			System.out.println("playing the song " + song.getFilename());
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
