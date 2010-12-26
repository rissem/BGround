package com.jukejuice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Before;
import org.junit.Test;

public class SetServiceTest {
	public SetService setService;
	public DbPopulator dbPopulator;
	public Db db;
	public static final Logger log = Logger.getLogger(SetServiceTest.class);
	
	@Before
	public void setup() throws SQLException, CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		db = new Db();
		db.setSqliteFilePath(Db.TESTING_DB);
		db.dropTables();
		dbPopulator = new DbPopulator();
		dbPopulator.setDb(db);
		File markerFile = new File(Util.resource("testMusicMarker"));
		String musicDirectory = markerFile.getParent();
		dbPopulator.setMusicDirectory(musicDirectory);
		dbPopulator.init();
		setService = new SetService();
		setService.setDb(db);
	}
	
	@Test
	public void testSetCreation() throws SQLException
	{
		int setId = setService.createSet("Jazz Legends");
		System.out.println("SETID = " + setId);
		Assert.assertNotNull(setService.getSet(setId));
		Song song = db.findSongById(1);
		setService.addSongToSet(song.getId(), setId);
		SongSet set = setService.getSet(setId);
		System.out.println(set);
		System.out.println(set.getSongs());
		Assert.assertTrue(set.getSongs().contains(song));
	}
}
