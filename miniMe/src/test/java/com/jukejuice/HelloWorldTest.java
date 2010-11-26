package com.jukejuice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;

public class HelloWorldTest {
	
	@Test
	public void sampleTest() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, ClassNotFoundException, SQLException {
		File markerFile = new File(Util.resource("testMusicMarker"));
		String musicDirectory = markerFile.getParent();

		Db db = new Db();
		db.setSqliteFilePath("testing.db");

		DbPopulator dbPopulator = new DbPopulator();
		dbPopulator.setDb(db);
		dbPopulator.setMusicDirectory(musicDirectory);
		dbPopulator.init();
	}
	
	@Test
	public void sampleTest2() {
		assert 2 == 2;
	}
}

   