package com.jukejuice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlaylistManagerTest {

	public DbPopulator dbPopulator;

	@Before
	public void setup() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, ClassNotFoundException, SQLException
	{
		Db db = new Db();
		db.dropSongTable();
		db.setSqliteFilePath(Db.TESTING_DB);
		dbPopulator = new DbPopulator();
		dbPopulator.setDb(db);
		File markerFile = new File(Util.resource("testMusicMarker"));
		String musicDirectory = markerFile.getParent();
		dbPopulator.setMusicDirectory(musicDirectory);
		dbPopulator.init();
	}
	
	@Test
	public void testEmptyPlaylistManager()
	{
		PlaylistManager playlistManager = new PlaylistManager();
		assert(playlistManager.getPlaylist().size() == 0);
	}

	@Test
	public void addSong()
	{
		PlaylistManager playlistManager = new PlaylistManager();
		int songId = 128;
		Song song = new Song(songId,"Mike Risse", "The Greatest Song Ever");
		playlistManager.enqueue(song);
		Assert.assertTrue(playlistManager.getPlaylist().size() == 1);
		Assert.assertTrue(playlistManager.findSong(songId).equals(song));
	}
}
