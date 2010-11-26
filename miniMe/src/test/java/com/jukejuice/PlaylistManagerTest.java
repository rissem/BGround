package com.jukejuice;

import org.junit.Test;

public class PlaylistManagerTest {

	@Test
	public void testEmptyPlaylistManager()
	{
		PlaylistManager playlistManager = new PlaylistManager();
		assert(playlistManager.getPlaylist().size() ==0);
	}

	@Test
	public void addSong()
	{
		PlaylistManager playlistManager = new PlaylistManager();
		int songId = 128;
		Song song = new Song(songId,"Mike Risse", "The Greatest Song Ever");
		playlistManager.enqueue(song);
		assert(playlistManager.getPlaylist().size() == 1);
		assert (playlistManager.findSong(songId).equals(song));
	}
}
