package com.jukejuice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

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
	public void updatePlaylist() {
		log.info("updating...");
		if (getStatus().startsWith("stop"))
		{
			String venue = "summit"; 
			try {
				log.info("status is stop");
				URL url = new URL("http://localhost:3000/" + venue + "/pop.json");
				URLConnection connection = url.openConnection();
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				StringBuffer response = new StringBuffer();
				String inputLine;
				while ((inputLine = in.readLine()) != null) { 
					response.append(inputLine);
				}
				JSONObject object = new JSONObject(response.toString());
				String file_path = object.getJSONObject("queued_mp3").getJSONObject("mp3").getString("file_path");
				System.out.println("file path = " + file_path);
				playSong(file_path);
			}
			catch (Exception e)
			{
				log.error("", e);
			}
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
	
	private void playSong(String filename) {
		try
		{
			String escapedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			log.info("escaped filename = " + escapedFilename);
			String newsong = Util.fetchUrl("http://127.0.0.1:8081/addPlay.html?songName=" + escapedFilename);
			log.debug("new song = " + newsong);
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
