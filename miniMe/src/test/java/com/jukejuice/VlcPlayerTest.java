package com.jukejuice;

import org.apache.log4j.Logger;
import org.junit.Test;

public class VlcPlayerTest {
	public static Logger log = Logger.getLogger(VlcPlayerTest.class);	

	@Test
	public void testPop() {
		VlcPlayer player = new VlcPlayer();
		player.updatePlaylist();
/*
		try {
			JSONObject obj = new JSONObject("{\"queued_mp3\":{\"id\":9,\"mp3\":{\"file_path\":\"/tmp/music/Black_Eyed_Peas_-_Where_is_the_love.mp3\",\"id\":16,\"song\":{\"album\":\"Where is the Love-(Promo CDS)\",\"artist\":\"Black Eyed Peas Feat. Justin T\",\"id\":16,\"title\":\"Where is the Love\"}}}});");
			log.info(obj);
		} catch (JSONException e) {
			log.error("", e);
		}
*/				
		
	}
}
