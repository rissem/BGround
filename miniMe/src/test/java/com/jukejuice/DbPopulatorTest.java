package com.jukejuice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DbPopulatorTest {
	
	Db db;
	public static Logger logger = Logger.getLogger(DbPopulatorTest.class);
	
	@Before
	public void setup() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, ClassNotFoundException, SQLException
	{
		db = new Db();
		db.setSqliteFilePath(Db.TESTING_DB);

		DbPopulator dbPopulator = new DbPopulator();
		dbPopulator.setDb(db);
		File markerFile = new File(Util.resource("testMusicMarker"));
		String musicDirectory = markerFile.getParent();
		dbPopulator.setMusicDirectory(musicDirectory);
		dbPopulator.init();
	}
	
	@Test
	public void findSongById() throws SQLException
	{
		Song song = db.findSongById(1);
		logger.info("this is a test");
		Assert.assertTrue("Song exists with ID 1", song != null);
		Assert.assertTrue("Song found by ID 1 has ID 1", song.getId() == 1);
	}
	
	@Test
	public void searchTest() throws SQLException
	{
		Song song = db.findSongById(1);
		String title = song.getTitle();
		Assert.assertTrue("Song exists with ID 1", song != null);
		Assert.assertTrue("search by a song's exact title finds song", db.search(title).contains(song));
		db.search(title.substring(4, title.length() - 2)).contains(song);
	}
	
	@Test
	public void testSongEquals()
	{
		Song song1 = new Song(1,"filename", "artist", "title");
		Song song2 = new Song(1,"filename", "artist", "title");
		Song song3 = new Song(3,"filename", "artist", "title");
		Assert.assertTrue(song1.equals(song2));
		Assert.assertFalse(song1.equals(song3));
	}
}

   