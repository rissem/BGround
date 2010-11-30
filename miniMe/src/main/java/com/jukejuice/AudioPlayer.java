package com.jukejuice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

//TODO extract an interface so that it would be easy to switch to another media playing application
public class AudioPlayer {
	public void play() {

	}

	public void pause() {

	}

	public void skip() {

	}
	
	public void volumeUp() {
		
	}
	
	public void volumeDown(){
		
	}

	public void updatePlaylist(PlaylistManager playlistManager) {
		//if status is stopped this means
		// is a song currently playing?
		// if pop the top of the playlist and play in vlc
		System.out.println("status = *" + getStatus() + "*");
		List<Song> playlist = playlistManager.getPlaylist();
		System.out.println("playlist size = *" + playlist.size() + "*");		
		if ((getStatus().startsWith("stop")) && playlist.size() != 0)
		{
			//playlistManager.setNowPlaying()
			Song song = playlist.remove(0);
			System.out.println("playing the song " + song.getFilename());
			playSong(song);
		}
	}	

	private String getStatus() {
		String content = null;
		try
		{
			URL url = new URL("http://localhost:8081/status.html");
			URLConnection conn = url.openConnection();
			content = Util.convertStreamToString(conn.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	private void playSong(Song song) {
		String escapedFilename = song.getFilename().replaceAll(" ", "%20");
		System.out.println("url = " + " http://localhost:8081/addPlay.html?songName=" + escapedFilename);
		System.out.println(escapedFilename);
		try {
			URL url = new URL("http://localhost:8081/addPlay.html?songName=" + escapedFilename);
			URLConnection conn = url.openConnection();
			String content = Util.convertStreamToString(conn.getInputStream());
			System.out.print(content);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		song.getFilename();
	}
	
	private String[] getQueuedFiles() {
		String[] filenames = null;
		try {
			URL url = new URL("http://localhost:8081/playlistFiles.xml");
			URLConnection conn = url.openConnection();
			String content = Util.convertStreamToString(conn.getInputStream());
			System.out.println("content = " + content);

			if (content == "content = Undefined\nPlaylist\n	Media Library") {
				System.out.println("empty playlist");
			} else {
				String processedString;
				processedString = content.replace("Undefined\nPlaylist\n", "");
				processedString = processedString.replace("Media Library", "");
				filenames = processedString.split("\n");
				for (String filename : filenames) {
					System.out.println("filename = " + filename);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filenames;
	}
}
